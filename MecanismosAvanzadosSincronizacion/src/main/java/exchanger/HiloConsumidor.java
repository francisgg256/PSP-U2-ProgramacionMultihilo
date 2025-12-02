package exchanger;

import java.util.Objects;
import java.util.concurrent.Exchanger;

/******************************************************************************
 * hilo Consumidor cuyo método run() repite indefinidamente el bucle:
 *
 *  - imprimir en la Salida la cadena de 10 caracteres recibida en último
 *    intercambio con el hilo Productor
 *  - cambiar con el Productor una cadena vacía por la que ha rellenado
 *
 * hasta que el Productor le proporcione una cadena vacía (señal de parada)
 *
 */
class HiloConsumidor extends Thread {

    //intercambiador de cadena
    private final Exchanger<String> intercambiadorCadena;
    
    //cadena recibida en el último intercambio
    private String cadena;
    
    // constructor
    HiloConsumidor(Exchanger<String> intercambiador) {
        Objects.requireNonNull(intercambiador, "ERROR: El exchanger de los hilos no puede ser nulo.");

        //intercambiador
        intercambiadorCadena = intercambiador;
    }

    /**************************************************************************
     * mientras que el Productor no envía la cadena vacía (señal de parada):
     *
     *  - imprimir en la Salida la cadena de 10 caracteres recibida en último
     *    intercambio con el hilo Productor
     *  - cambiar con el Productor una cadena vacía por la nueva rellena
     */
    @Override
    public void run() {

        //mientras que todavía no se ha realizado un intercambio, o ya se
        //realizado y la cadena no es la vacía
        while (cadena == null || cadena.length() > 0) {
            try {
                //llama a exhange(""), para intercambiar con el hilo Productor
                //una cadena vacía por otra rellena (esto bloquea la ejecución
                //del Consumidor hasta que el Productor está listo para realizar
                //el intercambio)
                cadena = intercambiadorCadena.exchange("");

                //si en el intercambio no se ha recibido la señal de parada
                if (cadena.length() > 0) {
                    //imprime la cadena en la Salida
                    System.out.println("Consumidor escribe " + cadena
                            + ", mientras Productor compone la siguiente...");
                }
                //imprime el resultado en la Salida
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
