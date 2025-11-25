package countdownlatch.busquedasincronizada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class BusquedaTodasPosicionesSincronizada {

    // Clase sincronizada que guarda todas las posiciones
    private static class ResultadoBusqueda
    {
        //private final List<Integer> posiciones = Collections.synchronizedList(new ArrayList<>());
        private final List<Integer> posiciones = new ArrayList<>();

        // Añadir una posición de forma sincronizada
        public synchronized void addPosicion(int pos) {
            posiciones.add(pos);
        }

        // Obtener una copia de las posiciones ordenadas
        public synchronized List<Integer> getPosicionesOrdenadas() {
            List<Integer> copia = new ArrayList<>(posiciones);
            Collections.sort(copia);
            return copia;
        }

        // Número de coincidencias
        public synchronized int size() {
            return posiciones.size();
        }
    }

    private static class Buscador implements Runnable {

        private final int[] array;
        private final int numeroBuscado;
        private final int inicio;
        private final int fin;
        private final ResultadoBusqueda resultado;
        private final CountDownLatch latch;

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
                    if (array[i] == numeroBuscado) {
                        resultado.addPosicion(i);
                    }
                }
            } finally {
                latch.countDown();
            }
        }
    }

    public static void main(String[] args)  {

        int[] array = {3, 23, 15, 23, 42, 4, 9, 23, 23, 50, 23};
        int numeroBuscado = 23;
        int numHilos = 3;

        ResultadoBusqueda resultado = new ResultadoBusqueda();
        CountDownLatch latch = new CountDownLatch(numHilos);

        int tamanoSegmento = array.length / numHilos;

        for (int i = 0; i < numHilos; i++) {
            int inicio = i * tamanoSegmento;
            int fin = (i == numHilos - 1) ? array.length : inicio + tamanoSegmento;

            Thread hilo = new Thread(new Buscador(array, numeroBuscado, inicio, fin, resultado, latch));
            hilo.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        List<Integer> posiciones = resultado.getPosicionesOrdenadas();
        if (posiciones.isEmpty()) {
            System.out.println("Número no encontrado.");
        } else {
            System.out.println("Número encontrado en las posiciones: " + posiciones);
        }
    }
}
