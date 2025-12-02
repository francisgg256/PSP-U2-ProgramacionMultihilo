package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleExecutor {

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
            long duracionTarea=(long) Math.random()*10000;

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
        ExecutorService executor= Executors.newSingleThreadExecutor();

        //Creamos 3 tareas que son ejecutadas por el mismo hilo
        for(int i=0;i<3;i++)
        {
            executor.execute(new Tarea(i+1));
            //executor.submit(new Tarea(i+1));
        }

        //Creamos 5 tareas que son ejecutadas por el mismo hilo.
        for (int i = 1; i <= 5; i++) {

            int taskId = i;
            //El método submit recibe como parámetro el Runnable. En este caso como una expresión lambda.
            executor.submit(() ->
                    System.out.println("Tarea " + taskId + " ejecutada por " + Thread.currentThread().getName())
            );
        }

        //executor.shutdown();
    }
}
