package locks.readwritelocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocks {

    //Creamos los bloqueadores de lectura y escritura
    private static ReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    //Creamos el lock para lectura
    private static Lock readLock=readWriteLock.readLock();

    //Creamos el lock para lectura
    private static Lock writeLock=readWriteLock.writeLock();

    private static List<Integer> lista=new ArrayList<>();

    public static void main(String[] args) {

        Thread escritor=new Thread(new Escritor());

        Thread lector1=new Thread(new Lector());
        Thread lector2=new Thread(new Lector());
        Thread lector3=new Thread(new Lector());
        Thread lector4=new Thread(new Lector());

        escritor.start();
        lector1.start();
        lector2.start();
        lector3.start();
        lector4.start();


    }

    public static class Escritor implements Runnable
    {

        @Override
        public void run() {
            while (true)
            {
                try
                {
                    escribeDatos();
                }
                catch (InterruptedException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void escribeDatos() throws InterruptedException {
            Thread.sleep(10000);
            writeLock.lock();
            try
            {
                int valor=(int) (Math.random()*10);
                System.out.println("Escribiendo el valor: " + valor);
                lista.add(valor);
            }
            finally {
                writeLock.unlock();
            }


        }
    }

    public static class Lector implements Runnable
    {

        @Override
        public void run() {
            while (true)
            {
                try
                {
                    leeDatos();
                }
                catch (InterruptedException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void leeDatos() throws InterruptedException {
            Thread.sleep(4000);
            readLock.lock();

            try
            {
                System.out.println("Lista: " + lista);
            }
            finally {
                readLock.unlock();
            }


        }
    }

}
