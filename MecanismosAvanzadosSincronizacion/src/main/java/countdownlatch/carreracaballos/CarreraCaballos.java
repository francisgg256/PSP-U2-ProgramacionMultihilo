package countdownlatch.carreracaballos;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class CarreraCaballos {

    public static void main(String[] args) {
        int numCorredores = 5;

        // El cerrojo es de 1. Todos esperan UNA señal.
        CountDownLatch latch = new CountDownLatch(1);

        System.out.println("Juez: ¡Corredores a sus puestos!");

        for (int i = 1; i <= numCorredores; i++) {
            new Thread(new Corredor(latch, i)).start();
        }

        try {
            Thread.sleep(2000); // El juez se toma un segundo para preparar la pistola
            System.out.println("Juez: ¡PREPARADOS... LISTOS...!");

        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        finally
        {
            // ¡PUM! Bajamos el contador a 0. Todos los hilos se liberan a la vez.
            System.out.println("Juez: ¡YA!");
            latch.countDown();
        }


    }

    private static class Corredor implements Runnable {
        private CountDownLatch latch;
        private int dorsal;

        public Corredor(CountDownLatch latch, int dorsal) {
            Objects.requireNonNull(latch,"ERROR: El latch de los hilos no puede ser nulo.");

            if (dorsal<0)
                throw new IllegalArgumentException("ERROR: El dorsal no puede ser negativo.");

            this.latch = latch;
            this.dorsal = dorsal;
        }

        @Override
        public void run() {
            try {
                System.out.println("Corredor " + dorsal + " en posición.");
                // El corredor se queda aquí esperando el disparo
                latch.await();

                System.out.println("¡Corredor " + dorsal + " ha salido disparado!");

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
