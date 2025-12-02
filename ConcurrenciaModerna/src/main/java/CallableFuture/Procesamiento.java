package CallableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Procesamiento {

    public static class Procesador implements Callable<String> {

        private int identificador;

        public Procesador(int identificador)
        {
            if (identificador<=0)
                throw new IllegalArgumentException("ERROR: El identificador del callable no puede ser menor que 1.");

            this.identificador=identificador;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(2000);

            return "Identificador: " + identificador;
        }
    }

    public static void main(String[] args) {

        ExecutorService executor= Executors.newFixedThreadPool(2);
        List<Future<String>> lista=new ArrayList<>();
        for (int i=1;i<=10;i++)
        {
            Future<String> future=executor.submit(new Procesador(i));
            lista.add(future);
        }

        for(Future<String> f:lista)
        {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

        executor.shutdown();
        try {
            if(!executor.awaitTermination(1000,TimeUnit.MILLISECONDS))
            {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            executor.shutdownNow();
        }
    }
}
