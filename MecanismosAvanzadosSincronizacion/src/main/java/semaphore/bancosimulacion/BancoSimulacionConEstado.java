package semaphore.bancosimulacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class BancoSimulacionConEstado {
    // Limitamos a 2 cajeros para forzar que haya cola y veamos el estado WAITING rápido
    private static final Semaphore cajeros = new Semaphore(2);

    public static void main(String[] args) {
        // Guardamos las referencias a los hilos para poder espiarlos (ver sus estados)
        List<Thread> listaHilos = new ArrayList<>();

        System.out.println("--- INICIO DE LA SIMULACIÓN ---");

        // 1. Creamos y lanzamos los clientes
        for (int i = 1; i <= 5; i++) {
            Thread t = new Thread(new Cliente(i), "Cliente-" + i);
            listaHilos.add(t);
            t.start();
        }

        // 2. Lanzamos el Hilo Monitor (El Espía)
        Monitor monitor = new Monitor(listaHilos);
        monitor.start();
    }

    // --- LA LÓGICA DEL CLIENTE ---
    private static class Cliente implements Runnable {
        private final int id;

        public Cliente(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Simulamos un pequeño retraso aleatorio al llegar para que no lleguen todos a la vez exacta
                System.out.println("Cliente " + id + " llegando al banco...");
                Thread.sleep((long) (Math.random() * 100));

                // INTENTO DE ENTRAR (Punto crítico de bloqueo)
                cajeros.acquire();

                System.out.println(Thread.currentThread().getName() + " entró al cajero.");

                // Simulamos la operación (tarda 2 segundos)
                // Durante este tiempo el estado será TIMED_WAITING
                Thread.sleep(4000);

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                System.out.println(Thread.currentThread().getName() + " sale del cajero.");
                cajeros.release();
            }
        }
    }

    // --- LA LÓGICA DEL MONITOR (EL ESPÍA) ---
    private static class Monitor extends Thread {
        private final List<Thread> hilosObservados;

        public Monitor(List<Thread> hilos) {
            Objects.requireNonNull(hilos,"ERROR: La lista de hilos no puede ser nula.");

            this.hilosObservados = hilos;
            // Lo hacemos Daemon para que si los clientes terminan, este hilo muera automáticamente
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // Hacemos una pausa para no saturar la consola
                    Thread.sleep(600);

                    System.out.println("\nESTADO ACTUAL DEL BANCO:");
                    boolean todosTerminados = true;

                    for (Thread t : hilosObservados) {
                        // Aquí capturamos el estado actual
                        Thread.State estado = t.getState();

                        String explicacion = "";
                        switch (estado) {
                            case WAITING: explicacion = "(Esperando semáforo - Cola)"; break;
                            case TIMED_WAITING: explicacion = "(Dentro del cajero operando)"; break;
                            case RUNNABLE: explicacion = "(Moviéndose/Procesando)"; break;
                            case TERMINATED: explicacion = "(Ya se fue)"; break;
                            default: explicacion = "(" + estado + ")";
                        }

                        System.out.printf("   %-10s -> %s %s%n", t.getName(), estado, explicacion);

                        if (estado != Thread.State.TERMINATED) {
                            todosTerminados = false;
                        }
                    }
                    System.out.println("--------------------------------");

                    if (todosTerminados) {
                        System.out.println("Todos los clientes han terminado. Monitor cerrando.");
                        break;
                    }

                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
