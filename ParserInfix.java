import java.util.*;
import java.util.Map;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

class ParserInfix {
    private final List<Token.Tokenizer> tokens;
    private int index = 0;

    ParserInfix(List<Token.Tokenizer> tok) {
        this.tokens = tok;
    }
// Parserinfix
    public Expr1 parse() {
        Expr1 expr = parseExpr();
        if (index < tokens.size()) {
            throw new IllegalArgumentException("Unerwartetes Token: " + tokens.get(index));
        }
        return expr;
    }
// Parserinfix
    private Expr1 parseExpr() {
        return parseTernary();
    }

    private Expr1 parseTernary() {

        Expr1 cond = parseLogic();
        if (index < tokens.size() && tokens.get(index) instanceof Token.QuestionTokenizer) {
            index++; // überspringe '?'
            Expr1 thenExpr = parseExpr();
            if (index >= tokens.size() || !(tokens.get(index) instanceof Token.ColonTokenizer)) {
                throw new IllegalArgumentException("Ternärer Operator erwartet ':'");
            }
            index++; // überspringe ':'
            Expr1 elseExpr = parseExpr();
            return new Ternary(cond, thenExpr, elseExpr);
        }
        return cond;
    }

    private Expr1 parseLogic() {
        Expr1 expr = parseComparison();
        while (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.VergleichTokenizer ver) {
                String op = ver.vergleich();
                if (op.equals("&&")) {
                    index++;
                    expr = new BoolOp(Bool.AND, expr, parseComparison());
                } else if (op.equals("||")) {
                    index++;
                    expr = new BoolOp(Bool.OR, expr, parseComparison());
                } 
            } else {
                break;
            }
        }
        return expr;
    }

    private Expr1 parseComparison() {
        Expr1 expr = parseAddSub();
        while (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.VergleichTokenizer ver
                    && (ver.vergleich().equals("&&") || ver.vergleich().equals("||") || ver.vergleich().equals("¡"))) {
                index++;
                Bool b = ver.vergleich().equals("&&") ? Bool.AND : (ver.vergleich().equals("||") ? Bool.OR : Bool.NOT);
                expr = new BoolOp(b, expr, parseComparison());
            } else if (t instanceof Token.VergleichTokenizer v) {
                Vergleich cmp = switch (v.vergleich()) {
                    case ">" -> Vergleich.PLUSGRAND;
                    case "<" -> Vergleich.PLUSPETIT;
                    case ">=" -> Vergleich.PLUSGRANDEGAL;
                    case "<=" -> Vergleich.PLUSPETITEGAL;
                    case "==" -> Vergleich.EGAL;
                    case "!=" -> Vergleich.NOTEGAL;
                    default -> throw new IllegalArgumentException("Unbekannter Vergleichsoperator: " + v.vergleich());
                };
                index++;
                expr = new VergleichOP(cmp, expr, parseAddSub());
            } else {
                break;
            }
        }
        return expr;
    }

    private Expr1 parseAddSub() {
        Expr1 expr = parseMulDivMod();
        while (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.OperatorTokenizer op && (op.opera().equals("+") || op.opera().equals("-"))) {
                index++;
                expr = new BiOp(op.opera().equals("+") ? Op.ADD : Op.SUB, expr, parseMulDivMod());
            } else
                break;
        }
        return expr;
    }

    private Expr1 parseMulDivMod() {
        Expr1 expr = parseUnary();
        while (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.OperatorTokenizer op
                    && (op.opera().equals("%") || op.opera().equals("*") || op.opera().equals("/"))) {
                index++;
                expr = new BiOp(op.opera().equals("*") ? Op.MUL : (op.opera().equals("/") ? Op.DIV : Op.MOD), expr,
                        parseUnary());
            } else
                break;
        }
        return expr;
    }

    private Expr1 parseUnary() {
        if (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.OperatorTokenizer op && op.opera().equals("~")) {
                index++;
                return new UnOp(Op.NEG, parseUnary());
            } else if (t instanceof Token.OperatorTokenizer op && op.opera().equals("√")) {
                index++;
                return new UnOp(Op.RAC, parseUnary());
            } else if (t instanceof Token.OperatorTokenizer op && op.opera().equals("!")) {
                index++;
                return new UnOp(Op.FAC, parseUnary());
            }else if (t instanceof Token.VergleichTokenizer ver && ver.vergleich().equals("¡")) {
                index++;
                Expr1 right = parseUnary(); 
                return new BoolOp(Bool.NOT, right, null);
            }
        }
        return parsePowVer();
    }

    private Expr1 parsePowVer() {
        Expr1 base = parseAtom();
        while (index < tokens.size()) {
            Token.Tokenizer t = tokens.get(index);
            if (t instanceof Token.OperatorTokenizer op && op.opera().equals("^")) {
                index++;
                Expr1 expossant = parseUnary();
                base = new BiOp(Op.POW, base, expossant);
            } else {
                break;
            }
        }
        return base;
    }

    private Expr1 parseAtom() {
        if (index >= tokens.size())
            throw new IllegalArgumentException("Unerwartetes Ende");
        Token.Tokenizer t = tokens.get(index++);

        if (t instanceof Token.NumTokenizer num) {
            return new Num1(num.number());
        }
        if (t instanceof Token.VariablenTokenizer var) {
            return new Var1(var.mathe());
        }
        if (t instanceof Token.ParenTokenizer paren && paren.paren().equals("(")) {
            Expr1 expr = parseExpr();
            if (!(tokens.get(index++) instanceof Token.ParenTokenizer p2 && p2.paren().equals(")")))
                throw new RuntimeException("schließende Klammer ) vergessen");
            return expr;
        }

        if (t instanceof Token.FunktionTokenizer func) {
            if (!(index < tokens.size() && tokens.get(index++) instanceof Token.ParenTokenizer p
                    && p.paren().equals("("))) {
                throw new IllegalArgumentException("Funktion erwartet Argument in Klammern: " + func.func());
            }
            // öffnende Klammer
            Expr1 expr = parseExpr();
            if (!(index < tokens.size() && tokens.get(index++) instanceof Token.ParenTokenizer p2
                    && p2.paren().equals(")"))) {
                throw new IllegalArgumentException("Funktion erwartet schließende Klammer: " + func.func());
            }
            // schließende Klamer

            return switch (func.func()) {
                case "sin" -> new UnOp(Op.SIN, expr);
                case "cos" -> new UnOp(Op.COS, expr);
                case "tan" -> new UnOp(Op.TAN, expr);
                case "log" -> new UnOp(Op.LOG, expr);
                case "exp" -> new UnOp(Op.EXP, expr);
                case "ln" -> new UnOp(Op.LN, expr);
                default -> throw new IllegalArgumentException("Unbekannte Funktion: " + func.func());
            };
        }
        throw new IllegalArgumentException("Unerwartetes Token: " + t);
    }
}
// = new BoolOp(b, expr, parseComparison());
//             } else if (t instanceof Token.VergleichTokenizer ver
//                     && (ver.vergleich().equals("&&") || ver.vergleich().equals("||") || ver.vergleich().equals("¡"))) {
//                 index++;
//                 Bool b = ver.vergleich().equals("&&") ? Bool.AND : (ver.vergleich().equals("||") ? Bool.OR : Bool.NOT);
//                 expr 