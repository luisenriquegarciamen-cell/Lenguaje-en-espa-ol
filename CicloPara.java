/*  
para i = 1 hasta 10 hacer
    mostrar i
fin_para 
*/

public class CicloPara {
    public boolean esCicloPara(String linea){
        String[] tokens = linea.split(" ");
        int estado = 0;

        for (String token : tokens){

            switch(estado) {
                case 0:
                    if(token.equals("para"))
                        estado = 1;
                    else
                        return false;
                    break;

                case 1:
                    if(esVariable(token)) //Metodo pendiente
                        estado = 2;
                    else
                        return false;
                    break;

                case 2:
                    if(token.equals("="))
                        estado = 3;
                    else
                        return false;
                    break;

                case 3:
                    if(esNumero(token)) //Metodo pendiente
                        estado = 4;
                    else
                        return false;
                    break;

                case 4:
                    if(token.equals("hasta"))
                        estado = 5;
                    else
                        return false;
                    break;

                case 5:
                    if(esNumero(token)) //Metodo pendiente
                        estado = 6;
                    else
                        return false;
                    break;

                case 6:
                    if(token.equals("hacer"))
                        estado = 7;
                    else
                        return false;
                    break;
            }
        }
        return estado == 7;
    }
}

