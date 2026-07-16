import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AutomataPara extends JFrame {

    JTextField txtCodigo;
    JButton btnProcesar;

    ArrayList<Integer> recorrido = new ArrayList<>();

    public AutomataPara() {
        setTitle("Autómata del ciclo PARA");
        setSize(900,350);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(20, 20, 500, 30);
        add(txtCodigo);

        btnProcesar = new JButton("Procesar");
        btnProcesar.setBounds(550,20,120,30);
        add(btnProcesar);

        btnProcesar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarPara(txtCodigo.getText());
                repaint();
            }
        });
        setVisible(true);
    }

    //=================== AUTOMATA ===================

    public boolean validarPara(String linea){
        recorrido.clear();
        String[] tokens = linea.split(" ");
        int estado = 0;
        recorrido.add(0);

        for(String token : tokens){
            switch(estado){
                case 0:
                    if(token.equals("para")){
                        estado = 1;
                        recorrido.add(1);
                    }
                    else
                        return false;
                    break;

                case 1:
                    if(esIdentificador(token)){
                        estado = 2;
                        recorrido.add(2);
                    }
                    else
                        return false;
                    break;

                case 2:
                    if(token.equals("=")){
                        estado = 3;
                        recorrido.add(3);
                    }
                    else
                        return false;
                    break;

                case 3:
                    if(esNumero(token)){
                        estado = 4;
                        recorrido.add(4);
                    }
                    else
                        return false;
                    break;

                case 4:
                    if(token.equals("hasta")){
                        estado = 5;
                        recorrido.add(5);
                    }
                    else
                        return false;
                    break;

                case 5:
                    if(esNumero(token)){
                        estado = 6;
                        recorrido.add(6);
                    }
                    else
                        return false;
                    break;

                case 6:
                    if(token.equals("hacer")){
                        estado = 7;
                        recorrido.add(7);
                    }
                    else
                        return false;
                    break;
            }
        }
        return estado == 7;
    }

    public boolean esNumero(String s){
        try{
            Integer.parseInt(s);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean esIdentificador(String s){
        return s.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    //=================== GRAFO ===================

    @Override
    public void paint(Graphics g){
        super.paint(g);
        int y = 180;
        int[] x = {40,140,240,340,440,540,640,740};
        
        // Estados
        for(int i=0;i<8;i++){

            g.drawOval(x[i],y,50,50);
            g.drawString("q"+i,x[i]+18,y+30);

        }

        // Estado final
        g.drawOval(735,175,60,60);

        dibujarTransicion(g,0,1,"para",90,140);
        dibujarTransicion(g,1,2,"id",190,240);
        dibujarTransicion(g,2,3,"=",290,340);
        dibujarTransicion(g,3,4,"num",390,440);
        dibujarTransicion(g,4,5,"hasta",490,540);
        dibujarTransicion(g,5,6,"num",590,640);
        dibujarTransicion(g,6,7,"hacer",690,740);

    }

    public void dibujarTransicion(Graphics g,
                                  int origen,
                                  int destino,
                                  String texto,
                                  int x1,
                                  int x2){

        if(recorridoContiene(origen,destino))
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.BLACK);

        g.drawLine(x1,205,x2,205);

        g.drawString(texto,(x1+x2)/2-10,190);

    }

    public boolean recorridoContiene(int a,int b){

        for(int i=0;i<recorrido.size()-1;i++){

            if(recorrido.get(i)==a &&
               recorrido.get(i+1)==b)

                return true;

        }

        return false;

    }

    public static void main(String[] args) {
        new AutomataPara();
    }

}