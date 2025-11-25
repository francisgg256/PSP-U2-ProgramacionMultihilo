package locks.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SumaParalelaVector {
    private static int suma=0;

    private static Lock lock=new ReentrantLock();

    private static int[] vector=new int[10];

    public static void main(String[] args)  {
        for (int i=0; i<10; i++)
        {
            vector[i]=1000;
        }

        int posicionMedia=vector.length/2;

        Thread t1=new Thread(new Sumador(0,posicionMedia));
        Thread t2=new Thread(new Sumador(posicionMedia,2*posicionMedia));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("La suma de los valores del vector es: " + suma);

    }

    public static class Sumador implements Runnable
    {
        private int izquierda;
        private int derecha;

        public Sumador(int indiceIzquierdo, int indiceDerecho)
        {
            this.izquierda=indiceIzquierdo;
            this.derecha=indiceDerecho;
        }

        @Override
        public void run() {
            for(int i=izquierda;i<derecha; i++)
            {
                lock.lock();
                try
                {
                    suma=suma + vector[i]; //Condición de carrera
                }
                finally {
                    lock.unlock();
                }

            }
        }
    }
}
