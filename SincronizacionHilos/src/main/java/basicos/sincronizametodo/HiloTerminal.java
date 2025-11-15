package basicos.sincronizametodo;


import java.util.Objects;

public class HiloTerminal extends Thread {

    private ServidorWeb servidor;

    public HiloTerminal(String nombre, ServidorWeb servidor) {
        Objects.requireNonNull(servidor,"ERROR: El objeto servidor no puede ser nulo.");
        Objects.requireNonNull(nombre,"ERROR: El nombre de un hilo no puede ser nulo.");

        if (nombre.isBlank())
            throw new IllegalArgumentException("ERROR: El nombre de un hilo no puede ser vacío.");

        setName(nombre);
        this.servidor = servidor;
    }

    @Override
    public void run() {
        //metodo que incrementa la cuenta de accesos
        for (int i = 1; i <= 10; i++) //se simulan 10 accesos
        {
                servidor.incrementaCuenta();
         }
    }
}
