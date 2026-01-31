enum Bool {
    NOT,
    AND,
    OR;
// Bool
    public boolean bool(boolean a, boolean b) {
        return switch (this) {
            case NOT -> !a;
            case AND -> a && b;
            case OR -> a || b;

        };
    }
// Bool
    public String toText() {
        return switch (this) {
            case NOT -> "¡";
            case AND -> "&&";
            case OR -> "||";
        };
    }
}