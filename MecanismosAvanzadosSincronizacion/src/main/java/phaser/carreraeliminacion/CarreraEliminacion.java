package phaser.carreraeliminacion;

import java.util.Objects;
import java.util.concurrent.Phaser;

public class CarreraEliminacion {

    public static void main(String[] args) {
        // Sobrescribimos onAdvance con una clase anónima
        Phaser phaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("\n========================================");
                System.out.println(">> FASE " + phase + " COMPLETADA.");
                System.out.println(">> Participantes restantes: " + registeredParties);
                System.out.println("========================================\n");

                // Si devuelve true, el Phaser termina (isTerminated() = true)
                // Queremos terminar si no quedan participantes O si llegamos a la fase 3
                return registeredParties == 0 || phase == 2;
            }
        };

        // Lanzamos 3 corredores
        for (int i = 1; i <= 3; i++) {
            new Thread(new Runner(phaser, "Corredor " + i)).start();
        }

        phaser.arriveAndDeregister(); // Main se retira

    }

    static class Runner implements Runnable {
        private Phaser phaser;
        private String name;

        public Runner(Phaser phaser, String name) {
            Objects.requireNonNull(phaser, "ERROR: El phaser de los hilos no puede ser nulo.");
            Objects.requireNonNull(name, "ERROR: El nombre de los hilos no puede ser nulo.");

            if (name.isBlank())
                throw new IllegalArgumentException("ERROR: El nombre de los hilos no puede ser vacío.");

            this.phaser = phaser;
            this.name = name;
            this.phaser.register();
        }

        @Override
        public void run() {
            while (!phaser.isTerminated()) { // Mientras el Phaser siga vivo
                System.out.println(name + " está corriendo en la fase " + phaser.getPhase());

                // Simular carrera
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

                // En la fase 1, simulamos que el Corredor 2 se lesiona y abandona
                if (phaser.getPhase() == 1 && name.equals("Corredor 2")) {
                    System.out.println( name + " SE LESIONÓ Y ABANDONA");
                    phaser.arriveAndDeregister(); // ¡Adios! El Phaser recalcula las partes
                    break;
                } else {
                    // Esperar a los demás para la siguiente ronda
                    phaser.arriveAndAwaitAdvance();
                }
            }
        }
    }
}
