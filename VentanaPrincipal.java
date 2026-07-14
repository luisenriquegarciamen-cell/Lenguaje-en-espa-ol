import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private JList<String> lista;
    private JTextArea areaTexto;
    private JButton btn1, btn2, btn3, btn4;

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
        btn1.addActionListener(this);
        panel.add(btn2);
        btn3 = new JButton("Limpiar");
        btn3.setBounds(210, 200, 140, 40);
        btn3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn3.setFocusPainted(false);
        btn1.addActionListener(this);
        panel.add(btn3);
        btn4 = new JButton("Guardar");
        btn4.setBounds(210, 260, 140, 40);
        btn4.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn4.setFocusPainted(false);
        btn1.addActionListener(this);
        panel.add(btn4);

        //------------------ TEXT AREA ---------------------

        areaTexto = new JTextArea();
        areaTexto.setEditable(true);
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 15));

        JScrollPane scrollArea = new JScrollPane(areaTexto);

        // Ocupa prácticamente toda la mitad derecha
        scrollArea.setBounds(410, 20, 400, 420);

        panel.add(scrollArea);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //------------- PENDIENTE POR HACER -----------------
    }

    public static void main(String[] args) {
        VentanaPrincipal v = new VentanaPrincipal();
        v.setVisible(true);
    }

}