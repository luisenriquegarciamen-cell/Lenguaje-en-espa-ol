import java.util.ArrayList;

public class Booleano {
    public ArrayList<Integer> recorrido;

    public Booleano(ArrayList<Integer> recorrido) {
        this.recorrido = recorrido;
    }

    //Idea con ArrayList
    public boolean esBoolean(String cadena){
        int estado = 0;
        recorrido.add(0);
        recorrido.clear();

        if ((cadena == "falso") || (cadena == "verdadero")) {
            recorrido.add(1);
            estado = 1;
        } else {
            return false;
        }
        return estado == 1;
    }
    //Idea con estados (enteros)
    public boolean declararBooleano(String cadena) {
        int estado = 0;
        recorrido.clear();
        recorrido.add(0);

        if (cadena == "Logico") {
            estado = 1;
            recorrido.add(1);
        } else {
            return false;
        }
        return estado == 1;
    }
}