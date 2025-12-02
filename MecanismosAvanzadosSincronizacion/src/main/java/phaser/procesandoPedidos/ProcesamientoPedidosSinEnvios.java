package phaser.procesandoPedidos;

import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class ProcesamientoPedidosSinEnvios {
    // Clase que extiende Phaser para imprimir cuando se completa una fase
    private static class PedidoPhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            System.out.println("\n--- Fase " + phase + " completada por " + registeredParties + " pedidos ---\n");
            // Si devuelve true, el Phaser termina (isTerminated() = true)
            // Queremos terminar si no quedan participantes O si llegamos a la fase 2
            return phase == 1 || registeredParties == 0;
        }
    }

    // Clase que representa un pedido procesándose
    static class Pedido implements Runnable {
        private final int id;
        private final Phaser phaser;

        public Pedido(int id, Phaser phaser) {
            Objects.requireNonNull(phaser,"ERROR: El phaser de los hilos no puede ser nulo.");

            if (id<1)
                throw new IllegalArgumentException("ERROR: El id del hilo no puede ser menor que 1.");

            this.id = id;
            this.phaser = phaser;
            phaser.register(); // Registrar dinámicamente el pedido
        }

        @Override
        public void run() {
            try {
                // --- FASE 0: Validación ---
                procesarFase("Validación", 1000, 3000);
                phaser.arriveAndAwaitAdvance();

                // Verificación de seguridad: ¿Sigue vivo el phaser?
                if (phaser.isTerminated()){
                    System.out.println("-> Pedido " + id + " detiene su ejecución (Phaser terminado).");
                    return;
                }

                // --- FASE 1: Empaquetado ---
                procesarFase("Empaquetado", 1000, 4000);

                // Al llegar aquí, se ejecuta onAdvance.
                // Si phase es 1, onAdvance devuelve true y el Phaser pasa a estado TERMINATED.
                phaser.arriveAndAwaitAdvance();

                // --- PUNTO CRÍTICO DE LA CORRECCIÓN ---
                // Preguntamos: ¿El Phaser ha terminado?
                if (phaser.isTerminated()) {
                    System.out.println("-> Pedido " + id + " detiene su ejecución (Phaser terminado).");
                    return; // Salimos del método run(), finalizando el hilo correctamente.
                }

                // --- FASE 2: Envío (Solo se ejecuta si el Phaser NO ha terminado) ---
                procesarFase("Envío", 1000, 5000);
                phaser.arriveAndDeregister();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void procesarFase(String fase, int minMillis, int maxMillis) throws InterruptedException {
            int tiempo = ThreadLocalRandom.current().nextInt(minMillis, maxMillis);
            System.out.println("Pedido " + id + " procesando " + fase + " (duración: " + tiempo + " ms)");
            Thread.sleep(tiempo); // Simula procesamiento
        }
    }

    public static void main(String[] args) {

        PedidoPhaser phaser = new PedidoPhaser();

        System.out.println("=== Inicio del procesamiento de pedidos ===");

        // Crear 5 pedidos simultáneos
        for (int i = 1; i <= 5; i++) {
            new Thread(new Pedido(i, phaser)).start();
        }

        // Simular llegada de nuevos pedidos después de un tiempo
//        new Thread(() -> {
//            try {
//                Thread.sleep(4000); // Llegada tardía
//                System.out.println("\nNuevo pedido llegando tarde: Pedido 6\n");
//                new Thread(new Pedido(6, phaser)).start();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }).start();
    }
}
