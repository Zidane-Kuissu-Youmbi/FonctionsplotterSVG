import java.util.*;
import java.util.*;
import java.util.Map;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

// ProductionDot
class ProductionDot {
    private static int conteur;

    public String toDot(Expr1 expr) {
        conteur = 0;
        StringBuilder bild = new StringBuilder("digraph DIG {\n");
        toDotHelfer(expr, bild, -1);
        bild.append("}\n");
        return bild.toString();
    }

    private static int toDotHelfer(Expr1 expr1, StringBuilder bild, int parentId) {
        int identity = conteur++;

        String label = expr1 instanceof Num1 n ? String.valueOf(n.number())
                : expr1 instanceof Var1 v ? v.name()
                        : expr1 instanceof BoolOp bp ? bp.ope().toText()
                                : expr1 instanceof VergleichOP v ? v.ver().toText()
                                        : expr1 instanceof BiOp b ? b.op().symbol()
                                                : expr1 instanceof IsBool b ? String.valueOf(b.value())
                                                        : expr1 instanceof UnOp u ? u.op().symbol()
                                                                : expr1 instanceof Ternary ? "?" : "?";

        String color = expr1 instanceof Num1 ? "lightgreen"
                : expr1 instanceof Var1 ? "lightblue"
                        : expr1 instanceof BiOp ? "lightcoral"
                                : expr1 instanceof UnOp ? "orange"
                                        : expr1 instanceof Ternary ? "violet"
                                                : expr1 instanceof BoolOp ? "cyan"
                                                        : expr1 instanceof VergleichOP ? "burlywood"
                                                                : "white"; // fallback

        bild.append("  n").append(identity)
                .append(" [label=\"").append(label)
                .append("\", style=filled, fillcolor=\"").append(color)
                .append("\"];\n");

        if (parentId != -1) {
            bild.append("  n").append(parentId).append(" -> n").append(identity).append(";\n");
        }

        switch (expr1) {
            case BiOp b -> {
                toDotHelfer(b.left(), bild, identity);
                toDotHelfer(b.right(), bild, identity);
            }
            case UnOp u -> toDotHelfer(u.mitte(), bild, identity);
            case Ternary t -> {
                toDotHelfer(t.bedingung(), bild, identity);
                toDotHelfer(t.thene(), bild, identity);
                toDotHelfer(t.elses(), bild, identity);
            }
            case BoolOp b -> {
                toDotHelfer(b.left(), bild, identity);
                if (b.ope() != Bool.NOT)
                    toDotHelfer(b.right(), bild, identity);
            }
            case VergleichOP v -> {
                toDotHelfer(v.left(), bild, identity);
                toDotHelfer(v.right(), bild, identity);
            }
            default -> {
                // Num1, Var1, IsBool → terminal nodes, nothing to add
            }
        }

        return identity;
    }
}
// ProductionDot
