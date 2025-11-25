package productoresconsumidores.colacadenas;

import java.util.Queue;

public class Consumidor implements Runnable {
    private final Queue<String> cola;

    public Consumidor(Queue<String> cola)
    {
        this.cola=cola;
    }

    @Override
    public void run() {
        while (true)
            consumeDato();
    }

    private void consumeDato()
    {
        try
        {
            synchronized (cola)
            {
                if (cola.isEmpty())
                {
                    //Si la cola está vacía, el consumimdor debe esperar a que se produzcan datos
                    System.out.println("Consumidor debe esperar...");
                    cola.wait();
                }

                Thread.sleep(700); //Simulamos el tiempo de procesamiento de consumir un dato

                String dato=cola.remove();
                System.out.println("Consumidor consume dato. Dato consumido: " + dato + ". Total datos sin consumir: " + cola.size());

                if (cola.size()==9)
                {
                    //Como ya se ha consumido un dato, el productor ya puede producir
                    cola.notify();
                }

            }


        }
        catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
