package exchanger;

import java.util.Objects;
import java.util.concurrent.Exchanger;

/******************************************************************************
 * hilo Productor cuyo método run() ejecuta el bucle:
 * 
 *  - agregar 10 caracteres consecutivos a la cadena vacía proporcionada por
 *    el Consumidor
 *  - cambiar con el hilo Consumidor la cadena compuesta por otra vacía
 *
 * hasta que se recibe una llamada al método parada()
 * 
 */
class HiloProductor extends Thread {

    //intercambiador de objetos String
    private final Exchanger<String> intercambiadorCadena;
    
    private boolean continuar;

    //cadena
    private String cadena;
    

    /**************************************************************************
     * constructor del hilo que recibe como parámetro un intercambiador de
     * cadena
     *
     * @param c
     */
    HiloProductor(Exchanger<String> intercambiador) {
        Objects.requireNonNull(intercambiador, "ERROR: El exchanger de los hilos no puede ser nulo.");

        //intercambiador
        intercambiadorCadena = intercambiador;
        
        //cadena a vacía
        cadena = "";
        
        //continuar a verdadero
        continuar = true;
        
    }

    /**************************************************************************
     * mientras que no se llama al método parada(), ejecuta el bucle:
     *
     * - agregar 10 carácteres consecutivos a la cadena vacía proporcionada por
     *   el Consumidor
     * - cambiar con el hilo Consumidor la cadena compuesta por otra vacía
     *
     * cuando se llama a parada(), intercambia la cadena vacía con el
     * Consumidor (la señal de parada para el Consumidor)
     */
    @Override
    public void run() {

        //carácter tope
        final char chTope = 1 + 'Z';
        
        //carácter inicial
        char ch = 'A';
        

        //cadena a vacía
        cadena = "";

        //mientras no se indica parada
        while (continuar) {

            //agrega 10 caracteres consecutivos a la cadena vacía recibida en
            //el intercambio anterior
            for (int j = 0; j < 10; j++) {

                //agrega el carácter a la cadena
                cadena += (char)ch;
                ch ++;                

                //si llegó al tope
                if (ch == chTope) {
                    //empieza otra vez por 'A'
                    ch = 'A';
                }
            }

            try {
                //llama a exchange(str), para intercambiar con el hilo Consumidor
                //la cadena rellenada por otra vacía (esto bloquea la ejecución
                //del Productor hasta que el Consumidor está listo para realizar
                //el intercambio)
                cadena = intercambiadorCadena.exchange(cadena);

            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        //si se indicó parada
        try {
            //intercambia con el hilo Consumidor la cadena vacía (señal de
            //parada para el Consumidor), por otra vacía (que ya no se vuelve
            //a usar, porque el bucle ha finalizado)
            intercambiadorCadena.exchange(cadena);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**************************************************************************
     * método que ordena el fin del bucle
     *
     */
    public void parada() {
        //fin del bucle
        continuar = false;
    }
}
