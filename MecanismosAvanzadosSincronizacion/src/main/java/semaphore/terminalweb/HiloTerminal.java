package semaphore.terminalweb;

import java.util.Objects;
import java.util.concurrent.Semaphore;


public class HiloTerminal extends Thread {
//clase derivada de Thread que define un hilo

    private ServidorWeb servidor;
    private Semaphore semaforo;
    private String nombre;

    public HiloTerminal(String nombre, ServidorWeb servidor, Semaphore semaforo) {

        Objects.requireNonNull(servidor,"ERROR: El objeto servidor no puede ser nulo.");
        Objects.requireNonNull(semaforo,"ERROR: El semáforo no puede ser nulo.");
        Objects.requireNonNull(nombre,"ERROR: El nombre del hilo no puede ser nulo.");

        setName(nombre);
        this.servidor = servidor;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        //la tarea del hilo es invocar a incrementaCuenta()simulando un acceso
        // al servidor

        for (int i = 1; i <= 10; i++) //se simulan 10 accesos al servidor
        {
            try {
                semaforo.acquire();
                //en cada acceso se adquiere el recurso 
                //y si está ocupado se bloquea
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            try{
                servidor.incrementaCuenta();
            }
            finally {
                //adquirido el recurso, invoca a este método para simular el acceso
                //al servidor incrementado la cuenta de accesos

                semaforo.release();
            }


        }



    }
}
