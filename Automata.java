import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analiza una línea de pseudocódigo construyendo y recorriendo un DFA
 * distinto según la instrucción detectada (Numero / Si / SiNo / Desconocido).
 */
public class Automata {

    private final String lineaOriginal;
    private boolean esValida;
    private final List<Estado> estados;
    private final List<Transicion> transiciones;
    private final List<Estado> caminoEstados;
    private final List<Transicion> caminoTransiciones;
    private String tipoDFA; // "Numero", "Si", "SiNo", "Desconocido", "Vacia"

    public Automata(String linea) {
        this.lineaOriginal = linea.trim();
        this.estados = new ArrayList<>();
        this.transiciones = new ArrayList<>();
        this.caminoEstados = new ArrayList<>();
        this.caminoTransiciones = new ArrayList<>();
        analizar();
    }

    private void analizar() {
        List<String> tokens = tokenizar(lineaOriginal);
        if (tokens.isEmpty()) {
            Estado q0 = new Estado("q0", true, true, false, 200, 100);
            estados.add(q0);
            caminoEstados.add(q0);
            esValida = true;
            tipoDFA = "Vacia";
            return;
        }

        String primerToken = tokens.get(0);
        if (primerToken.equals("Numero")) {
            tipoDFA = "Numero";
            construirDFANumero();
            ejecutarDFA(tokens);
        } else if (primerToken.equals("Cadena")) {
            tipoDFA = "Cadena";
            construirDFACadena();
            ejecutarDFA(tokens);
        }else if (primerToken.equals("Si")) {
            tipoDFA = "Si";
            construirDFASi();
            ejecutarDFA(tokens);
        } else if (primerToken.equals("SiNo")) {
            tipoDFA = "SiNo";
            construirDFASiNo();
            ejecutarDFA(tokens);
        } else {
            tipoDFA = "Desconocido";
            construirDFADesconocido(primerToken);
            ejecutarDFA(tokens);
        }
    }

