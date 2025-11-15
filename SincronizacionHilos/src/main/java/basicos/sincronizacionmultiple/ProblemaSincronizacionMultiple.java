package basicos.sincronizacionmultiple;

public class ProblemaSincronizacionMultiple {

    public static int contador1=0;
    public static int contador2=0;

    public static synchronized void incrementar1()
    {
        String name = Thread.currentThread().getName();
        System.out.println(name + " → entrando en incrementar1 (adquiere el lock).");
        contador1++;
        System.out.println(name + " → saliendo de incrementar1 (libera el lock).");
    }

    public static synchronized void incrementar2()
    {
        String name = Thread.currentThread().getName();
        System.out.println(name + " → entrando en incrementar2 (adquiere el lock).");
        contador2++;
        System.out.println(name + " → saliendo de incrementar2 (libera el lock).");
    }


    public static void main(String[] args) {
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
