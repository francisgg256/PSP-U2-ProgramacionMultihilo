package basicos.jardinesnosincronizado;


import java.util.Objects;

public class EntraJardin extends Thread {

    private Jardin jardin;

    public EntraJardin(String nombre, Jardin jardin) {
        Objects.requireNonNull(jardin,"ERROR: El objeto jardin no puede ser nulo.");
        Objects.requireNonNull(nombre,"ERROR: El nombre de un hilo no puede ser nulo.");

        if (nombre.isBlank())
            throw new IllegalArgumentException("ERROR: El nombre de un hilo no puede ser vacío.");

        this.setName(nombre);
        this.jardin = jardin;
    }


    @Override
    public void run() {
        //invoca al metodo que incrementa la cuenta de accesos al jardín
        jardin.incrementaCuenta();
    }
}
