public class ComparadoresYO {
    public boolean esComparadorValido(String cadena){
        int estado = 0;
        if ((cadena == "&") || (cadena == "|")) {
            estado = 1;
        } else {
            return false;
        }
        return estado == 1;
    }
}
