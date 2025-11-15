package basicos.sincronizacionmultiple;

public class HiloSegmentoSincronizadoConEstados {
    private int contador1=0;
    private int contador2=0;

    private Object block1=new Object();
    private Object block2=new Object();

    public void incrementar1()
    {
        String name = Thread.currentThread().getName();

        synchronized (block1)
        {
            System.out.println(name + " → entrando en incrementar1 (adquiere el lock).");

            try
            {
                Thread.sleep(200); // 200ms con el lock
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            contador1++;
        }
    }

    public void incrementar2()
    {
        String name = Thread.currentThread().getName();

        synchronized (block2)
        {
            System.out.println(name + " → entrando en incrementar2 (adquiere el lock).");

            try
            {
                Thread.sleep(200); // 200ms con el lock
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            contador2++;
        }
    }

    public void creaHilos()
    {
        Thread t1=new Thread(()->{
            for(int i=0;i<5;i++)
                incrementar1();
        },"t1");

        Thread t2=new Thread(()->{
            for(int i=0;i<5;i++)
                incrementar2();
        },"t2");

        // Monitor que imprime el estado de los hilos hasta que ambos terminen
        Thread monitor = new Thread(() -> {
            while (true) {

                Thread.State estadoHilo1 = t1.getState();
                Thread.State estadoHilo2 = t2.getState();

                System.out.println("[Monitor Estados -] t1: " + estadoHilo1 + " | t2: " + estadoHilo2 );

                //Nos salimos del bucle while cuando ambos hilos hayan terminado
                if (estadoHilo1 == Thread.State.TERMINATED && estadoHilo2 == Thread.State.TERMINATED) break;

                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("[Monitor Estados - ] Ambos hilos TERMINATED. Monitor sale.");

        }, "monitor");

        t1.start();
        t2.start();
        monitor.start();

        try {
            t1.join();
            t2.join();
            monitor.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Valor de contador1: " + contador1);
        System.out.println("Valor de contador2: " + contador2);
    }
}
