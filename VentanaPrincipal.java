import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private JList<String> lista;
    private JTextArea txtArea;
    private JButton btnAnalizar, btnEjecutar, btnGuardar, btnLimpiar;

    public VentanaPrincipal() {
        setTitle("Lenguaje en español");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245,245,245));

        //------------------- TÍTULO -----------------------

        JLabel titulo = new JLabel("Procesamiento de Datos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBounds(25, 15, 350, 35);
        panel.add(titulo);

        //------------------- JLIST ------------------------

        String datos[] = {
            "Linea 1",
            "Linea 2",
            "Linea 3",
            "Linea 4",
            "Linea 5"
        };

        lista = new JList<>(datos);
        lista.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setBounds(30, 80, 160, 120);
        panel.add(scrollLista);

        //------------------- BOTONES ----------------------

        btnAnalizar = new JButton("Analizar");
        btnAnalizar.setBounds(210, 80, 140, 40);
        btnAnalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAnalizar.setFocusPainted(false);
        btnAnalizar.addActionListener(this);
        panel.add(btnAnalizar);

        btnEjecutar = new JButton("Ejecutar");
        btnEjecutar.setBounds(210, 140, 140, 40);
        btnEjecutar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEjecutar.setFocusPainted(false);
        btnEjecutar.addActionListener(this);
        panel.add(btnEjecutar);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(210, 260, 140, 40);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(this);
        panel.add(btnGuardar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(210, 200, 140, 40);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(this);
        panel.add(btnLimpiar);

        //------------------ TEXT AREA ---------------------

        txtArea = new JTextArea();
        txtArea.setEditable(true);
        txtArea.setFont(new Font("Consolas", Font.PLAIN, 15));

        JScrollPane scrollArea = new JScrollPane(txtArea);

        // Ocupa prácticamente toda la mitad derecha
        scrollArea.setBounds(410, 20, 400, 420);

        panel.add(scrollArea);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //---------------- Boton Analizar -------------------
        if (e.getSource() == btnAnalizar) {

        }

        //---------------- Boton Ejecutar -------------------
        if (e.getSource() == btnEjecutar) {

        }

        //---------------- Boton Guardar -------------------
        if (e.getSource() == btnGuardar) {
            //JOptionPane que pide el nombre del archivo
            String nombre = JOptionPane.showInputDialog(this,
                    "Ingrese el nombre del archivo (sin extensión):",
                    "Guardar archivo",
                    JOptionPane.PLAIN_MESSAGE);

            if (nombre != null) {
                //Agrega un "txt" al final en el caso de que no lo tenga
                if (!nombre.toLowerCase().endsWith(".txt")) {
                    nombre = nombre + ".txt";
                }

                File archivo = new File(nombre);
                boolean existia = archivo.exists();

                try (FileWriter escritor = new FileWriter(archivo)) {
                    escritor.write(txtArea.getText());

                    //Se actua dependiendo de si el archivo existia o no
                    if (existia) {
                        JOptionPane.showMessageDialog(this,
                                "El archivo ya existía. Se ha sobrescrito: " + archivo.getName(),
                                "Guardar",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Archivo creado y guardado: " + archivo.getName(),
                                "Guardar",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    //Si algo sale mal se muestra un mensaje en un JOptionPane
                    JOptionPane.showMessageDialog(this,
                            "Error al guardar el archivo:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        //---------------- Boton Limpiar -------------------
        if (e.getSource() == btnLimpiar) {
            txtArea.setText("");
        }
    }

    public static void main(String[] args) {
        VentanaPrincipal v = new VentanaPrincipal();
        v.setVisible(true);
    }

}