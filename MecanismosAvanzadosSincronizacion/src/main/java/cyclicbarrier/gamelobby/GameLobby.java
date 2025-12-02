package cyclicbarrier.gamelobby;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GameLobby {
    public static void main(String[] args) {
        // 1. Definimos la barrera para 4 jugadores.
        // La acción (lambda) se ejecutará cuando el último llegue.
        CyclicBarrier gameBarrier = new CyclicBarrier(4, () -> {
            System.out.println("\n*** ¡TODOS LOS JUGADORES LISTOS! INICIANDO PARTIDA... ***\n");
        });

        // 2. Creamos y lanzamos los hilos (Jugadores)
        for (int i = 1; i <= 4; i++) {
            new Thread(new Player(gameBarrier, "Jugador " + i)).start();
        }
    }

    static class Player implements Runnable {
        private CyclicBarrier barrier;
        private String name;

        public Player(CyclicBarrier barrier, String name) {
            Objects.requireNonNull(barrier,"ERROR: El barrier de los hilos no puede ser nulo.");
            Objects.requireNonNull(name, "ERROR: El nombre de un hilo no puede ser nulo.");

            if (name.isBlank())
                throw new IllegalArgumentException("ERROR: El nombre de un hilo no puede ser nulo.");

            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                // Simulamos tiempo de carga (aleatorio entre 1 y 5 seg)
                long duration = (long) (Math.random() * 4000) + 1000;
                System.out.println(name + " está cargando texturas y escena lobby... (" + duration + "ms)");
                Thread.sleep(duration);

                System.out.println(name + " ha llegado a la barrera. Esperando a los demás...");

                // --- AQUÍ ESTÁ LA MAGIA ---
                // El hilo se bloquea aquí hasta que el contador de la barrera llegue a 0.
                barrier.await();

                System.out.println(name + " comienza a jugar.");

            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
