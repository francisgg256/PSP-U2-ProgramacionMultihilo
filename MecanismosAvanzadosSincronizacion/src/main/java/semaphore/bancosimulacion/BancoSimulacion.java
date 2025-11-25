package semaphore.bancosimulacion;

import java.util.concurrent.Semaphore;

//Hay 10 clientes que quieren acceder a un cajero cuando solo existen tres cajeros
public class BancoSimulacion {

    // 1. Definimos el Semáforo con 3 permisos (recursos disponibles)
    private static final Semaphore cajeros = new Semaphore(3,true);

    public static void main(String[] args) {
        // Creamos 10 clientes (hilos)
        for (int i = 1; i <= 10; i++) {
            new Thread(new Cliente(i)).start();
        }
    }

    static class Cliente implements Runnable {
        private int id;

        public Cliente(int id) {
            if (id<=0)
                throw new IllegalArgumentException("ERROR: El identificador del cliente no puede ser menor que 1.");

            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("Cliente " + id + " llegando al banco...");

                // 2. INTENTO DE ADQUIRIR PERMISO (Bloqueante)
                // Si no hay permisos, el hilo se detiene aquí.
                cajeros.acquire();

                System.out.println("Cliente " + id + " está usando un cajero.");

                // Simulemos que tarda un tiempo en operar
                Thread.sleep(4000);

                System.out.println("Cliente " + id + " ha terminado.");

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                // 3. LIBERACIÓN DEL PERMISO (CRUCIAL)
                // Siempre, SIEMPRE en el bloque finally.
                System.out.println("Cliente " + id + " libera el cajero y sale.");
                cajeros.release();
            }
        }
    }
}
