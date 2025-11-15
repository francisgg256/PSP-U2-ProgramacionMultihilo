package basicos.sincronizacionmultiple;

public class HiloSegmentoSincronizado {
    private int contador1=0;
    private int contador2=0;

    private Object block1=new Object();
    private Object block2=new Object();

    public void incrementar1()
    {
        synchronized (block1)
        {
            contador1++;
        }
    }

    public void incrementar2()
    {
        synchronized (block2)
        {
            contador2++;
        }
    }

    public void creaHilos()
    {
        Thread t1=new Thread(()->{
            for(int i=0;i<1000;i++)
                incrementar1();
        });

        Thread t2=new Thread(()->{
            for(int i=0;i<1000;i++)
                incrementar2();
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Valor de contador1: " + contador1);
        System.out.println("Valor de contador2: " + contador2);
    }
}
