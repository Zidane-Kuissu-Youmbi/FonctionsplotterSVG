import java.util.*;
import java.util.Map;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

public class Konvertisseur {

    public String toRPN(Expr1 expr) {
        return switch (expr) {
            case Num1 n -> String.valueOf(n.number());
            case Var1 v -> v.name();
            case IsBool bol -> String.valueOf(bol.value());
            case UnOp un -> "[ " + toRPN(un.mitte()) + " " + un.op().symbol() + " ]";
            case BiOp bi -> {
                String left = toRPN(bi.left());
                String right = toRPN(bi.right());
                String op = bi.op().symbol();
                yield "[ " + left + " " + right + " " + op + " ]";
            }
            case BoolOp b -> {
                if (b.ope() == Bool.NOT)
                    yield "[ " + toRPN(b.left()) + " ¡ ]";
                else
                    yield "[ " + toRPN(b.left()) + " " + toRPN(b.right()) + " " + b.ope().toText() + " ]";
            }
            case Ternary t -> toRPN(t.bedingung()) + " " + toRPN(t.thene()) + " " + toRPN(t.elses()) + " ?";
            case VergleichOP v -> "[ " + toRPN(v.left()) + " " + toRPN(v.right()) + " " + v.ver().toText() + " ]";

        };

    }
// Konvertisseur
    public String toInfix(Expr1 expr) {
        return switch (expr) {
            case Num1 n -> String.valueOf(n.number());
            case Var1 v -> v.name();
            case IsBool bol -> String.valueOf(bol.value());
            case UnOp un -> {
                String mitte = toInfix(un.mitte());
                String op = un.op().symbol();
                yield "(" + op + "(" + mitte + "))";
            }
            case BiOp bi -> {
                String left = toInfix(bi.left());
                String right = toInfix(bi.right());
                String op = bi.op().symbol();
                yield "((" + left + ") " + op + " (" + right + "))";

            }
            case BoolOp b -> {
                if (b.ope() == Bool.NOT)
                    yield "(!" + toInfix(b.left()) + ")";
                else
                    yield "((" + toInfix(b.left()) + ") " + b.ope().toText() + " (" + toInfix(b.right()) + "))";
            }
            case VergleichOP v -> "((" + toInfix(v.left()) + ") " + v.ver().toText() + " (" + toInfix(v.right()) + "))";
            case Ternary t ->
                "((" + toInfix(t.bedingung()) + ") ? (" + toInfix(t.thene()) + ") : (" + toInfix(t.elses()) + "))";
        };
    }
    // Konvertisseur
}
