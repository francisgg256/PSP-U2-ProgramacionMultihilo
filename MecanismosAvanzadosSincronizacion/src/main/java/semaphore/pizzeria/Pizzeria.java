package semaphore.pizzeria;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Pizzeria {
    public static void main(String[] args) {
        // EL BUFFER (Mostrador)
        Queue<String> pizzasTerminadas = new LinkedList<>();
        final int CAPACIDAD_MAXIMA = 5;

        // SEMÁFOROS
        // 1. Controla el espacio. Empieza con 5 permisos (hay 5 huecos).
        Semaphore horno = new Semaphore(CAPACIDAD_MAXIMA);

        // 2. Controla el producto. Empieza con 0 permisos (no hay pizzas).
        Semaphore mostrador = new Semaphore(0);

        // Un objeto extra para asegurar que nadie toque la lista a la vez (Mutex)
        // Esto es necesario porque LinkedList no es Thread-Safe por sí misma
        Object cerrojoMostrador = new Object();

        // --- HILO PRODUCTOR (EL PIZZERO) ---
        Thread pizzero = new Thread(() -> {
            int idPizza = 1;
            while (true) {
                try {
                    // A. ¿Hay espacio para cocinar?
                    // Si es 0, el pizzero se duerme esperando hueco.
                    horno.acquire();

                    // B. Sección Crítica (Poner la pizza)
                    Thread.sleep(1000); // Tiempo de cocinado
                    String pizza = "Pizza " + idPizza++;

                    synchronized (cerrojoMostrador) {
                        pizzasTerminadas.add(pizza);
                        System.out.println("COCINADO: " + pizza + " | Stock actual: " + pizzasTerminadas.size());
                    }

                    // C. ¡AVISAR AL REPARTIDOR!
                    // Incrementamos el contador de pizzas listas.
                    // Si el repartidor estaba dormido esperando, esto lo despierta.
                    try
                    {

                    }
                    finally {
                        mostrador.release();

                    }

                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        // --- HILO CONSUMIDOR (EL REPARTIDOR) ---
        Thread repartidor = new Thread(() -> {
            while (true) {
                try {
                    // A. ¿Hay pizzas para llevar?
                    // Si es 0, el repartidor se duerme esperando pizzas.
                    mostrador.acquire();

                    // B. Sección Crítica (Coger la pizza)
                    Thread.sleep(2000); // Tiempo de reparto (es más lento que cocinar)
                    String pizza;

                    synchronized (cerrojoMostrador) {
                        pizza = pizzasTerminadas.poll();
                        System.out.println("ENTREGADO: " + pizza + " | Stock actual: " + pizzasTerminadas.size());
                    }

                    // C. ¡AVISAR AL PIZZERO!
                    // Se ha liberado un hueco en el pizzasTerminadas.
                    // Incrementamos el contador de espacios vacíos.
                    try
                    {

                    }
                    finally {
                        horno.release();
                    }


                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        pizzero.start();
        repartidor.start();
    }
}
