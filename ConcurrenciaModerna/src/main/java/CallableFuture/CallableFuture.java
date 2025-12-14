package CallableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class CallableFuture {
    public static void main(String[] args) {
        // 1. Control de Recursos: Solo permitimos 10 hilos simultáneos.
        // Las tareas excedentes esperan en una cola interna automáticamente.
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<String>> futuros = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            final int taskId = i;

            // 2. Submit devuelve un Future. No gestionamos el hilo, gestionamos la Tarea.
            // Usamos Callable (permite return) en vez de Runnable.
            Future<String> futuro = executor.submit(() -> {
                Thread.sleep(100); // Simula trabajo
                return "Tarea " + taskId + " completada"; // ¡Retorno directo!
            });

            futuros.add(futuro);
        }

        // 3. Recolectar resultados de forma ordenada
        for (Future<String> f : futuros) {
            try {
                // .get() espera si es necesario y nos da el valor o la excepción
                System.out.println(f.get());
            } catch (Exception e) {
                e.printStackTrace(); // Aquí capturamos errores que ocurrieron DENTRO del hilo
            }
        }

        // 4. Apagar el pool ordenadamente
        executor.shutdown();
        try {
            if(!executor.awaitTermination(1000, TimeUnit.MILLISECONDS))
            {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            executor.shutdownNow();
        }
    }
}
