import java.util.List;
import java.util.Stack;

class ParserRPN {
    private final List<Token.Tokenizer> tokens;

    ParserRPN(List<Token.Tokenizer> tokens) {
        this.tokens = tokens;
    }
// ParserRPN
    public Expr1 parse(List<Token.Tokenizer> tokens) {
        Stack<Expr1> stack = new Stack<>();

        for (Token.Tokenizer token : tokens) {
            if (token instanceof Token.NumTokenizer num) {
                stack.push(new Num1(num.number()));
            } else if (token instanceof Token.VariablenTokenizer var) {
                stack.push(new Var1(var.mathe()));
            } else if (token instanceof Token.VergleichTokenizer vToken) {
                String v = vToken.vergleich();
                if (v.equals("¡") && stack.size() >= 1) {
                    Expr1 a = stack.pop();
                    stack.push(new BoolOp(Bool.NOT, a, null));
                }
                if ((v.equals("&&") || v.equals("||")) && stack.size() >= 2) {
                    Expr1 b = stack.pop();
                    Expr1 a = stack.pop();
                    Bool bool = v.equals("&&") ? Bool.AND : Bool.OR;
                    stack.push(new BoolOp(bool, a, b));
                }
                if (stack.size() < 2)
                    throw new IllegalArgumentException("Zu wenige Operanden für Vergleich: " + v);
                Expr1 b = stack.pop();
                Expr1 a = stack.pop();
                Vergleich cmp = switch (v) {
                    case "==" -> Vergleich.EGAL;
                    case "!=" -> Vergleich.NOTEGAL;
                    case ">" -> Vergleich.PLUSGRAND;
                    case "<" -> Vergleich.PLUSPETIT;
                    case ">=" -> Vergleich.PLUSGRANDEGAL;
                    case "<=" -> Vergleich.PLUSPETITEGAL;
                    default -> throw new IllegalArgumentException("Unbekannter Vergleich: " + v);
                };
                stack.push(new VergleichOP(cmp, a, b));
            } else if (token instanceof Token.OperatorTokenizer opToken) {
                String op = opToken.opera();
                Op operator = switch (op) {
                    case "+" -> Op.ADD;
                    case "-" -> Op.SUB;
                    case "*" -> Op.MUL;
                    case "/" -> Op.DIV;
                    case "%" -> Op.MOD;
                    case "^" -> Op.POW;
                    case "~" -> Op.NEG;
                    case "√" -> Op.RAC;
                    case "!" -> Op.FAC;
                    default -> throw new IllegalArgumentException("Unbekannter Operator: " + op);
                };

                if (operator == Op.NEG || operator == Op.RAC || operator == Op.FAC) {
                    if (stack.isEmpty())
                        throw new IllegalArgumentException("Zu wenige Operanden für " + op);
                    Expr1 a = stack.pop();
                    stack.push(new UnOp(operator, a));
                } else {
                    if (stack.size() < 2)
                        throw new IllegalArgumentException("Zu wenige Operanden für " + op);
                    Expr1 b = stack.pop();
                    Expr1 a = stack.pop();
                    stack.push(new BiOp(operator, a, b));
                }
            } else if (token instanceof Token.QuestionTokenizer) {
                if (stack.size() < 3)
                    throw new RuntimeException("Ternär benötigt 3 Argumente");
                Expr1 elseExpr = stack.pop();
                Expr1 thenExpr = stack.pop();
                Expr1 cond = stack.pop();
                stack.push(new Ternary(cond, thenExpr, elseExpr));
            } else if (token instanceof Token.FunktionTokenizer funcToken) {
                String func = funcToken.func();
                if (stack.isEmpty())
                    throw new IllegalArgumentException("Fehlender Operand für Funktion: " + func);
                Expr1 arg = stack.pop();
                Op function = switch (func) {
                    case "sin" -> Op.SIN;
                    case "cos" -> Op.COS;
                    case "tan" -> Op.TAN;
                    case "log" -> Op.LOG;
                    case "exp" -> Op.EXP;
                    case "ln" -> Op.LN;
                    default -> throw new IllegalArgumentException("Unbekannte Funktion: " + func);
                };
                stack.push(new UnOp(function, arg));
            } else {
                throw new IllegalArgumentException("Unerwartetes Token: " + token);
            }
        }

        if (stack.size() != 1)
            throw new IllegalArgumentException("Ungültiger RPN-Ausdruck: Stack enthält " + stack.size() + " Elemente");

        return stack.pop();
    }// ParserRPN
}
