import lvp.Clerk;
import lvp.skills.Text;
import lvp.skills.Interaction;
import lvp.views.Dot;
import lvp.views.Turtle;
import java.util.*;
import java.util.logging.Logger;
import java.beans.Expression;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;

void main() {

    Clerk.clear();

    Clerk.markdown(Text.fillOut(
            """
                    # Meine FunktionsPlotterSVG 📈
                    ## Enum Bool
                    Die klasse Bool enthält nur die **logischeOperatoren**:
                    ***NOT(¡), AND(&&), OR(||)***. Dies werden im Laufe des Programms benutzen um booleaschen Werten für die Ternäre Operator zu erhalten
                    ```java
                    ${0} 
                    ```
                    ### Hinweise
                    > In den gesamte Code und Ausführen wird **"!"** als **Fakültät** bezeichnet. Für die Logische **NOT** wird **"¡"** benutzen. Die **Negation** von Ausdrücke wird durch **"~"** bezeichnet.
                    **Beispiel** **~35 + (~20) = ~55** aber wird mit dem Parser und Expr1 als **(-55)** ubernehmen.

                    ## Enum Vergleich
                    Die Klasse Vergleich dient als Unterstützung für die **vergleichen Operatoren**. Unteranderen haben wir die 5 wichtigen in Vergleich zusammengefasst:
                    **Größergleich( >= ), Kleinergleich( <= ), Größer( > ), kleiner( < ), Gleich( == ) und NichtGleich( != )**. Da den gesamten Projekt handelt sich über die mathematische
                    Funktionen und deren Plotter, werden die vergleichsOperator benutzen um Ausdrücke zu vergleichen.
                    ```java
                    ${1}
                    ```
                    ## Enum Op
                    Jetzt in der Klasse Op wird fast alle **mathematische Operatoren** wie ***Add( + )***, ***Sub( - )***, ***Mul( * )***, ***Div( / )***, ***Mod( % )***, **trigonometrische Funktionen**:  ***sin, cos, tan*** und **Andere Funktionen** wie
                    ***Exponentiel( exp )***, ***Natürliche Logarithmus( ln )***, ***Logarithmus( log )***, ***Würzel( √ )***, ***Potenz( ^ )***, ***Fakultät( ! )*** deklariert.
                    Im Klasse Op apply(double, double) ist eine abstrakte Methode , das heißt jeder Intanzen muss dies implementieren.
                    #### eine Beispiel Code ist für die Fakultät eingezeigt. 
                    ```java
                    ${2}
                    ```
                    ## Interface Expr1.
                    Danach würde eine Umgebung erstellt, wo alle Berechnung durgeführt werden können. Die sogenannte **Expr1**.
                    Es ist ein Sealed interface, die **8** unteren Klassen Übernimmt. Alle diese Klassen müssen die drei Methoden von Expr1 implementieren:

                    ➜ Die ***toText()- Methode*** dient als Darstellung von Ausdrucken.

                    ➜ Die ***toJavaScript()-Methode*** wird für die Plotter verwendet.

                    ➜ Die ***eval(Map<String, Expr1>)-Methode*** rechnet alle gegeben Ausdrücken.

                    Die Besonderheit in der Berechnung ist die Möglichkeit Variablen wie ***"a"*** in einer Map zu speichern und Später eine Werte geben die in Berechnung verwendet wird.
                    #### Num1 und Var1:
                    Diese klasse unterstützen Variablen von Typ double für Num1 und String für Var1(unsere Parameter in Funktion Ausdrück)
                    #### UnOp:
                    Diese Klasse nimmt alle UnäreOperatoren wie _neg(~), ln, sin, !_...
                    #### BiOp:
                    Und diese nimmt alle Binäre Operatoren wie _add(+), mod(%), pow(^)_...
                    #### IsBool
                    Es die Klasse von Wahrheitwert _(true, false)_
                    #### Ternary:
                    Das ist die Umgebung für ternäreOperatoren. Es wird durch IsBool Unterstützen um die Bedingungen zu prüfen.
                    #### BoolOp:
                    Die Klasse BoolOp nimmt auch eine BinäreOperator aber in diesem Fall sind die Operand BooleascheWert(IsBool).
                    #### VergleichOp:
                    Noch eine BinäreOperator, die zwar Ausdrücke von Typ double nimmt , aber gibt auch eine "IsBool" zurückt.

                    **Beipielen** für Expr1 Schreibweise,  die berechnen Wurde:

                    | Funktion Eingabe | Expr1 Schreibweise|
                    |------------------|-------------------|
                    |**(x)>=0.0 ? sin(x+1.0) : cos(x)** | **Ternary[bedingung=VergleichOP[ver=PLUSGRANDEGAL, left=Var1[name=x], right=Num1[number=0.0]], thene=UnOp[op=SIN, mitte=BiOp[op=ADD, left=Var1[name=x], right=Num1[number=1.0]]], elses=UnOp[op=COS, mitte=Var1[name=x]]]**|
                    |**ln(x^2.0 + a) % 2.0 * x + 1.0** |**BiOp[op=ADD, left=BiOp[op=MUL, left=BiOp[op=MOD, left=UnOp[op=LN, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Var1[name=a]]], right=Num1[number=2.0]], right=Var1[name=x]], right=Num1[number=1.0]]**
                    |**!(2.0 * x)**| **UnOp[op=FAC, mitte=BiOp[op=MUL, left=Num1[number=2.0], right=Var1[name=x]]]**
                    |**log(x^2.0 + 1.0)** |**UnOp[op=LOG, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Num1[number=1.0]]]**

                    #### Beispiel Code für die Binäre Operatoren. 
                    ```java
                    ${3}
                    ```
                    ## Class Token
                    Die klasse **Token** enthält eine Untere **sealed interface Tokenizer** die durch **" 8 "**  untere Klasse implementiert wurde um alle oben genannten Ausdrücken zu token.
                    In Token gibt nur eine einzelne Methode : ***"token(String)"***, die eine *List von Tokenizer* zurückt gibt

                    Als beispiele haben wir:
                    | Funktionen | Liste von Tokenizer|
                    |------------|----------------|
                    **" (x) >= 0.0 ? sin(x+1.0): cos(x) "** | **[(, x, ), >=, 0.0, ?, sin, (, x, +, 1.0, ), :, cos, (, x, )]**
                    **" ln(x^2.0 + a) % 2.0 * x + 1.0 "**| **[ln, (, x, ^, 2.0, +, a, ), %, 2.0, `*`, (, x, ), +, 1.0]**
                    **" !(2.0 * x) "**| **[!, (, 2.0, `*`, x, )]**
                    **" log(x^2.0 + 1.0) "** | **[log, (, x, ^, 2.0, +, 1.0, )]**

                    ## Class ParserInfix
                    Um Instanzen von diese Klasse zu erzeugen, brauchen wir erstmal die Liste, die der Token zurück gegeben hat. Und dann
                    wird die Methode ***parse()***, die keine Parameter nimmt, benutzen. Diese Methode  gibt eine Expr1 zurückt, die mit der Expr1 Klasse berechnen
                    wurden.

                    Nehmen wir jetzt die gleichen Funktionen wie oben damit wir gut verfolgen was in jeden Schritt passiert:
                    | Tokenizer Liste | ParserInfix|
                    |-----------------|------------|
                    |**[(, x, ), >=, 0.0, ?, sin, (, x, +, 1.0, ), :, cos, (, x, )]** | **Ternary[bedingung=VergleichOP[ver=PLUSGRANDEGAL, left=Var1[name=x], right=Num1[number=0.0]], thene=UnOp[op=SIN, mitte=BiOp[op=ADD, left=Var1[name=x], right=Num1[number=1.0]]], elses=UnOp[op=COS, mitte=Var1[name=x]]]**
                    |**[ln, (, x, ^, 2.0, +, a, ), %, 2.0, `*`, (, x, ), +, 1.0]** | **BiOp[op=ADD, left=BiOp[op=MUL, left=BiOp[op=MOD, left=UnOp[op=LN, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Var1[name=a]]], right=Num1[number=2.0]], right=Var1[name=x]], right=Num1[number=1.0]]**
                    |**[!, (, 2.0, `*`, x, )]** | **UnOp[op=FAC, mitte=BiOp[op=MUL, left=Num1[number=2.0], right=Var1[name=x]]]**
                    | **[log, (, x, ^, 2.0, +, 1.0, )]** | **UnOp[op=LOG, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Num1[number=1.0]]]**

                    ```java
                    ${4}
                    ```
                    ### Class ParserRPN
                    Da es nicht nur die ***Infix*** Schreibweise um mathematiche Ausdrücken zu schreiben, aber  auch ***RPN*** schreibweise, war es Sinnvoll
                    eine andere Parser Klasse zu Schreiben. Die untenstehende Code basiert sich auf die ***RPN Parser***.
                    Wie Parser Infix brauchen wir auch eine Liste von *Token.Tokenizer* um Intanzen zu erzeugen. Allerdings braucht die **parse()**-Methode von RPN
                    auch die Liste von Token.Tokenizer als Parameter.

                    wie sieht die RPN schreibweise aus? lassen Sie sich mit den folgenden Beispielen , die aus vorherige Liste basiert, erklären
                    | Tokenizer Liste | ParserRPN|
                    |-----------------|------------|
                    |**[(, x, ), >=, 0.0, ?, sin, (, x, +, 1.0, ), :, cos, (, x, )]** | **[ x 0.0  >=  ] [ [ x 1.0 + ] sin ] [ x cos ] ?**
                    |**[ln, (, x, ^, 2.0, +, a, ), %, 2.0, `*`, (, x, ), +, 1.0]** | **[ [ [ [ [ [ x 2.0 ^ ] a + ] ln ] 2.0 % ] x * ] 1.0 + ]** 
                    | **[!, (, 2.0, `*`, x, )]** | **[ [ 2.0 x * ] ! ]**
                    | **[log, (, x, ^, 2.0, +, 1.0, )]** | **[ [ [ x 2.0 ^ ] 1.0 + ] log ]**

                    ## Class Konvertisseur
                    Heutzutage ist die  *RPN* Schreibweise sehr Selten. Das heißt nur wenige Leute wissen wie man das liest. Deshalb war es von
                    Vorteile eine Übersetzung  Klasse(**Konvertisseur**) zu erstellen, damit jeder von Beide Schreibweise(**Infix** und **RPN**) zurechtkommt.
                    Die ***toRPN(Expr1)-*** und ***toInfix(Expr1)***-Methode nehmen eine Expr1 als Parameter und in dementsprenchend Schreibweise konvertiert.
                    #### Beispiel:
                    |Funktionen| Expr1  | toInfix | toRPN |
                    |----------|--------|---------|-------|
                    |**(x) >= 0.0 ? sin(x+1.0): cos(x)**|**Ternary[bedingung=VergleichOP[ver=PLUSGRANDEGAL, left=Var1[name=x], right=Num1[number=0.0]], thene=UnOp[op=SIN, mitte=BiOp[op=ADD, left=Var1[name=x], right=Num1[number=1.0]]], elses=UnOp[op=COS, mitte=Var1[name=x]]]** |**((x  >=  0.0) ? (sin(( (x) + (1.0)))) : (cos(x)))** |**[ x 0.0  >=  ] [ [ x 1.0 + ] sin ] [ x cos ] ?**
                    |**ln(x^2.0 + a) % 2.0 * x + 1.0** |**BiOp[op=ADD, left=BiOp[op=MUL, left=BiOp[op=MOD, left=UnOp[op=LN, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Var1[name=a]]], right=Num1[number=2.0]], right=Var1[name=x]], right=Num1[number=1.0]]** | **( (( (( ((ln(( (( (x) ^ (2.0))) + (a))))) % (2.0))) * (x))) + (1.0))** | **[ [ [ [ [ [ x 2.0 ^ ] a + ] ln ] 2.0 % ] x * ] 1.0 + ]**
                    |**!(2.0 * x)**|**UnOp[op=FAC, mitte=BiOp[op=MUL, left=Num1[number=2.0], right=Var1[name=x]]]** | **(!(( (2.0) * (x))))** | **[ [ 2.0 x * ] ! ]**
                    |**log(x^2.0 + 1.0)** |**UnOp[op=LOG, mitte=BiOp[op=ADD, left=BiOp[op=POW, left=Var1[name=x], right=Num1[number=2.0]], right=Num1[number=1.0]]]** | **(log(( (( (x) ^ (2.0))) + (1.0))))** | **[ [ [ x 2.0 ^ ] 1.0 + ] log ]**

                    #### Beispiel Code für die Methode toInfix(Expr1). 
                    ```java
                    ${5}
                    ```
                    ## Class ProductionDot 
                    Damit wir eine Baum-Struktur sehen können,alle Eingegebenen Funktionen werden jetzt zu Dot konvertieren.
                    Die Klasse **ProductionDot** kümmert sich darum, und zwar durch die Methode **toDot(Expr1)**.
                    Die toDot Methode ruft sich selbst eine Hilfsmethode **toDotHelfer** um alle Fällen von Ausdrücken zu Prüfen. 
                    Diese Hilfsmethode bildet **`Label`** je nach Instanzen. Dazu weist sie auch jede Knote je nach Inztanzen eine Farbe zu.
                    #### Beispiel Rückgabe von ProductionDot von 2 Funktionen. 
                    |Funktionen | RückgabeDot|
                    |-----------|------------|
                    |**√(x^3 + 1)**   | digraph DIG {<br>n0 [label="√", style=filled, fillcolor="orange"];<br>n1 [label="+", style=filled, fillcolor="lightcoral"];<br>n0 -> n1;<br>n2 [label="^", style=filled, fillcolor="lightcoral"];<br>n1 -> n2;<br>n3 [label="x", style=filled, fillcolor="lightblue"];<br>n2 -> n3;<br>n4 [label="3.0", style=filled, fillcolor="lightgreen"];<br>n2 -> n4;<br>n5 [label="1.0", style=filled, fillcolor="lightgreen"];<br>n1 -> n5;<br>}| 
                    | **tan(x)**   | digraph DIG {<br>n0 [label="tan", style=filled, fillcolor="orange"];<br>n1 [label="x", style=filled, fillcolor="lightblue"];<br>n0 -> n1;<br>}
  


                    ##  Projekt Prüfung 🥳
                    Zuguterlässt ist jetzt ein  Beispiel um den gesamten Projekt zu Prüfen. 

                    """,
            Text.codeBlock("./Bool.java", "// Bool"),
            Text.codeBlock("Vergleich.java", "// Vergleich"),
            Text.codeBlock("Op.java", "// Op"),
            Text.codeBlock("Expr1.java", "// Expr1"),
            Text.codeBlock("ParserInfix.java", "// Parserinfix"),
            Text.codeBlock("Konvertisseur.java", "// Konvertisseur")));
    // SVG-Plot mit Expr1
    Clerk.markdown(" ### Freiwillige Änderungen der Achsen des Plotters  ");
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// min x Wert", "private final double xMin = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// max x Wert", "private final double xMax = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// min y Wert", "private final double yMin = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// max y Wert", "private final double yMax = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// longueur", "private final double width = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// largeur", "private final double height = $ ;",
            "Geben sie den Wert an"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// escale", "private final double padding = $ ;",
            "Geben sie den Wert an"));
    Clerk.markdown(" ###  Name eines Paramaters und  Wert eingeben  ");
    Clerk.write(Interaction.input("./meinedemo.java", "// Name des parameters", "String variableeingabe = \"$\" ;",
            " Variablename eingeben"));
    Clerk.write(Interaction.input("./SVGPlotterStatic.java", "// Parameterwert eingeben", "double d = $ ;",
            "Geben sie den Wert an"));
    Clerk.markdown(" ### Funktionen eingeben");
    Clerk.write(Interaction.input("./meinedemo.java", "// f(x)", "String fi = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// g(x)", "String gi = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// p(x)", "String pi = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// h(x)", "String hi = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// u(x)", "String ui = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// v(x)", "String vi = \"$\" ;",
            "Geben sie eine Funktion an"));
    Clerk.write(Interaction.input("./meinedemo.java", "// t(x)", "String ti = \"$\" ;",
            "Geben sie eine Funktion an"));
    // SVG-Plot mit Expr1
    // zz
    String[] COLORS = { "cyan", "red","orange", "green", "blue", "violet", "black" };
    String[] NOMS = { "f", "g", "p", "h", "u", "v", "t" };
    String[] dote = { "eins.dot", "zwei.dot", "drei.dot", "vier.dot", "fünf.dot", "sechs.dot", "sieben.dot" };
    String fi = "(1/(5*√2*3.14))*exp((-1/50)*x^2)" ; // f(x)
    String gi = "exp(x)" ; // g(x)
    String pi = "3*x^3 - 4*x- 1" ; // p(x)
    String hi = "(x> 0 && x<5)?x * ln(x) - x: (x^2 -1)/(x +3) "; // h(x)
    String ui = "1/(log(x^b))" ; // u(x)
    String vi = "√(x^3) - 3"; // v(x)
    String ti = "¡(x < 0)? 2 *x^2 - 4 * x +8 : ~x" ; // t(x)
    String[] expressions = { fi, gi, pi, hi, ui, vi, ti };
    List<FonctionSVG> fonctions = new ArrayList<>();
    for (int i = 0; i < expressions.length; i++) {
        Token t = new Token();
        List<Token.Tokenizer> token = t.token(expressions[i]);
        ParserInfix p = new ParserInfix(token);
        //ParserRPN prpn = new ParserRPN(token);
        ProductionDot prod = new ProductionDot();
        Expr1 expr;
        try {
            expr = p.parse();
            //expr = prpn.parse(token);
            String dotOutput = prod.toDot(expr);
            try (FileWriter fw = new FileWriter(dote[i])) {
                fw.write(dotOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            continue;
        }
        fonctions.add(new FonctionSVG(NOMS[i], COLORS[i], expr, expressions[i]));
    }
    String variableeingabe = "b" ; // Name des parameters
    String svg = new SVGPlotterStatic("", fonctions, variableeingabe).toSVG();
    try {
        Files.writeString(Path.of("./graphe.svg"), svg);
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Dotplot
    for (int i = 0; i < expressions.length; i++) {
        Dot dot = new Dot();
        String se = "";
        try {
            se = Files.readString(Path.of("./" + dote[i]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dot.draw(se);
    }
    // Dotplot
    Clerk.markdown("""
            # FonctionsPlotter 🎨

            """);
    // Fonction
    String func = "";
    try {
        func = Files.readString(Path.of("./graphe.svg"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Fonction
    // zz
    Clerk.markdown("<div>" + func + "</div>");
}

