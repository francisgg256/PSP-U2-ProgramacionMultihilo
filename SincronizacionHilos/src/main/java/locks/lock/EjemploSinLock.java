package locks.lock;

public class EjemploSinLock {
    public static void main(String[] args) throws InterruptedException {
        ContadorSinLock contador = new ContadorSinLock();

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
