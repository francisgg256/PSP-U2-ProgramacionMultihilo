package productoresconsumidores.fabricapeligrosa;

public class FabricaPeligrosa {
    // Recurso compartido
    private static boolean estaLleno = false;
    private static final Object cerrojo = new Object();

    public static void main(String[] args) {
        // Creamos varios productores y consumidores para aumentar la probabilidad de caos
        for (int i = 0; i < 2; i++) {
            new Thread(new Productor(), "Productor-" + i).start();
            new Thread(new Consumidor(), "Consumidor-" + i).start();
        }
    }

    static class Productor implements Runnable {
        public void run() {
            while (true) {
                synchronized (cerrojo) {
                    while (estaLleno) {
                        try {
                            // Si está lleno, espero
                            cerrojo.wait();
                        } catch (InterruptedException e) { return; }
                    }

                    // Producir
                    estaLleno = true;
                    System.out.println(Thread.currentThread().getName() + " puso un paquete.");

                    // PELIGRO: Usamos notify().
                    // Si despertamos a OTRO Productor por error, este verá que está lleno
                    // y se volverá a dormir. La señal se perderá y los consumidores seguirán durmiendo.
                    cerrojo.notify();
                }
            }
        }
    }

    static class Consumidor implements Runnable {
        public void run() {
            while (true) {
                synchronized (cerrojo) {
                    while (!estaLleno) {
                        try {
                            // Si está vacío, espero
                            cerrojo.wait();
                        } catch (InterruptedException e) { return; }
                    }

                    // Consumir
                    estaLleno = false;
                    System.out.println(Thread.currentThread().getName() + " consumió paquete.");

                    // PELIGRO: Usamos notify().
                    // Si despertamos a OTRO Consumidor por error, verá que está vacío
                    // y se volverá a dormir. Nadie avisará a los productores.
                    cerrojo.notify();
                }
            }
        }
    }
}
