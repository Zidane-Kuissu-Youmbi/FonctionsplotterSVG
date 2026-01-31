enum Vergleich {
    PLUSGRAND,
    PLUSPETIT,
    PLUSGRANDEGAL,
    PLUSPETITEGAL,
    EGAL,
    NOTEGAL;
// Vergleich
    public boolean compare(double a, double b) {
        return switch (this) {
            case PLUSGRAND -> a > b;
            case PLUSPETIT -> a < b;
            case PLUSGRANDEGAL -> a >= b;
            case PLUSPETITEGAL -> a <= b;
            case EGAL -> a == b;
            case NOTEGAL -> a != b;
        };
    }
// Vergleich
    public String toText() {
        return switch (this) {
            case PLUSGRAND -> " > ";
            case PLUSPETIT -> " < ";
            case PLUSGRANDEGAL -> " >= ";
            case PLUSPETITEGAL -> " <= ";
            case EGAL -> " == ";
            case NOTEGAL -> " != ";
        };
    }

    public String toJavaScript() {
        return switch (this) {
            case PLUSGRAND -> ">";
            case PLUSPETIT -> "<";
            case PLUSGRANDEGAL -> ">=";
            case PLUSPETITEGAL -> "<=";
            case EGAL -> "==";
            case NOTEGAL -> "!=";
        };
    }
}
