package locks.readwritelocks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocksBiblioteca {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();

        // Hilos lectores
        Runnable lector = () -> biblioteca.leer(Thread.currentThread().getName());

        // Hilo escritor
        Runnable escritor = () -> biblioteca.escribir(Thread.currentThread().getName(), "Versión 2");

        Thread t1 = new Thread(lector, "Lector 1");
        Thread t2 = new Thread(lector, "Lector 2");
        Thread t3 = new Thread(escritor, "Escritor 1");

        t1.start();
        t2.start();
        t3.start();
    }

    public static class Biblioteca {

        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        private String libro = "Versión 1";

        // Lectura (muchos pueden leer a la vez)
        public void leer(String lector) {

            rwLock.readLock().lock();
            try {
                System.out.println(lector + " lee: " + libro);
                Thread.sleep(500); // simula tiempo de lectura
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.readLock().unlock();
            }
        }

        // Escritura (solo uno puede escribir, bloquea lecturas)
        public void escribir(String escritor, String nuevaVersion) {
            rwLock.writeLock().lock();
            try {
                libro = nuevaVersion;
                System.out.println(escritor + " escribió: " + libro);
                Thread.sleep(1000); // simula tiempo de escritura
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }

}
