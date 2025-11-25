package productoresconsumidores.panaderia;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Panadero implements Runnable{

    private Panaderia panaderia;

    public Panadero(Panaderia panaderia)
    {
        Objects.requireNonNull(panaderia,"ERROR: Una panaderia no puede ser nula.");
        this.panaderia=panaderia;
    }

    @Override
    public void run() {
        //Panadero produce 10 panes
        for(int i=0;i<10;i++){
            panaderia.hornear("Pan número " + i);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500,2000));
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
