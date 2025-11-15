package basicos.sincronizacionmultiple;

public class ProblemaSincronizacionMultipleConEstado {

    public static int contador1 = 0;
    public static int contador2 = 0;

    // Métodos static synchronized → bloquean en ProblemaSincronizacionMultiple.class
    public static synchronized void incrementar1() {

        String name = Thread.currentThread().getName();

        System.out.println(name + " → entrando en incrementar1 (adquiere el lock).");
        // Simula trabajo mientras se mantiene el lock para que otro hilo quede BLOCKED
        try
        {
            Thread.sleep(200); // 200ms con el lock
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        contador1++;

        System.out.println(name + " → saliendo de incrementar1 (libera el lock).");
    }

    public static synchronized void incrementar2() {

        String name = Thread.currentThread().getName();
        System.out.println(name + " → entrando en incrementar2 (adquiere el lock).");

        // Simula trabajo mientras se mantiene el lock para que otro hilo quede BLOCKED
        try
        {
            Thread.sleep(200); // 200ms con el lock
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        contador2++;

        System.out.println(name + " → saliendo de incrementar2 (libera el lock).");
    }

    public static void main(String[] args) {
        // Usamos pocas iteraciones para que las pausas sean observables en consola
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                incrementar1();
                // pequeña pausa fuera del lock para dar oportunidad de intercalar
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                incrementar2();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "t2");

        // Monitor que imprime el estado de los hilos hasta que ambos terminen
        Thread monitor = new Thread(() -> {
            while (true) {

                Thread.State estadoHilo1 = t1.getState();
                Thread.State estadoHilo2 = t2.getState();

                System.out.println("[Monitor Estados -] t1: " + estadoHilo1 + " | t2: " + estadoHilo2 );

                //Nos salimos del bucle while cuando ambos hilos hayan terminado
                if (estadoHilo1 == Thread.State.TERMINATED && estadoHilo2 == Thread.State.TERMINATED) break;

                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("[Monitor Estados - ] Ambos hilos TERMINATED. Monitor sale.");

        }, "monitor");

        t1.start();
        t2.start();
        monitor.setDaemon(false); // queremos que termine antes de imprimir resultados finales
        monitor.start();

        try {
            t1.join();
            t2.join();
            monitor.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Valor de contador1: " + contador1);
        System.out.println("Valor de contador2: " + contador2);
    }
}
