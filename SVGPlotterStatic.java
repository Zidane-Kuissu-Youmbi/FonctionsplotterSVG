import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

// SVG
public class SVGPlotterStatic {
    private final String title;
    private final List<FonctionSVG> functions;
    private final String parameter;
    private final double xMin = -20 ; // min x Wert
    private final double xMax = 20 ; // max x Wert
    private final double yMin = -20 ; // min y Wert
    private final double yMax = 20 ; // max y Wert
    private final double width = 1000 ; // longueur
    private final double height = 800 ; // largeur
    private final double padding = 0.001 ; // escale

    public SVGPlotterStatic(String title, List<FonctionSVG> functions, String parameter) {
        this.title = title;
        this.functions = functions;
        this.parameter = parameter;
    }

    public String toSVG() {
        StringBuilder sb = new StringBuilder();
        sb.append("<svg width=\"").append(width).append("\" height=\"").append(height).append("\" ")
                .append("xmlns=\"http://www.w3.org/2000/svg\">\n");

        
        sb.append("<rect width=\"100%\" height=\"100%\" fill=\"white\" />\n");

        
        drawGrid(sb);

        
        drawAxes(sb);

        
        sb.append("<text x=\"").append(width / 2)
                .append("\" y=\"30\" text-anchor=\"middle\" font-size=\"20\" fill=\"green\" font-weight=\"bold\">")
                .append(title).append("</text>\n");

    
        drawLegend(sb);
        drawFunctions(sb);
        sb.append("<script><![CDATA[\n");
        sb.append("function fact(n) {\n");
        sb.append("  if (n < 0) return -fact(-n);\n");
        sb.append("  if (n % 1 !== 0) return NaN;\n");
        sb.append("  let s = 1;\n");
        sb.append("  for (let i = 1; i <= n; i++) s *= i;\n");
        sb.append("  return s;\n");
        sb.append("}\n");
        sb.append("]]></script>\n");



        sb.append("</svg>");
        return sb.toString();
    }

    public void saveToFile(String filename) throws IOException {
        Files.writeString(Path.of(filename), toSVG());
    }

    private void drawGrid(StringBuilder sb) {
        sb.append("<g stroke=\"#ddd\" stroke-width=\"1\">\n");
        for (int i = (int) Math.ceil(xMin); i <= xMax; i++) {
            double x = scaleX(i);
            sb.append("<line x1=\"").append(x).append("\" y1=\"").append(0).append("\" x2=\"").append(x)
                    .append("\" y2=\"").append(height).append("\" />\n");
        }
        for (int i = (int) Math.ceil(yMin); i <= yMax; i++) {
            double y = scaleY(i);
            sb.append("<line x1=\"").append(0).append("\" y1=\"").append(y).append("\" x2=\"").append(width)
                    .append("\" y2=\"").append(y).append("\" />\n");
        }
        sb.append("</g>\n");
    }

    private void drawAxes(StringBuilder sb) {
        double x0 = scaleX(0);
        double y0 = scaleY(0);
        sb.append("<g stroke=\"black\" stroke-width=\"2\">\n");
        sb.append("<line x1=\"0\" y1=\"").append(y0).append("\" x2=\"").append(width).append("\" y2=\"").append(y0)
                .append("\" />\n");
        sb.append("<line x1=\"").append(x0).append("\" y1=\"0\" x2=\"").append(x0).append("\" y2=\"").append(height)
                .append("\" />\n");
        sb.append("</g>\n");

        // Axis labels
        sb.append("<g font-size=\"10\" fill=\"black\">\n");
        for (int i = (int) Math.ceil(xMin); i <= xMax; i++) {
            double x = scaleX(i);
            sb.append("<text x=\"").append(x).append("\" y=\"").append(scaleY(0) + 15)
                    .append("\" text-anchor=\"middle\">")
                    .append(i).append("</text>\n");
        }
        for (int i = (int) Math.ceil(yMin); i <= yMax; i++) {
            double y = scaleY(i);
            if (i != 0) {
                sb.append("<text x=\"").append(scaleX(0) + 5).append("\" y=\"").append(y + 3).append("\">")
                        .append(i).append("</text>\n");
            }
        }
        sb.append("</g>\n");
    }

    private void drawLegend(StringBuilder sb) {
        double x = 10;
        double y = 50;
        sb.append("<g font-size=\"12\">\n");
        for (int i = 0; i < functions.size(); i++) {
            FonctionSVG f = functions.get(i);
            sb.append("<rect x=\"").append(x).append("\" y=\"").append(y + i * 20)
                    .append("\" width=\"10\" height=\"10\" fill=\"")
                    .append(f.couleur()).append("\" />\n");
            sb.append("<text x=\"").append(x + 15).append("\" y=\"").append(y + 9 + i * 20).append("\" fill=\"black\">")
                    .append(f.nom() + "(x)= " + f.text()).append("</text>\n");
        }
        sb.append("</g>\n");
    }

    private void drawFunctions(StringBuilder sb) {
        sb.append("<g fill=\"none\" stroke-width=\"2\">\n");
        for (FonctionSVG f : functions) {
            sb.append("<path d=\"");
            boolean started = true;
            double lastY = 0;
            for (double x = xMin; x <= xMax; x += padding) {
                double y;
                Map<String, Expr1> env = new HashMap<>();
                env.put("x", new Num1(x));
                double d = 2 ; // Parameterwert eingeben
                if (parameter != null)
                    env.put(parameter, new Num1(d)); // valeur par défaut

                try {
                    y = ((Num1) f.fonction().eval(env)).number();
                } catch (Exception e) {
                    started = true;
                    continue;
                }
                if (!Double.isFinite(y)) {
                    started = true;
                    continue;
                }

                double sx = scaleX(x);
                double sy = scaleY(y);
                if (!started && Math.abs(y - lastY) < 2.0) {
                    sb.append(" L").append(sx).append(" ").append(sy).append(" ");
                } else {
                    sb.append(" M").append(sx).append(" ").append(sy).append(" ");
                    started = false;
                }
                lastY = y;
            }
            sb.append("\" stroke=\"").append(f.couleur()).append("\" />\n");
        }
        sb.append("</g>\n");
    }

    private double scaleX(double x) {
        return padding + (x - xMin) / (xMax - xMin) * (width - 2 * padding);
    }

    private double scaleY(double y) {
        return height - padding - (y - yMin) / (yMax - yMin) * (height - 2 * padding);
    }
}
// SVG
