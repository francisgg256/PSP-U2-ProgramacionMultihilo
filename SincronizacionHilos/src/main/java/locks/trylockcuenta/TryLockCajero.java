package locks.trylockcuenta;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockCajero {
    public static void main(String[] args) {
        Cajero cajero = new Cajero();

        Thread t1 = new Thread(() -> cajero.usar("Cliente 1"));
        Thread t2 = new Thread(() -> cajero.usar("Cliente 2"));

        t1.start();
        t2.start();
    }

    public static class Cajero {

        private final Lock lock = new ReentrantLock();

        public void usar(String nombreCliente) {

            System.out.println(nombreCliente + " intenta usar el cajero...");

            if (lock.tryLock())
            { // intenta sin esperar indefinidamente
                try {
                    System.out.println(nombreCliente + " está usando el cajero");
                    Thread.sleep(1000); // simula operación
                    System.out.println(nombreCliente + " terminó");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(nombreCliente + " no pudo usarlo (ocupado)");
            }
        }
    }
}
