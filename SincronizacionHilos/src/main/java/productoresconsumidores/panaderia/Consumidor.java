package productoresconsumidores.panaderia;

import java.util.Objects;

public class Consumidor implements Runnable{

    private Panaderia panaderia;

    public Consumidor(Panaderia panaderia)
    {
        Objects.requireNonNull(panaderia,"ERROR: Una panaderia no puede ser nula.");
        this.panaderia=panaderia;
    }

    @Override
    public void run() {
        //El consumidor debe consumir 10 panes
        for(int i=0; i<10;i++)
            panaderia.consumir();
    }
}
