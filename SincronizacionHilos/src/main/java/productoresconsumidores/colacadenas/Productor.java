package productoresconsumidores.colacadenas;

import java.util.Queue;

public class Productor implements Runnable{
    private final Queue<String> cola;

    public Productor(Queue<String> cola)
    {
        this.cola=cola;
    }


    @Override
    public void run()
    {
        while (true)
            produceDato();
    }

    private void produceDato()
    {
        try
        {
            synchronized (cola)
            {
                if (cola.size()==10)
                {
                    //Si la cola está llena, el productor debe esperar a que se consuman datos
                    System.out.println("Productor, esperando...");
                    cola.wait();
                }

                Thread.sleep(1000); //Simulamos que el tiempo de procesamiento sea 1ms

                cola.add("id " + cola.size());
                System.out.println("Productor produce nuevo dato. Total datos sin consumir: " + cola.size());

                if (cola.size()==1)
                    //Como ya se ha producido un dato, el consumidor ya puede consumir
                    cola.notify();

            }
        }
        catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }


    }
}
