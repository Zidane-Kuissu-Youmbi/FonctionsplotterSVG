import java.util.*;
import java.util.Map;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

sealed interface Expr1 permits BiOp, Var1, Num1, Ternary, UnOp, IsBool, BoolOp, VergleichOP {

    String toText();

    String toJavaScript();

    Expr1 eval(Map<String, Expr1> env);
}

record Num1(double number) implements Expr1 {
    public String toText() {
        return String.valueOf(number);
    }

    public Expr1 eval(Map<String, Expr1> env) {
        return this;
    }

    @Override
    public String toJavaScript() {
        return Double.toString(number);
    }
}

record Var1(String name) implements Expr1 {
    public String toText() {
        return name;
    }

    public Expr1 eval(Map<String, Expr1> env) {
        if (!env.containsKey(name))
            throw new RuntimeException("Variable " + name + " nicht gefunden.");
        return env.getOrDefault(name, new Num1(0).eval(env));
    }

    public String toJavaScript() {
        return name; // Variable einfach als JS-Variable übernehmen
    }

}

record UnOp(Op op, Expr1 mitte) implements Expr1 {
    public String toText() {
        return "(" + op.symbol() + " " + mitte.toText() + ")";
    }

    public Expr1 eval(Map<String, Expr1> env) {
        double mv = ((Num1) mitte.eval(env)).number();
        double result = op.apply(mv, 0);
        return new Num1(result);
    }

    public String toJavaScript() {
        String symbol = op.symbol();

        return switch (symbol) {
            case "~" -> "(-" + mitte.toJavaScript() + ")";
            case "√" -> "Math.sqrt(" + mitte.toJavaScript() + ")";
            case "!" -> "fact(" + mitte.toJavaScript() + ")";
            case "log" -> "Math.log10(" + mitte.toJavaScript() + ")";
            case "ln" -> "Math.log(" + mitte.toJavaScript() + ")";
            case "sin", "cos", "tan", "exp" -> "Math." + symbol + "(" + mitte.toJavaScript() + ")";
            default -> throw new RuntimeException("Unbekannter einstelliger Operator: " + symbol);
        };
    }
}
// Expr1
record BiOp(Op op, Expr1 left, Expr1 right) implements Expr1 {
    public String toText() {
        return "(" + left.toText() + " " + op.symbol() + " " + right.toText() + ")";
    }

    public Expr1 eval(Map<String, Expr1> env) {
        double lv = ((Num1) left.eval(env)).number();
        double rv = ((Num1) right.eval(env)).number();
        double result = op.apply(lv, rv);
        return new Num1(result);
    }

    public String toJavaScript() {
        String symbol = op.symbol();
        switch (symbol) {
            case "^":
                return "Math.pow(" + left.toJavaScript() + ", " + right.toJavaScript() + ")";
            case "+":
            case "-":
            case "%":
            case "*":
            case "/":
                return "(" + left.toJavaScript() + " " + symbol + " " + right.toJavaScript() + ")";
            default:
                throw new RuntimeException("Unbekannter Operator für JS: " + symbol);
        }
    }

}
// Expr1
record IsBool(boolean value) implements Expr1 {
    public String toText() {
        return " " + Boolean.valueOf(value) + " ";
    }

    public Expr1 eval(Map<String, Expr1> env) {
        return this;
    }

    public String toJavaScript() {
        return Boolean.toString(value);
    }
}

record Ternary(Expr1 bedingung, Expr1 thene, Expr1 elses) implements Expr1 {
    public String toText() {
        return bedingung.toText() + " ? " + thene.toText() + " : " + elses.toText();
    }

    public Expr1 eval(Map<String, Expr1> env) {
        IsBool b = (IsBool) bedingung.eval(env);
        return b.value() ? thene.eval(env) : elses.eval(env);
    }

    public String toJavaScript() {
        return "(" + bedingung.toJavaScript() + " ? " + thene.toJavaScript() + " : " + elses.toJavaScript() + ")";
    }
}

record BoolOp(Bool ope, Expr1 left, Expr1 right) implements Expr1 {
    public String toText() {
        if (ope == Bool.NOT)
            return "(" + ope.toText() + " " + left.toText() + ")";
        else
            return "(" + left.toText() + " " + ope.toText() + " " + right.toText() + ")";
    }

    public Expr1 eval(Map<String, Expr1> env) {
        IsBool links = (IsBool) left.eval(env);
        boolean ergebnis;
    
        if (ope == Bool.NOT) {
            ergebnis = ope.bool(links.value(), false); // right ist hier irrelevant
        } else {
            IsBool recht = (IsBool) right.eval(env); 
            ergebnis = ope.bool(links.value(), recht.value());
        }
    
        return new IsBool(ergebnis);
    }
    

    public String toJavaScript() {
        switch (ope) {
            case Bool.AND -> {
                return "(" + left.toJavaScript() + " && " + right.toJavaScript() + ")";
            }
            case Bool.OR -> {
                return "(" + left.toJavaScript() + " || " + right.toJavaScript() + ")";
            }
            case Bool.NOT -> {
                return "(!" + left.toJavaScript() + ")";
            }
            default -> throw new RuntimeException("Unbekannter BoolOp: " + ope);
        }
    }

}

record VergleichOP(Vergleich ver, Expr1 left, Expr1 right) implements Expr1 {
    public String toText() {
        return "(" + left.toText() + " " + ver.toText() + " " + right.toText() + ")";
    }

    public Expr1 eval(Map<String, Expr1> env) {
        double linkewert = ((Num1) left.eval(env)).number();
        double rechtewert = ((Num1) right.eval(env)).number();
        boolean result = ver.compare(linkewert, rechtewert);
        return new IsBool(result);
    }

    public String toJavaScript() {
        return "(" + left.toJavaScript() + " " + ver.toJavaScript() + " " + right.toJavaScript() + ")";
    }

}
