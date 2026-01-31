import java.util.*;
import java.util.Map;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

enum Op {
    ADD("+") {
        public double apply(double a, double b) { // Addition Operator mit ihre eigene instanzMethode, dient dazu wie
                                                  // die Operation ausgeführt wird.
            return a + b;
        }

    },
    SUB("-") {
        public double apply(double a, double b) { // genaue wie bei addition und für die andere
            return a - b;
        }
    },
    MUL("*") {
        public double apply(double a, double b) {
            return a * b;
        }
    },
    DIV("/") {
        public double apply(double a, double b) {
            if (b == 0) // b darf nicht gleich 0 sein
                throw new ArithmeticException("Division bei zero nicht definiert");// assertion werfen wenn b == 0.
            return a / b;
        }
    },
    NEG("~") {
        public double apply(double a, double b) { // die Negation Operator ignoriet die b und negiert nur die a
            return -a;
        }
    },
    RAC("√") {
        public double apply(double a, double b) {
            if (a < 0)
                throw new ArithmeticException(" Die QuadratWurzel ist nicht für negative Zahlen definiert");
            return Math.sqrt(a);
        }
    },
    POW("^") {
        public double apply(double a, double b) {
            return Math.pow(a, b);
        }
    },
    LOG("log") {
        public double apply(double a, double b) {
            if (a <= 0)
                throw new ArithmeticException("Logarithmus bei zero ist nicht definiert");
            return Math.log10(a);
        }
    },
    LN("ln") {
        public double apply(double a, double b) {
            if (a <= 0)
                throw new ArithmeticException("Logarithmus bei zero ist nicht definiert");
            return Math.log(a);
        }
    },
    MOD("%") {
        public double apply(double a, double b) {
            if (b == 0) // b darf nicht gleich 0 sein
                throw new ArithmeticException("Modulo bei zero nicht definiert");// assertion werfen wenn b == 0.
            return a % b;
        }
    },
    SIN("sin") {
        public double apply(double a, double b) {
            return Math.sin(a);
        }
    },
    COS("cos") {
        public double apply(double a, double b) {
            return Math.cos(a);
        }
    },
    // Op
    FAC("!") {
        public double apply(double a, double b) {
            double s = 1;
            if (a < 0) {
                for (int i = 1; i <= -a; i++) {
                    s = s * i;
                }
                return -s;
            } else {
                for (int i = 1; i <= a; i++) {
                    s = s * i;
                }
                return s;
            }
        }
    },
    // Op
    TAN("tan") {
        public double apply(double a, double b) {
            return Math.tan(a);
        }
    },
    EXP("exp") {
        public double apply(double a, double b) {
            return Math.exp(a);
        }
    };

    private final String symbol;// instanz Variable

    Op(String symbol) { // kanonische Konstruktor mit String Parameter
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public abstract double apply(double a, double b); // Abstrakte Methode die jeder Operator implementiert.

}
