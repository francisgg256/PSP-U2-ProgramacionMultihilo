package cyclicbarrier.transaccionbancaria;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TransaccionBancaria {
    public static void main(String[] args) {
        // Queremos sincronizar 3 subsistemas
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("ÉXITO: Todos los sistemas validados. Se realiza Commit de la transacción.");
        });

        // Hilo 1: Sistema de Saldo (Funciona bien)
        new Thread(new SubSistema(barrier, "Saldo", false)).start();

        // Hilo 2: Base de Datos (Funciona bien)
        new Thread(new SubSistema(barrier, "BaseDatos", false)).start();

        // Hilo 3: Sistema de Fraude (SIMULAREMOS UN FALLO AQUÍ)
        // Pasamos 'true' para indicar que este hilo fallará
        new Thread(new SubSistema(barrier, "Fraude-Check", true)).start();
    }

    static class SubSistema implements Runnable {
        private CyclicBarrier barrier;
        private String name;
        private boolean soyErroneo;

        public SubSistema(CyclicBarrier barrier, String name, boolean soyErroneo) {
            Objects.requireNonNull(barrier, "ERROR: El barrier de los hilos no puede ser nulo.");
            Objects.requireNonNull(name, "ERROR: El nombre de los hilos no puede ser nulo.");

            if (name.isBlank())
                throw new IllegalArgumentException("ERROR: El nombre de un hilo no puede ser vacío.");

            this.barrier = barrier;
            this.name = name;
            this.soyErroneo = soyErroneo;
        }

        @Override
        public void run() {
            System.out.println("[" + name + "] Iniciando validación...");

            try {
                // Simulamos trabajo
                Thread.sleep(1000);

                if (soyErroneo) {
                    System.err.println(" [" + name + "] ERROR CRÍTICO DETECTADO. Abortando...");
                    // Al lanzar una RuntimeException o interrumpir, no llegamos al await().
                    // Ojo: Si el hilo muere antes de llegar al await, los otros se quedarían esperando
                    // ETERNAMENTE si no usamos timeouts o gestión de interrupciones.

                    // Para romper la barrera explícitamente y avisar a los otros:
                    //Thread.currentThread().interrupt();
                    // O simplemente no llamar a await() y dejar que los otros tengan timeout.
                    throw new RuntimeException("Fallo interno");
                }
                else {
                    System.out.println("[" + name + "] Validación OK. Esperando en barrera...");

                    // Usamos un Timeout por seguridad.
                    // Si en 2 segundos no llegan todos (porque uno falló), lanzamos excepción.
                    barrier.await(2, TimeUnit.SECONDS);

                    System.out.println("[" + name + "] -> Transacción finalizada correctamente.");
                }


            } catch (InterruptedException e) {
                // Ocurre si este hilo fue interrumpido
                System.out.println("[" + name + "] Interrumpido. Rollback local.");
            } catch (TimeoutException e) {
                // Ocurre si esperamos demasiado a los otros
                System.out.println("[" + name + "] Timeout esperando a los otros. Rollback local.");
            } catch (BrokenBarrierException e) {
                // --- AQUÍ ESTÁ LA CLAVE DE LA RECUPERACIÓN ---
                // Ocurre si OTRO hilo rompió la barrera o falló.
                System.err.println(" [" + name + "] ¡La barrera se rompió! Otro sistema falló. HACIENDO ROLLBACK.");
                performRollback();
            } catch (RuntimeException e) {
                // Capturamos el error simulado
            }
        }

        private void performRollback() {
            // Lógica real de deshacer cambios
            System.out.println("[" + name + "] Revirtiendo cambios en base de datos...");
        }
    }
}
