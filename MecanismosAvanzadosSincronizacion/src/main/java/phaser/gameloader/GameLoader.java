package phaser.gameloader;

import java.util.Objects;
import java.util.concurrent.Phaser;

public class GameLoader {

    public static void main(String[] args) {

        Phaser phaser = new Phaser();

        System.out.println("--- INICIANDO MOTOR DEL JUEGO ---");

        // Creamos 3 hilos trabajadores
        for (int i = 1; i <= 3; i++) {
            new Thread(new Tarea(phaser, "Servicio " + i)).start();
        }
    }

    static class Tarea implements Runnable {
        private final Phaser phaser;
        private final String name;

        public Tarea(Phaser phaser, String name) {
            Objects.requireNonNull(phaser, "ERROR: El phaser de los hilos no puede ser nulo.");
            Objects.requireNonNull(name, "ERROR: El nombre de los hilos no puede ser nulo.");

            if (name.isBlank())
                throw new IllegalArgumentException("ERROR: El nombre de los hilos no puede ser vacío.");

            this.phaser = phaser;
            this.name = name;
            // ¡CLAVE! Cada hilo se registra a sí mismo dinámicamente
            this.phaser.register();
        }

        @Override
        public void run() {
            // --- FASE 1: Carga de Assets ---
            System.out.println(name + ": Cargando assets (Fase " + phaser.getPhase() + ")...");
            dormir(1000); // Simular trabajo
            System.out.println(name + ": Assets cargados.");

            System.out.println(name + ": Cargando sonido (Fase " + phaser.getPhase() + ")...");
            dormir(500);
            System.out.println(name + ": Sonidos cargados.");

            System.out.println(name + ": Cargando mapas (Fase " + phaser.getPhase() + ")...");
            dormir(500);
            System.out.println(name + ": Mapas cargados.");

            // Punto de sincronización. Nadie pasa de aquí hasta que los 3 lleguen.
            phaser.arriveAndAwaitAdvance();

            // --- FASE 2: Conexión Online ---
            System.out.println(name + ": Conectando al servidor (Fase " + phaser.getPhase() + ")...");
            dormir(1000);
            System.out.println(name + ": Verificando perfil (Fase " + phaser.getPhase() + ")...");
            dormir(1000);
            System.out.println(name + ": Conectado. Listo para jugar.");

            // Finalizamos y nos desregistramos
            phaser.arriveAndDeregister();
        }

        private void dormir(int ms) {
            try
            {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
