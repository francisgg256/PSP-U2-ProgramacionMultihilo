package semaphore.terminalweb;

import java.util.concurrent.Semaphore;

/**Se trata de simular el acceso simultáneo de 4 terminales (hilos) a un servidor
 * y llevar la cuenta de accesos en cada instante. Desde cada terminal se
 * simularán 10 accesos.
 *
 */
public class Main {


    public static void main(String[] args) {

      Semaphore semaforo = new Semaphore(1);

      //semáforo para las secciones críticas de esta clase (permisos 1)
      ServidorWeb servidor = new ServidorWeb();
      
      //Se crean cuatro hilos
      HiloTerminal hterminal1 = new HiloTerminal("Cliente 1", servidor,semaforo);
      HiloTerminal hterminal2 = new HiloTerminal("Cliente 2", servidor,semaforo);
      HiloTerminal hterminal3 = new HiloTerminal("Cliente 3", servidor,semaforo);
      HiloTerminal hterminal4 = new HiloTerminal("Cliente 4", servidor,semaforo);


      //se inician los cuatro hilos
      hterminal1.start();
      hterminal2.start();
      hterminal3.start();
      hterminal4.start();



    }

}
