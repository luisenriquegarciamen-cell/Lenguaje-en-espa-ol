public class Transicion{
    public Estado origen, destino;
    public char simbolo;
    public Transicion(Estado origen,Estado destino,char simbolo){
        this.origen=origen;
        this.destino=destino;
        this.simbolo=simbolo;
    }
    public void dibujado(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        if(origen.equals(destino)){
            int x=origen.getX();
            int y=origen.getY();
            g2d.drawArc(x-20,y-50,40,40,0,360);
            g2d.drawString(simbolo,x,y-55);
        }else{
            int x1= origen.getX();
            int y1= origen.getY();
            int x2= destino.getX();
            int y2= destino.getY();
            g2d.drawLine(x1,y1,x2,y2);
            g2d.drawString(simbolo,(x1+x2)/2,(y1+y2)/2);
            
        }
    }
    //Getters
    public Estado getOrigen(){
        return origen;
    }
    public Estado getDestino(){
        return destino;
    }
    public char getSimbolo(){
        return simbolo;
    }
}