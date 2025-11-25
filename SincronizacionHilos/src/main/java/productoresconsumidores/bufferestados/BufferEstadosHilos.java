package productoresconsumidores.bufferestados;

import java.util.LinkedList;
import java.util.Queue;

public class BufferEstadosHilos {

    public static void main(String[] args) throws InterruptedException {
        BufferDidactico buffer = new BufferDidactico();

        Thread productor = new Thread(() -> {
            for (int i = 0; i < 2; i++) { // Solo 2 iteraciones para verlo claro
                buffer.producir(i);
            }
        }, "Productor");

        Thread consumidor = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                buffer.consumir();
            }
        }, "Consumidor");

        HiloMonitor monitor = new HiloMonitor(productor, consumidor);

        // 1. Iniciamos el monitor
        monitor.start();

        // 2. Iniciamos Productor y esperamos un poco para asegurar que llene y espere
        productor.start();
        Thread.sleep(200); // Dejamos que el productor llene el hueco y termine su turno

        // 3. Iniciamos Consumidor
        consumidor.start();
    }

    // 1. El Buffer compartido (El Recurso Crítico)
    private static class BufferDidactico {
        private final Queue<Integer> cola = new LinkedList<>();
        private final int CAPACIDAD = 1; // Buffer de tamaño 1 para bloquear rápido
        private final Object cerrojo = new Object();

        public void producir(int valor) {
            synchronized (cerrojo) {
                // 1. Si está lleno, a dormir (WAITING)
                while (cola.size() == CAPACIDAD) {
                    try {
                        System.out.println("Buffer lleno. Productor hace wait()...");
                        cerrojo.wait();
                    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                // 2. Producir
                cola.add(valor);
                System.out.println("Productor puso: " + valor);

                // TRUCO MAESTRO:
                // Antes de avisar al consumidor, dormimos un poco manteniendo el candado.
                // Si el Consumidor estaba WAITING, el Monitor lo verá así durante este tiempo.
                simularTrabajoLento(1000);

                cerrojo.notifyAll();
            }
        }

        public void consumir() {
            synchronized (cerrojo) {
                // 1. Si está vacío, a dormir (WAITING)
                while (cola.isEmpty()) {
                    try {
                        System.out.println("Buffer vacío. Consumidor hace wait()...");
                        cerrojo.wait();
                    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

                // 2. Consumir
                int val = cola.poll();
                System.out.println("Consumidor quitó: " + val);

                // TRUCO MAESTRO:
                // Dormimos manteniendo el candado ANTES de notificar.
                // Esto garantiza que el Productor (que está esperando hueco)
                // aparezca como WAITING en el monitor durante 1 segundo entero.
                simularTrabajoLento(1000);

                cerrojo.notifyAll();
            }
        }

        private void simularTrabajoLento(int ms) {
            try {
                // OJO: Thread.sleep NO suelta el candado synchronized
                Thread.sleep(ms);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    // 2. El Hilo Monitor (El Espía)
    private static class HiloMonitor extends Thread {
        private final Thread t1;
        private final Thread t2;

        public HiloMonitor(Thread t1, Thread t2) {
            this.t1 = t1;
            this.t2 = t2;
            setDaemon(true);
        }

        @Override
        public void run() {
            System.out.println("Monitor: ACTIVADO");
            Thread.State lastS1 = null, lastS2 = null;

            while (t1.isAlive() || t2.isAlive()) {
                Thread.State s1 = t1.getState();
                Thread.State s2 = t2.getState();

                // Imprimimos SIEMPRE que detectemos WAITING o BLOCKED para asegurarnos de verlo
                if (s1 != lastS1 || s2 != lastS2 || s1 == Thread.State.WAITING || s2 == Thread.State.WAITING) {

                    // Solo imprimimos si hubo cambio real o si es el estado que buscamos desesperadamente
                    if (s1 != lastS1 || s2 != lastS2) {
                        System.out.printf("FOTO ESTADO -> Prod: %-15s | Cons: %-15s%n", s1, s2);
                        lastS1 = s1;
                        lastS2 = s2;
                    }
                }

                // Muestreo rápido (50ms)
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
        }
    }
}
