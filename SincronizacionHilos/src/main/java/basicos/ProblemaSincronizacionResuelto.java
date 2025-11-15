package basicos;

public class ProblemaSincronizacionResuelto {

    public static int contador=0;

    public static synchronized void incrementar()
    {
        contador++;
    }

    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            for(int i=0;i<1000;i++)
                incrementar();
        });

        Thread t2=new Thread(()->{
            for(int i=0;i<1000;i++)
                incrementar();
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Valor de contador: " + contador);
    }
}
