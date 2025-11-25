package productoresconsumidores.colacadenas;

import java.util.LinkedList;
import java.util.Queue;

public class ProductorConsumidorCadenas {

    public static void main(String[] args) {
        Queue<String> cola=new LinkedList<>();

        Thread productor=new Thread(new Productor(cola));
        Thread consumidor=new Thread(new Consumidor(cola));

        productor.start();
        consumidor.start();

    }
}
