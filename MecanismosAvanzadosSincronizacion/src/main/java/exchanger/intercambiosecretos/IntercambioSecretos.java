package exchanger.intercambiosecretos;

import java.util.concurrent.Exchanger;

public class IntercambioSecretos {
    public static void main(String[] args) {
        // 1. Creamos el Exchanger. Tipado para Strings.
        Exchanger<String> exchanger = new Exchanger<>();

        // 2. Definimos la tarea del Agente A
        Runnable tareaAgenteA = () -> {
            try {
                String miMaletin = "Código Nuclear de A";
                System.out.println("Agente A: Tengo -> " + miMaletin + ". Esperando en el puente...");

                // AQUI OCURRE LA MAGIA: Se bloquea hasta que llegue B
                String recibido = exchanger.exchange(miMaletin);

                System.out.println("Agente A: ¡Intercambio exitoso! Ahora tengo -> " + recibido);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        };

        // 3. Definimos la tarea del Agente B
        Runnable tareaAgenteB = () -> {
            try {
                String miMaletin = "Planos de la Base de B";
                // Simulamos que el Agente B tarda un poco más en llegar (sleep)
                Thread.sleep(5000);
                System.out.println("Agente B: He llegado con -> " + miMaletin);

                String recibido = exchanger.exchange(miMaletin);

                System.out.println("Agente B: ¡Intercambio exitoso! Ahora tengo -> " + recibido);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        };

        // 4. Iniciamos los hilos
        new Thread(tareaAgenteA).start();
        new Thread(tareaAgenteB).start();
    }
}
