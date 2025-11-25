package countdownlatch.busquedaatomica;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BusquedaAtomica {

    public static void main(String[] args) throws InterruptedException {

        int[] array = {3, 8, 15, 23, 42, 4, 9, 16, 23, 50};
        int numeroBuscado = 23;
        int numHilos = 3;

        // Variable segura para almacenar la posición encontrada
        AtomicInteger posicionEncontrada = new AtomicInteger(-1);

        // CountDownLatch para esperar a que todos los hilos terminen
        CountDownLatch latch = new CountDownLatch(numHilos);

        int tamanoSegmento = array.length / numHilos;

        for (int i = 0; i < numHilos; i++) {
            int inicio = i * tamanoSegmento;
            int fin = (i == numHilos - 1) ? array.length : inicio + tamanoSegmento;

            Thread hilo = new Thread(new Buscador(array, numeroBuscado, inicio, fin, posicionEncontrada, latch));
            hilo.start();
        }

        // Esperar a que todos los hilos terminen
        latch.await();

        // Mostrar resultado final
        if (posicionEncontrada.get() != -1) {
            System.out.println("Número encontrado en la posición: " + posicionEncontrada.get());
        } else {
            System.out.println("Número no encontrado.");
        }
    }

    private static class Buscador implements Runnable {

        private int[] array;
        private int numeroBuscado;
        private int inicio;
        private int fin;
        private AtomicInteger posicionEncontrada;
        private CountDownLatch latch;

        public Buscador(int[] array, int numeroBuscado, int inicio, int fin,
                        AtomicInteger posicionEncontrada, CountDownLatch latch) {
            this.array = array;
            this.numeroBuscado = numeroBuscado;
            this.inicio = inicio;
            this.fin = fin;
            this.posicionEncontrada = posicionEncontrada;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                for (int i = inicio; i < fin; i++) {

                    // Si ya fue encontrado por otro hilo, detiene la búsqueda
                    if (posicionEncontrada.get() != -1) {
                        break;
                    }

                    if (array[i] == numeroBuscado) {
                        posicionEncontrada.compareAndSet(-1, i);
                        break;
                    }
                }
            } finally {
                // Avisar que este hilo ya terminó
                latch.countDown();
            }
        }
    }
}
