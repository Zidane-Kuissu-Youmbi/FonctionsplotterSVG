import java.util.*;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.PrintWriter;

class Token {

    public sealed interface Tokenizer
            permits NumTokenizer, OperatorTokenizer, ParenTokenizer, VariablenTokenizer, FunktionTokenizer,
            VergleichTokenizer, QuestionTokenizer, ColonTokenizer {
    }

    public record NumTokenizer(double number) implements Tokenizer {
        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

    public record VariablenTokenizer(String mathe) implements Tokenizer {
        @Override
        public String toString() {
            return mathe;
        }
    }

    public record FunktionTokenizer(String func) implements Tokenizer {
        @Override
        public String toString() {
            return func;
        }
    }

    record QuestionTokenizer(String question) implements Tokenizer {
        @Override
        public String toString() {
            return question;
        }
    }

    record ColonTokenizer(String colon) implements Tokenizer {
        @Override
        public String toString() {
            return colon;
        }
    }

    public record OperatorTokenizer(String opera) implements Tokenizer {
        @Override
        public String toString() {
            return opera;
        }
    }

    public record ParenTokenizer(String paren) implements Tokenizer {
        @Override
        public String toString() {
            return paren;
        }
    }

    public record VergleichTokenizer(String vergleich) implements Tokenizer {
        @Override
        public String toString() {
            return vergleich;
        }
    }
// Token

    public List<Tokenizer> token(String in) {
        List<Tokenizer> tokens = new ArrayList<>();
        Set<String> functions = Set.of("sin", "cos", "tan", "log", "exp", "ln");

        // Découpage par espaces (utile pour RPN ou expressions espacées)
        String[] parts = in.trim().split("\\s+");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            // Traitement simple
            if (part.matches("\\d+(\\.\\d+)?")) {
                tokens.add(new NumTokenizer(Double.parseDouble(part)));
            } else if (Set.of("+", "-", "*", "/", "^", "!", "~", "%", "√").contains(part)) {
                tokens.add(new OperatorTokenizer(part));
            } else if (Set.of("==", "!=", "<=", ">=", "<", ">", "&&", "||", "¡").contains(part)) {
                tokens.add(new Token.VergleichTokenizer(part));
            } else if (functions.contains(part)) {
                tokens.add(new FunktionTokenizer(part));
            } else if (part.length() == 1 && Character.isLetter(part.charAt(0))) {
                tokens.add(new VariablenTokenizer(part));
            } else if (part.equals("?")) {
                tokens.add(new Token.QuestionTokenizer("?"));
            } else if (part.equals(":")) {
                tokens.add(new Token.ColonTokenizer(":"));
            } else if (part.equals("(") || part.equals(")")) {
                tokens.add(new ParenTokenizer(part));
            } else {
                // Bloc complexe analysé caractère par caractère
                tokens.addAll(tokeniseComplex(part));
            }
        }

        return tokens;
    }

    private List<Tokenizer> tokeniseComplex(String s) {
        List<Tokenizer> tokens = new ArrayList<>();
        Set<String> functions = Set.of("sin", "cos", "tan", "log", "exp", "ln");

        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);

            // Nombres
            if (Character.isDigit(c) || c == '.') {
                int start = i;
                while (start < s.length() && (Character.isDigit(s.charAt(start)) || s.charAt(start) == '.')) start++;
                double value = Double.parseDouble(s.substring(i, start));
                tokens.add(new NumTokenizer(value));
                i = start;
            }
            // Comparateurs longs
            else if (s.startsWith("==", i) || s.startsWith("!=", i) || s.startsWith(">=", i) || s.startsWith("<=", i)
                    || s.startsWith("&&", i) || s.startsWith("||", i)) {
                tokens.add(new Token.VergleichTokenizer(s.substring(i, i + 2)));
                i += 2;
            }
            // Comparateurs simples
            else if ("<>¡".indexOf(c) != -1) {
                tokens.add(new Token.VergleichTokenizer(String.valueOf(c)));
                i++;
            }
            // Ternär
            else if (c == '?') {
                tokens.add(new Token.QuestionTokenizer("?"));
                i++;
            } else if (c == ':') {
                tokens.add(new Token.ColonTokenizer(":"));
                i++;
            }
            // Fonctions ou variables
            else if (Character.isLetter(c)) {
                int start = i;
                while (start < s.length() && Character.isLetter(s.charAt(start))) start++;
                String name = s.substring(i, start);
                if (functions.contains(name)) {
                    tokens.add(new FunktionTokenizer(name));
                } else {
                    tokens.add(new VariablenTokenizer(name));
                }
                i = start;
            }
            // Opérateurs et parenthèses
            else {
                switch (c) {
                    case '+' -> tokens.add(new OperatorTokenizer("+"));
                    case '-' -> tokens.add(new OperatorTokenizer("-"));
                    case '*' -> tokens.add(new OperatorTokenizer("*"));
                    case '/' -> tokens.add(new OperatorTokenizer("/"));
                    case '^' -> tokens.add(new OperatorTokenizer("^"));
                    case '%' -> tokens.add(new OperatorTokenizer("%"));
                    case '~' -> tokens.add(new OperatorTokenizer("~"));
                    case '!' -> tokens.add(new OperatorTokenizer("!"));
                    case '√' -> tokens.add(new OperatorTokenizer("√"));
                    case '(' -> tokens.add(new ParenTokenizer("("));
                    case ')' -> tokens.add(new ParenTokenizer(")"));
                    default -> throw new RuntimeException("Symbole non reconnu : '" + c + "'");
                }
                i++;
            }
        }

        return tokens;
    }


    // Token
}

