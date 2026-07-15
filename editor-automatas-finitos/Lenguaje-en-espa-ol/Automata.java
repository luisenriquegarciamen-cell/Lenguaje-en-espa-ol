import java.util.List;
import java.util.Set;
/**
 * Controla el flujo de estados y transiciones para resolver el grafo del autómata.
 */
public class Automata {
    private final List<Transicion> transiciones;
    private final Estado estadoInicial;
    private final Set<Estado> estadosAceptacion;

    public Automata(List<Transicion> transiciones, Estado estadoInicial, Set<Estado> estadosAceptacion) {
        this.transiciones = transiciones;
        this.estadoInicial = estadoInicial;
        this.estadosAceptacion = estadosAceptacion;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public boolean esEstadoDeAceptacion(Estado estado) {
        return estadosAceptacion.contains(estado);
    }

    /**
     * Sobrecarga para mantener compatibilidad con búsquedas usando ID entero.
     */
    public boolean esEstadoDeAceptacion(int estadoId) {
        return estadosAceptacion.stream().anyMatch(e -> e.getId() == estadoId);
    }

    /**
     * Busca el siguiente estado basándose en un ID numérico de estado origen.
     * Devuelve el ID del nuevo estado, o -1 si no hay camino.
     */
    public int siguienteEstado(int estadoActualId, char c) {
        Transicion comodinSeleccionado = null;

        for (Transicion t : transiciones) {
            if (t.getOrigen().getId() == estadoActualId) {
                // Coincidencia exacta (ej: '"' o letras de "Cadena")
                if (t.getCaracter() == c) {
                    return t.getDestino().getId();
                }
                // Si es un comodín '.', lo guardamos como alternativa secundaria
                if (t.getCaracter() == '.') {
                    comodinSeleccionado = t;
                }
            }
        }

        if (comodinSeleccionado != null) {
            return comodinSeleccionado.getDestino().getId();
        }

        return -1; // Se rompió el flujo del autómata
    }

    /**
     * Evalúa si una cadena es completamente válida recorriendo todos los estados.
     */
    public boolean aceptaCompleto(String texto) {
        int estadoActualId = estadoInicial.getId();

        for (int i = 0; i < texto.length(); i++) {
            estadoActualId = siguienteEstado(estadoActualId, texto.charAt(i));
            if (estadoActualId == -1) {
                return false;
            }
        }

        return esEstadoDeAceptacion(estadoActualId);
    }
}