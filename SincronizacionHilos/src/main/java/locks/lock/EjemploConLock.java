package locks.lock;

public class EjemploConLock {
    public static void main(String[] args) throws InterruptedException {
        ContadorConLock contador = new ContadorConLock();

        Runnable tarea = () -> {
            for (int i = 0; i < 1000; i++) {
                contador.incrementar();
            }
        };

        Thread t1 = new Thread(tarea);
        Thread t2 = new Thread(tarea);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Valor final: " + contador.getValor());
    }
}
