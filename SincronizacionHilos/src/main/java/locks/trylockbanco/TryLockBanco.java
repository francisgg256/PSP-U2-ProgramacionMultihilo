package locks.trylockbanco;

public class TryLockBanco {
    public static void main(String[] args) {
        Banco banco = new Banco();

        Runnable tarea = () -> banco.accederCuenta(Thread.currentThread().getName());

        Thread t1 = new Thread(tarea, "Hilo 1");
        Thread t2 = new Thread(tarea, "Hilo 2");

        t1.start();
        t2.start();
    }
}