    private List<String> tokenizar(String linea) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "Numero|SiNo|Si|[a-zA-Z_][a-zA-Z0-9_]*|-?\\d+(\\.\\d+)?|<=|>=|<|>|=|\\(|\\)|\\S");
        Matcher matcher = pattern.matcher(linea);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    private String clasificarToken(String token) {
        if (token.equals("Numero")) return "Numero";
        if (token.equals("Cadena")) return "Cadena";
        if (token.equals("Si")) return "Si";
        if (token.equals("SiNo")) return "SiNo";
        if (token.equals("=")) return "=";
        if (token.equals("(")) return "(";
        if (token.equals(")")) return ")";
        if (token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">=")) return "relacion";
        if (token.startsWith("\"") && token.endsWith("\"")) return "CadenaLiteral";
        if (token.matches("-?\\d+(\\.\\d+)?")) return "NumeroLiteral";
        if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return "id";
        return "desconocido";
    }

    // ---------------- CONSTRUCCIÓN DE LOS DFA ----------------
    private void construirDFACadena() {
        // Grafo que soporta tanto "Cadena x" como "Cadena x = \"hola\""
        Estado c0 = new Estado("c0", true, false, false, 50, 100);
        Estado c1 = new Estado("c1", false, false, false, 170, 100);
        Estado c2 = new Estado("c2", false, true, false, 290, 100);  // Aceptación si es solo declaración "Cadena x"
        Estado c3 = new Estado("c3", false, false, false, 410, 100);
        Estado c4 = new Estado("c4", false, true, false, 530, 100);  // Aceptación si se le asigna un string
        Estado ce = new Estado("ce", false, false, true, 290, 220);

        estados.add(c0);
        estados.add(c1);
        estados.add(c2);
        estados.add(c3);
        estados.add(c4);
        estados.add(ce);

        transiciones.add(new Transicion(c0, c1, "Cadena"));
        transiciones.add(new Transicion(c1, c2, "id"));
        transiciones.add(new Transicion(c2, c3, "="));
        transiciones.add(new Transicion(c3, c4, "CadenaLiteral"));
        transiciones.add(new Transicion(c3, c4, "id"));
    }
    private void construirDFANumero() {
        Estado q0 = new Estado("q0", true, false, false, 50, 100);
        Estado q1 = new Estado("q1", false, false, false, 170, 100);
        Estado q2 = new Estado("q2", false, false, false, 290, 100);
        Estado q3 = new Estado("q3", false, false, false, 410, 100);
        Estado q4 = new Estado("q4", false, true, false, 530, 100);
        Estado qe = new Estado("qe", false, false, true, 290, 220);

        estados.add(q0);
        estados.add(q1);
        estados.add(q2);
        estados.add(q3);
        estados.add(q4);
        estados.add(qe);

        transiciones.add(new Transicion(q0, q1, "Numero"));
        transiciones.add(new Transicion(q1, q2, "id"));
        transiciones.add(new Transicion(q2, q3, "="));
        transiciones.add(new Transicion(q3, q4, "NumeroLiteral"));
        transiciones.add(new Transicion(q3, q4, "id"));
    }

    private void construirDFASi() {
        Estado s0 = new Estado("s0", true, false, false, 50, 120);
        Estado s1 = new Estado("s1", false, false, false, 130, 120);
        Estado s2_par = new Estado("s2_p", false, false, false, 210, 60);
        Estado s3_par = new Estado("s3_p", false, false, false, 290, 60);
        Estado s4_par = new Estado("s4_p", false, false, false, 370, 60);
        Estado s5_par = new Estado("s5_p", false, false, false, 450, 60);
        Estado s6_par = new Estado("s6_p", false, true, false, 530, 120);

        Estado s2_nopar = new Estado("s2", false, false, false, 210, 180);
        Estado s3_nopar = new Estado("s3", false, false, false, 330, 180);
        Estado s4_nopar = new Estado("s4", false, true, false, 450, 180);
        Estado se = new Estado("se", false, false, true, 290, 280);

        estados.add(s0); estados.add(s1); estados.add(s2_par); estados.add(s3_par);
        estados.add(s4_par); estados.add(s5_par); estados.add(s6_par);
        estados.add(s2_nopar); estados.add(s3_nopar); estados.add(s4_nopar); estados.add(se);

        transiciones.add(new Transicion(s0, s1, "Si"));
        transiciones.add(new Transicion(s1, s2_par, "("));
        transiciones.add(new Transicion(s2_par, s3_par, "id"));
        transiciones.add(new Transicion(s2_par, s3_par, "NumeroLiteral"));
        transiciones.add(new Transicion(s3_par, s4_par, "relacion"));
        transiciones.add(new Transicion(s4_par, s5_par, "id"));
        transiciones.add(new Transicion(s4_par, s5_par, "NumeroLiteral"));
        transiciones.add(new Transicion(s5_par, s6_par, ")"));

        transiciones.add(new Transicion(s1, s2_nopar, "id"));
        transiciones.add(new Transicion(s1, s2_nopar, "NumeroLiteral"));
        transiciones.add(new Transicion(s2_nopar, s3_nopar, "relacion"));
        transiciones.add(new Transicion(s3_nopar, s4_nopar, "id"));
        transiciones.add(new Transicion(s3_nopar, s4_nopar, "NumeroLiteral"));
    }

    private void construirDFASiNo() {
        Estado e0 = new Estado("e0", true, false, false, 80, 100);
        Estado e1 = new Estado("e1", false, true, false, 280, 100);
        Estado ee = new Estado("ee", false, false, true, 180, 200);

        estados.add(e0); estados.add(e1); estados.add(ee);
        transiciones.add(new Transicion(e0, e1, "SiNo"));
    }

    private void construirDFADesconocido(String primerToken) {
        Estado d0 = new Estado("d0", true, false, false, 100, 100);
        Estado de = new Estado("de", false, false, true, 300, 100);

        estados.add(d0); estados.add(de);
        transiciones.add(new Transicion(d0, de, primerToken));
    }

    private void construirDFADesconocido(String primerToken) {
        Estado d0 = new Estado("d0", true, false, false, 100, 100);
        Estado de = new Estado("de", false, false, true, 300, 100);

        estados.add(d0);
        estados.add(de);

        transiciones.add(new Transicion(d0, de, primerToken));
    }

    // ---------------- EJECUCIÓN DEL DFA ----------------

    private void ejecutarDFA(List<String> tokens) {
        Estado estadoActual = buscarInicial();
        if (estadoActual == null) return;
        caminoEstados.add(estadoActual);

        boolean errorDetectado = false;
        for (String token : tokens) {
            String tokenClasificado = clasificarToken(token);

            if (errorDetectado) {
                Estado estadoError = buscarError();
                if (estadoError != null) {
                    caminoEstados.add(estadoError);
                }
                continue;
            }

            Transicion transicionValida = buscarTransicion(estadoActual, tokenClasificado);
            if (transicionValida != null) {
                caminoTransiciones.add(transicionValida);
                estadoActual = transicionValida.getDestino();
                caminoEstados.add(estadoActual);
            } else {
                errorDetectado = true;
                Estado estadoError = buscarError();
                if (estadoError != null) {
                    Transicion transicionError = new Transicion(estadoActual, estadoError, token + " (Error)");
                    caminoTransiciones.add(transicionError);
                    estadoActual = estadoError;
                    caminoEstados.add(estadoActual);
                }
            }
        }

        esValida = estadoActual.isEsAceptacion() && !errorDetectado;
    }

    private Estado buscarInicial() {
        for (Estado e : estados) {
            if (e.isEsInicial()) return e;
        }
        return null;
    }

    private Estado buscarError() {
        for (Estado e : estados) {
            if (e.isEsError()) return e;
        }
        return null;
    }

    private Transicion buscarTransicion(Estado origen, String etiquetaToken) {
        for (Transicion t : transiciones) {
            if (t.getOrigen().equals(origen) && t.getEtiqueta().equals(etiquetaToken)) {
                return t;
            }
        }
        return null;
    }

    // ---------------- GETTERS ----------------

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    public boolean isEsValida() {
        return esValida;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public List<Estado> getCaminoEstados() {
        return caminoEstados;
    }

    public List<Transicion> getCaminoTransiciones() {
        return caminoTransiciones;
    }

    public String getTipoDFA() {
        return tipoDFA;
    }

}