
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnalizadorCadena {

    private final Automata grafoLiteral;
    private final Automata grafoDeclaracion;

    private static final int LITERAL_INICIO = 0;
    private static final int LITERAL_DENTRO = 1;
    private static final int LITERAL_ACEPTACION = 2;

    public AnalizadorCadena() {
        this.grafoLiteral = construirGrafoLiteral();
        this.grafoDeclaracion = construirGrafoDeclaracion();
    }

    private Automa construirGrafoLiteral() {
        List<Transicion> transiciones = new ArrayList<>();
        transiciones.add(new Transicion(LITERAL_INICIO, LITERAL_DENTRO, '"'));
        transiciones.add(new Transicion(LITERAL_DENTRO, LITERAL_ACEPTACION, '"')); // cierre (prioridad)
        transiciones.add(new Transicion(LITERAL_DENTRO, LITERAL_DENTRO, '.'));     // cualquier otro carácter
        return new Automata(transiciones, LITERAL_INICIO, Set.of(LITERAL_ACEPTACION));
    }

    private Automata construirGrafoDeclaracion() {
        // Estados 0..6 = las 6 letras de la palabra "Cadena"
        String palabraClave = "Cadena";
        List<Transicion> transiciones = new ArrayList<>();

        for (int i = 0; i < palabraClave.length(); i++) {
            transiciones.add(new Transicion(i, i + 1, palabraClave.charAt(i)));
        }
        int estadoTrasPalabraClave = palabraClave.length(); // = 6
        int estadoEspacio = estadoTrasPalabraClave + 1;      // 7
        int estadoAceptacion = estadoTrasPalabraClave + 2;   // 8

        transiciones.add(new Transicion(estadoTrasPalabraClave, estadoEspacio, ' '));
        transiciones.add(new Transicion(estadoEspacio, estadoAceptacion, '.'));       // primer carácter del nombre
        transiciones.add(new Transicion(estadoAceptacion, estadoAceptacion, '.'));    // resto del nombre (bucle)

        return new Automata(transiciones, 0, Set.of(estadoAceptacion));
    }

    /**
     * Recorre el grafo literal desde una posición dada, carácter por carácter,
     * y devuelve el token reconocido (con comillas) apenas se llega al estado
     * de aceptación, o null si el grafo se rompe antes de cerrar la cadena.
     */
    public String reconocer(String linea, int posicionInicial) {
        int estadoActual = grafoLiteral.getEstadoInicial();
        StringBuilder token = new StringBuilder();

        for (int i = posicionInicial; i < linea.length(); i++) {
            char c = linea.charAt(i);
            estadoActual = grafoLiteral.siguienteEstado(estadoActual, c);

            if (estadoActual == -1) {
                return null; // el grafo se rompió: no hay transición válida
            }
            token.append(c);

            if (grafoLiteral.esEstadoDeAceptacion(estadoActual)) {
                return token.toString();
            }
        }
        return null; // se acabó la línea y nunca se llegó al estado de aceptación
    }

    /** true si el texto completo, de principio a fin, es una cadena válida entre comillas. */
    public boolean esCadenaValida(String texto) {
        String resultado = reconocer(texto, 0);
        return resultado != null && resultado.length() == texto.length();
    }

    /**
     * Valida (recorriendo el grafo de declaración) si la línea tiene la forma "Cadena nombre".
     * @return el nombre de la variable si el grafo llega a un estado de aceptación, o null si no.
     */
    public String reconocerDeclaracion(String linea) {
        String limpio = linea.trim();

        if (!grafoDeclaracion.aceptaCompleto(limpio)) {
            return null; // el grafo no llegó a un estado de aceptación -> línea mal estructurada
        }
        return limpio.substring("Cadena ".length()).trim();
    }
}