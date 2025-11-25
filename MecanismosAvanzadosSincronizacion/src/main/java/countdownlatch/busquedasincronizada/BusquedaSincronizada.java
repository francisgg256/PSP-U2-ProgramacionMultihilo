package countdownlatch.busquedasincronizada;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class BusquedaSincronizada {

    private static class ResultadoBusqueda {
        private int posicion = -1;

        public synchronized void setPosicionSiNoExiste(int nuevaPosicion) {
            if (posicion == -1) {
                posicion = nuevaPosicion;
            }
        }

        public synchronized int getPosicion() {
            return posicion;
        }
    }

    private static class Buscador implements Runnable {

        private int[] array;
        private int numeroBuscado;
        private int inicio;
        private int fin;
        private ResultadoBusqueda resultado;
        private CountDownLatch latch;

        public Buscador(int[] array, int numeroBuscado, int inicio, int fin,
                        ResultadoBusqueda resultado, CountDownLatch latch) {
            Objects.requireNonNull(resultado,"ERROR: El objeto resultado no puede ser nulo.");
            Objects.requireNonNull(latch,"ERROR: El objeto latch del hilo no puede ser nulo.");

            if (array.length==0)
                throw new IllegalArgumentException("ERROR: El array de búsqueda está vacío.");

            if (inicio<0 || fin<0)
                throw new IllegalArgumentException("ERROR: La posición inicial y/o final de búsqueda no puede ser negativa.");

            this.array = array;
            this.numeroBuscado = numeroBuscado;
            this.inicio = inicio;
            this.fin = fin;
            this.resultado = resultado;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                for (int i = inicio; i < fin; i++) {

                    // Si otro hilo ya lo encontró, detiene la búsqueda
                    if (resultado.getPosicion() != -1) {
                        break;
                    }

                    if (array[i] == numeroBuscado) {
                        resultado.setPosicionSiNoExiste(i);
                        break;
                    }
                }
            } finally {
                latch.countDown(); // Avisar que el hilo terminó
            }
        }
    }

    public static void main(String[] args)  {

        int[] array = {3, 8, 15, 23, 42, 4, 9, 16, 23, 50};
        int numeroBuscado = 23;
        int numHilos = 3;

        // Objeto seguro para almacenar la posición encontrada
        ResultadoBusqueda resultado = new ResultadoBusqueda();

        // CountDownLatch para esperar a todos los hilos
        CountDownLatch latch = new CountDownLatch(numHilos);

        int tamanoSegmento = array.length / numHilos;

        for (int i = 0; i < numHilos; i++) {
            int inicio = i * tamanoSegmento;
            int fin = (i == numHilos - 1) ? array.length : inicio + tamanoSegmento;

            Thread hilo = new Thread(new Buscador(array, numeroBuscado, inicio, fin, resultado, latch));
            hilo.start();
        }

        // Esperar a que todos los hilos terminen
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Mostrar resultado final
        if (resultado.getPosicion() != -1) {
            System.out.println("Número encontrado en la posición: " + resultado.getPosicion());
        } else {
            System.out.println("Número no encontrado.");
        }
    }
}
