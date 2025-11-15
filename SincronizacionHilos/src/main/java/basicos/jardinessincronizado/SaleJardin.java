/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicos.jardinessincronizado;

import java.util.Objects;

public class SaleJardin extends Thread {
    private Jardin jardin;

    public SaleJardin(String nombre, Jardin jardin) {
        Objects.requireNonNull(jardin,"ERROR: El objeto jardin no puede ser nulo.");
        Objects.requireNonNull(nombre,"ERROR: El nombre de un hilo no puede ser nulo.");

        if (nombre.isBlank())
            throw new IllegalArgumentException("ERROR: El nombre de un hilo no puede ser vacío.");

        this.setName(nombre);
        this.jardin = jardin;
    }

    @Override
    public void run() {
        //invoca al metodo que decrementa la cuenta de accesos al jardín
        jardin.decrementaCuenta();
    }
}

