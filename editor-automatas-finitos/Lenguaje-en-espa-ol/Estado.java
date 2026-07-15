import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
/**
 * Representa un estado (nodo) tanto para la lógica del autómata 
 * como para su representación visual en el editor gráfico.
 */
public class Estado {
    // 1. Atributos para la Lógica del Autómata
    private final int id;
    private boolean esInicial;
    private boolean esFinal; // "esAceptacion" es equivalente a "esFinal"

    // 2. Atributos para la Representación Gráfica (Editor)
    public String nombre;
    private int x, y;
    private int radio = 25;

    // 3. Constructor Combinado
    public Estado(int id, String nombre, int x, int y, boolean esInicial, boolean esFinal) {
        this.id = id;
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.esInicial = esInicial;
        this.esFinal = esFinal;
    }
    // 4. Método de Dibujo con Graphics2D
    public void dibujado(Graphics2D g2d) {
        // Grosor del pincel
        g2d.setStroke(new BasicStroke(2));
        // Determinar color de relleno (ejemplo: amarillo suave, borde negro)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - radio, y - radio, radio * 2, radio * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - radio, y - radio, radio * 2, radio * 2);
        // Si es estado Final (Aceptación), dibujamos un círculo doble (borde interno)
        if (esFinal) {
            int radioInterno = radio - 5;
            g2d.drawOval(x - radioInterno, y - radioInterno, radioInterno * 2, radioInterno * 2);
        }
        // Si es estado Inicial, dibujamos una pequeña flecha apuntando al círculo
        if (esInicial) {
            int[] xPoints = {x - radio - 20, x - radio, x - radio - 20};
            int[] yPoints = {y - 10, y, y + 10};
            g2d.drawPolyline(xPoints, yPoints, 3);
        }
        // Dibujar el texto del nombre en el centro del círculo
        g2d.drawString(nombre, x - 8, y + 5);
    }
    public int getId() {
        return id;
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getX() { 
        return x; 
    }

    public void setX(int x) { // Corregido: Ahora sí recibe el parámetro 'x'
        this.x = x;
    }

    public int getY() { 
        return y; 
    }

    public void setY(int y) { // Corregido: Ahora sí recibe el parámetro 'y'
        this.y = y;
    }
    public boolean getEsInicial() { 
        return esInicial; 
    }
    public void setEsInicial(boolean esInicial) { 
        this.esInicial = esInicial; 
    }
    public boolean getEsFinal() { 
        return esFinal; 
    }
    public void setEsFinal(boolean esFinal) { 
        this.esFinal = esFinal; 
    }
    // Métodos para poder analizar Cadena (esAceptacion es lo mismo que esFinal)
    public boolean esAceptacion() {
        return esFinal;
    }
    // 6. Métodos de comparación para la Estructura de Datos del Autómata
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado estado = (Estado) obj;
        return id == estado.id;
    }
    @Override
    public int hashCode() {
        return id;
    }
}