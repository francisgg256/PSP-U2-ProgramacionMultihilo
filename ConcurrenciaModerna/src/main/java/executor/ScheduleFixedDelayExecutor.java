package executor;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleFixedDelayExecutor {
    public static void main(String[] args) {
        // 1. Creamos un Pool Programado
        // Usamos 2 hilos para que si el backup es lento, no bloquee el chequeo de salud
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        System.out.println("--- Sistema Iniciado a las " + LocalTime.now() + " ---");

        // CASO 1: Tarea única retardada (schedule)
        // "Dentro de 2 segundos, borra los logs temporales"
        scheduler.schedule(() -> {
            System.out.println("🧹 [Tarea Única] Limpiando logs temporales... " + LocalTime.now());
        }, 2, TimeUnit.SECONDS);


        // CASO 2: Frecuencia Fija (scheduleAtFixedRate)
        // "Revisa el pulso cada 3 segundos exactos"
        // Ideal para relojes, animaciones o mediciones que deben ser constantes.
        Runnable tareaPulso = () -> {
            System.out.println("[FixedRate] Pulso del sistema. " + LocalTime.now());
            // Simulamos que es rápido
            dormir(500);
        };
        // Inicia ya (0s) y repite cada 3s
        scheduler.scheduleAtFixedRate(tareaPulso, 0, 3, TimeUnit.SECONDS);


        // CASO 3: Retardo Fijo (scheduleWithFixedDelay)
        // "Haz un backup, y cuando termines, descansa 4 segundos antes del siguiente"
        // Ideal para operaciones I/O, descargas o tareas de duración impredecible.
        Runnable tareaBackup = () -> {
            System.out.println("[FixedDelay] Iniciando Backup pesado... " + LocalTime.now());
            // Simulamos que el backup tarda 2 segundos
            dormir(2000);
            System.out.println("✅ [FixedDelay] Backup finalizado. " + LocalTime.now());
        };
        // Inicia en 1s, espera 4s ENTRE el fin de uno y el inicio del siguiente
        scheduler.scheduleWithFixedDelay(tareaBackup, 1, 4, TimeUnit.SECONDS);


        // Para el ejemplo, dejamos correr el programa 15 segundos y luego cerramos
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- Apagando Planificador ---");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    // Método auxiliar para no ensuciar el código con try-catch
    private static void dormir(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }
}
