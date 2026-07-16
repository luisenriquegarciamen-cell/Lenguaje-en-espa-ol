import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Panel Swing encargado de renderizar el autómata.
 * Cumple con la arquitectura delegada: simplemente llama al método dibujado(...)
 * de cada Transicion y Estado.
 */
public class PanelAutomata extends JPanel {

    private Automata automata;

    public PanelAutomata() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(650, 350));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Representación Gráfica del Autómata",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)
        ));
    }

    /**
     * Asigna un autómata y fuerza el redibujado del panel.
     */
    public void setAutomata(Automata automata) {
        this.automata = automata;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Activar suavizado de bordes (Antialiasing)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Si no hay autómata asignado, mostrar mensaje indicativo
        if (automata == null) {
            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            FontMetrics fm = g2d.getFontMetrics();
            String msg = "Ingresa pseudocódigo y selecciona una línea para ver el grafo.";
            int tx = (getWidth() - fm.stringWidth(msg)) / 2;
            int ty = getHeight() / 2;
            g2d.drawString(msg, tx, ty);
            return;
        }

        List<Estado> estados = automata.getEstados();
        List<Transicion> transiciones = automata.getTransiciones();
        List<Estado> caminoEstados = automata.getCaminoEstados();
        List<Transicion> caminoTransiciones = automata.getCaminoTransiciones();
        boolean esValida = automata.isEsValida();

        // 1. Dibujar Transiciones normales definidas en el grafo
        for (Transicion t : transiciones) {
            boolean visitada = caminoTransiciones.contains(t);
            t.dibujado(g2d, visitada, esValida);
        }

        // 2. Dibujar Transiciones implícitas hacia el estado de error (si existieron)
        for (Transicion t : caminoTransiciones) {
            if (!transiciones.contains(t)) {
                t.dibujado(g2d, true, false);
            }
        }

        // 3. Dibujar Estados (nodos) por encima de las líneas
        for (Estado e : estados) {
            boolean visitado = caminoEstados.contains(e);
            e.dibujado(g2d, visitado, esValida);
        }
    }

    // -----------------------------------------------------------------
    // MÉTODO MAIN DE PRUEBA RÁPIDA (Para verificar que compila y ejecuta)
    // -----------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Prueba de Grafo del Autómata");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PanelAutomata panel = new PanelAutomata();

            // Ejemplo de prueba: probamos analizar la línea "Numero x = 42"
            Automata aut = new Automata("Numero x = 42");
            panel.setAutomata(aut);

            ventana.add(panel);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}