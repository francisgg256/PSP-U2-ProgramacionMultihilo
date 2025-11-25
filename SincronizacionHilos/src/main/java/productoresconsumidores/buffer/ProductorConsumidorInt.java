package productoresconsumidores.buffer;

public class ProductorConsumidorInt {

    public static void main(String[] args) {
        BufferInt buffer=new BufferInt();

        Thread productor=new Thread(new Productor(buffer));
        Thread consumidor=new Thread(new Consumidor(buffer));

        productor.start();
        consumidor.start();

    }
}
