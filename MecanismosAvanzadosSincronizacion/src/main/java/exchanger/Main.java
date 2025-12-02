package exchanger;

import java.util.concurrent.Exchanger;

/******************************************************************************
 * el hilo principal de esta aplicación, crea dos nuevos hilos con un punto
 * de sincronización tipo Exchanger
 * 
 * el denominado hilo productor se encarga de rellenar una cadena de diez 
 * caracteres (o lista de 10 caracteres)
 * 
 * el denominado hilo consumidor imprime esa lista
 *
 * dejaremos que ambos hilos funcionan alrededor de un segundo, antes de
 * enviarles la orden de parada
 *
 */
public class Main {

    /**************************************************************************
     * crea dos hilos que se intercambiarán una cadena
     */
    public static void main(String args[]) {
        //intercambiador para una cadena
        Exchanger<String> exchanger = new Exchanger<String>();
        
        //crea el hilo productor
        HiloProductor productor = new HiloProductor(exchanger);
        
        //inicia hilo productor
        productor.start();
        
        //crea e inicia el hilo consumidor
        (new HiloConsumidor(exchanger)).start();


        try {
            //duerme al hilo principal un segundo
            //con objeto de dar tiempo a que los hilos intercambien las cadenas
            Thread.sleep(1000);            
        } catch (InterruptedException ex) {
        }

        // se da la orden de parada al productor (que a su vez se la enviará al
        // consumidor en el siguiente intercambio)
        productor.parada();
        
    }
}
