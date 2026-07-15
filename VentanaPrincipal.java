import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private JList<String> lista;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaTexto;
    private JButton btn1, btn2, btn3, btn4;
    private PanelAutomata panelAutomata;
    private List<Automata> listaAutomatas;

    public VentanaPrincipal() {
        setTitle("Lenguaje en español - Analizador de Pseudocódigo");
        // Aumentamos la altura de la ventana para acomodar el panel del grafo
        setSize(850, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        listaAutomatas = new ArrayList<>();
        modeloLista = new DefaultListModel<>();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));

        // ------------------- TÍTULO -----------------------
        JLabel titulo = new JLabel("Procesamiento de Datos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBounds(25, 15, 350, 35);
        panel.add(titulo);

        // ------------------- JLIST ------------------------
        lista = new JList<>(modeloLista);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Listener para actualizar el dibujo del autómata al seleccionar una línea
        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = lista.getSelectedIndex();
                    if (index >= 0 && index < listaAutomatas.size()) {
                        panelAutomata.setAutomata(listaAutomatas.get(index));
                    }
                }
            }
        });

        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setBounds(30, 80, 160, 220); // Más alto
        panel.add(scrollLista);

        // ------------------- BOTONES ----------------------
        btn1 = new JButton("Analizar");
        btn1.setBounds(210, 80, 140, 40);
        btn1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn1.setFocusPainted(false);
        btn1.addActionListener(this);
        panel.add(btn1);

        btn2 = new JButton("Ejecutar");
        btn2.setBounds(210, 140, 140, 40);
        btn2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn2.setFocusPainted(false);
        btn2.addActionListener(this); // Corregido: btn2 en lugar de btn1
        panel.add(btn2);

        btn3 = new JButton("Limpiar");
        btn3.setBounds(210, 200, 140, 40);
        btn3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn3.setFocusPainted(false);
        btn3.addActionListener(this); // Corregido: btn3 en lugar de btn1
        panel.add(btn3);

        btn4 = new JButton("Guardar");
        btn4.setBounds(210, 260, 140, 40);
        btn4.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn4.setFocusPainted(false);
        btn4.addActionListener(this); // Corregido: btn4 en lugar de btn1
        panel.add(btn4);

        // ------------------ TEXT AREA ---------------------
        areaTexto = new JTextArea();
        areaTexto.setEditable(true);
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 15));

        JScrollPane scrollArea = new JScrollPane(areaTexto);
        scrollArea.setBounds(410, 20, 400, 280); // Reducido para dejar espacio al grafo
        panel.add(scrollArea);

        // ---------------- PANEL AUTOMATA -------------------
        panelAutomata = new PanelAutomata();
        panelAutomata.setBounds(30, 320, 780, 320);
        panel.add(panelAutomata);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            accionAnalizar();
        } else if (e.getSource() == btn2) {
            accionEjecutar();
        } else if (e.getSource() == btn3) {
            accionLimpiar();
        } else if (e.getSource() == btn4) {
            accionGuardar();
        }
    }

    // ----------------------------------------------------
    // ACCIONES DE LOS BOTONES
    // ----------------------------------------------------

    private void accionAnalizar() {
        listaAutomatas.clear();
        modeloLista.clear();

        String texto = areaTexto.getText();
        String[] lineas = texto.split("\n");

        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            Automata aut = new Automata(linea);
            listaAutomatas.add(aut);

            // Mostrar el estado en la lista
            String prefijo = aut.isEsValida() ? "[OK] " : "[ERR] ";
            if (linea.trim().isEmpty()) {
                modeloLista.addElement(prefijo + "Línea " + (i + 1) + ": (Vacía)");
            } else {
                modeloLista.addElement(prefijo + "Línea " + (i + 1) + ": " + linea.trim());
            }
        }

        // Seleccionar automáticamente la primera línea si existe
        if (!listaAutomatas.isEmpty()) {
            lista.setSelectedIndex(0);
        } else {
            panelAutomata.setAutomata(null);
        }
    }

    private void accionEjecutar() {
        if (listaAutomatas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero debés Analizar el pseudocódigo.", "Ejecución",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar si hay algún error sintáctico antes de ejecutar
        for (int i = 0; i < listaAutomatas.size(); i++) {
            if (!listaAutomatas.get(i).isEsValida()) {
                JOptionPane.showMessageDialog(this,
                        "No se puede ejecutar. Corregí los errores de sintaxis primero (Línea " + (i + 1) + ").",
                        "Error de Sintaxis", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Ejecución interpretada básica de variables y condicionales Si/SiNo
        StringBuilder logEjecucion = new StringBuilder();
        logEjecucion.append("=== INICIANDO EJECUCIÓN DEL PSEUDOCÓDIGO ===\n\n");

        Map<String, Double> variables = new HashMap<>();
        boolean condicionActiva = true; // Si estamos dentro de un bloque cuyo condicional fue verdadero
        boolean bloqueSiEjecutado = false; // Para controlar el SiNo

        for (int i = 0; i < listaAutomatas.size(); i++) {
            Automata aut = listaAutomatas.get(i);
            String linea = aut.getLineaOriginal();
            if (linea.isEmpty())
                continue;

            String tipo = aut.getTipoDFA();

            if (tipo.equals("Numero")) {
                if (condicionActiva) {
                    // Ejemplo: Numero x = 10 o Numero x = y
                    // Tokenizar para extraer datos
                    String[] partes = linea.split("=");
                    String parteIzquierda = partes[0].trim();
                    String parteDerecha = partes[1].trim();

                    String id = parteIzquierda.substring(6).trim(); // Quitar "Numero "

                    try {
                        double valor;
                        if (variables.containsKey(parteDerecha)) {
                            valor = variables.get(parteDerecha);
                        } else {
                            valor = Double.parseDouble(parteDerecha);
                        }
                        variables.put(id, valor);
                        logEjecucion.append(String.format("[Línea %d] Declaración: %s = %.2f\n", i + 1, id, valor));
                    } catch (NumberFormatException ex) {
                        logEjecucion.append(
                                String.format("[Línea %d] ERROR de Ejecución: Valor '%s' no definido o inválido\n",
                                        i + 1, parteDerecha));
                    }
                } else {
                    logEjecucion.append(String.format("[Línea %d] Ignorado (bloque condicional inactivo)\n", i + 1));
                }
            } else if (tipo.equals("Si")) {
                // Sintaxis: Si ( x < 10 ) o Si x < 10
                // Extraer la comparación
                String condString = linea.substring(2).trim(); // Quitar "Si"
                if (condString.startsWith("(")) {
                    condString = condString.substring(1, condString.length() - 1).trim();
                }

                // Expresión regular para separar: id/num operador id/num
                Pattern pattern = Pattern.compile(
                        "([a-zA-Z_][a-zA-Z0-9_]*|-?\\d+(\\.\\d+)?)\\s*(<=|>=|<|>)\\s*([a-zA-Z_][a-zA-Z0-9_]*|-?\\d+(\\.\\d+)?)");
                Matcher m = pattern.matcher(condString);

                if (m.find()) {
                    String op1 = m.group(1);
                    String operador = m.group(3);
                    String op2 = m.group(4);

                    double val1 = obtenerValorTermino(op1, variables);
                    double val2 = obtenerValorTermino(op2, variables);

                    boolean resultado = evaluarRelacion(val1, operador, val2);
                    condicionActiva = resultado;
                    bloqueSiEjecutado = resultado;

                    logEjecucion.append(String.format("[Línea %d] Evaluación Si: %.2f %s %.2f -> %s\n",
                            i + 1, val1, operador, val2, resultado ? "VERDADERO" : "FALSO"));
                } else {
                    logEjecucion.append(
                            String.format("[Línea %d] ERROR de Ejecución: Formato de condición inválido\n", i + 1));
                    condicionActiva = false;
                }
            } else if (tipo.equals("SiNo")) {
                // Se ejecuta sólo si la rama "Si" anterior fue falsa
                condicionActiva = !bloqueSiEjecutado;
                logEjecucion.append(String.format("[Línea %d] Bloque SiNo: Activo = %s\n", i + 1, condicionActiva));
            }
        }

        logEjecucion.append("\n=== VARIABLES FINALES ===\n");
        if (variables.isEmpty()) {
            logEjecucion.append("(Ninguna variable registrada)\n");
        } else {
            for (Map.Entry<String, Double> entry : variables.entrySet()) {
                logEjecucion.append(String.format("- %s = %.2f\n", entry.getKey(), entry.getValue()));
            }
        }

        // Mostrar el log en una ventana nueva
        JDialog diag = new JDialog(this, "Consola de Ejecución", true);
        diag.setSize(450, 350);
        diag.setLocationRelativeTo(this);

        JTextArea areaConsola = new JTextArea(logEjecucion.toString());
        areaConsola.setEditable(false);
        areaConsola.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaConsola.setBackground(Color.BLACK);
        areaConsola.setForeground(new Color(0, 230, 118)); // Verde consola

        JScrollPane scroll = new JScrollPane(areaConsola);
        diag.add(scroll);
        diag.setVisible(true);
    }

    private double obtenerValorTermino(String termino, Map<String, Double> variables) {
        if (variables.containsKey(termino)) {
            return variables.get(termino);
        }
        try {
            return Double.parseDouble(termino);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private boolean evaluarRelacion(double v1, String op, double v2) {
        switch (op) {
            case "<":
                return v1 < v2;
            case ">":
                return v1 > v2;
            case "<=":
                return v1 <= v2;
            case ">=":
                return v1 >= v2;
            default:
                return false;
        }
    }

    private void accionLimpiar() {
        areaTexto.setText("");
        modeloLista.clear();
        listaAutomatas.clear();
        panelAutomata.setAutomata(null);
    }

    private void accionGuardar() {
        JFileChooser chooser = new JFileChooser();
        int r = chooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
                w.write(areaTexto.getText());
                JOptionPane.showMessageDialog(this, "Pseudocódigo guardado correctamente.", "Guardar",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Establecer Look & Feel del sistema para una apariencia nativa más pulida
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal v = new VentanaPrincipal();
            v.setVisible(true);
        });
    }
}