package countdownlatch.servidorboot;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Imagina que estás levantando un servidor backend complejo.
 * Antes de decir "Servidor Iniciado en puerto 8080",
 * necesitas que la Base de Datos y el Sistema de Caché se conecten.
 * Si se inicia antes, fallará.
 */
public class ServidorBoot {

    public static void main(String[] args) {

        // 1. Definimos el cerrojo para 2 servicios (Base de Datos y Caché)
        final int SERVICIOS_TOTALES = 2;
        CountDownLatch latch = new CountDownLatch(SERVICIOS_TOTALES);

        System.out.println("INICIO DEL SISTEMA: Esperando a que carguen los servicios...");

        // Creamos e iniciamos los hilos de servicio
        new Thread(new Servicio("Base de Datos", 3000, latch)).start(); // Tarda 3 seg
        new Thread(new Servicio("Sistema de Caché", 1000, latch)).start(); // Tarda 1 seg

        try {
            // 2. El hilo principal se bloquea aquí. NO PASARÁ hasta que latch sea 0.
            latch.await();

            System.out.println("Todos los subsistemas han sido cargados correctamente.");
            System.out.println("Procedo a levantar el servidor.");
            Thread.sleep(2000);
            // Esta línea solo se ejecuta cuando ambos servicios han terminado
            System.out.println("¡TODOS LOS SISTEMAS LISTOS! Servidor levantado. Servidor aceptando peticiones.");

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    // Clase que simula un trabajo pesado
    private static class Servicio implements Runnable {
        private String nombre;
        private int tiempoDeCarga;
        private CountDownLatch latch;

        public Servicio(String nombre, int tiempoDeCarga, CountDownLatch latch) {
            Objects.requireNonNull(nombre,"ERROR: El nombre de los hilos no puede ser nulo.");
            Objects.requireNonNull(latch,"ERROR: El objeto latch de los hilos no puede ser nulo.");

            if (tiempoDeCarga<1)
                throw new IllegalArgumentException("ERROR: El tiempo de carga no puede ser inferior a 1.");

            if (nombre.isBlank())
                throw new IllegalArgumentException("ERROR: El nombre del hilo no puede ser vacío.");

            this.nombre = nombre;
            this.tiempoDeCarga = tiempoDeCarga;
            this.latch = latch;
        }

        @Override
        public void run() {

            try {

                System.out.println(" -> Iniciando " + nombre + "...");

                Thread.sleep(tiempoDeCarga); // Simulamos trabajo

                System.out.println(" -> " + nombre + " CARGADO");

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                // 3. ¡CRUCIAL! Decrementamos el contador pase lo que pase
                latch.countDown();
                System.out.println("(Cerrojo restante: " + latch.getCount() + ")");
            }
        }
    }
}



