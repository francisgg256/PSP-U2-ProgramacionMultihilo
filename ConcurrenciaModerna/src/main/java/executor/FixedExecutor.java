package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedExecutor {

    public static class Tarea implements Runnable{
        private int identificador;

        public Tarea(int identificador)
        {
            if (identificador<0)
                throw new IllegalArgumentException("ERROR: El identificador de una tarea no puede ser negativo.");

            this.identificador=identificador;
        }


        @Override
        public void run() {
            //long duracionTarea=(long) Math.random()*5;

            System.out.println("Tarea con identificador " + identificador + " está siendo ejecutada por el hilo: " + Thread.currentThread().getName());

            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        // Ejecutará las tareas secuencialmente una tras otra en un solo hilo.
        ExecutorService executor= Executors.newFixedThreadPool(2);

        //Creamos 10 tareas que son ejecutadas por los dos hilos
        for(int i=0;i<10;i++)
        {
            executor.execute(new Tarea(i+1));
        }


        executor.shutdown();
    }
}
