package cyclicbarrier.multiplesfases;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DataProcessing {
    public static void main(String[] args) {
        // Barrera para 3 hilos trabajadores
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            // Esto se ejecuta cada vez que se completa una etapa
            System.out.println(">>> ETAPA COMPLETADA. Pasando a la siguiente fase... <<<");
        });

        for (int i = 0; i < 3; i++) {
            new Thread(new DataWorker(barrier, i)).start();
        }
    }

    static class DataWorker implements Runnable {
        private CyclicBarrier barrier;
        private int id;

        public DataWorker(CyclicBarrier barrier, int id) {
            Objects.requireNonNull(barrier, "ERROR: El barrier de los hilos no puede ser nulo.");

            this.barrier = barrier;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // --- FASE 1: DESCARGA ---
                System.out.println("Trabajador " + id + ": Descargando datos...");
                Thread.sleep((long) (Math.random() * 2000));
                System.out.println("Trabajador " + id + ": Descarga finalizada. Esperando...");

                barrier.await(); // Primera espera: Nadie pasa hasta que todos descarguen.

                // --- FASE 2: ANÁLISIS ---
                System.out.println("Trabajador " + id + ": Analizando datos...");
                Thread.sleep((long) (Math.random() * 2000));
                System.out.println("Trabajador " + id + ": Análisis finalizado. Esperando...");

                barrier.await(); // ¡Reutilizamos la misma barrera!

                System.out.println("Trabajador " + id + ": Tarea terminada por completo.");

            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("Worker " + id + ": Error en la barrera.");
            }
        }
    }
}
