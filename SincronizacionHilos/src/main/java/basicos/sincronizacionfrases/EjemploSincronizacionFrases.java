package basicos.sincronizacionfrases;

public class EjemploSincronizacionFrases {
    public static void main(String[] args) {
//        Thread th1=new Thread(new ImprimirFrasesNoSincronizadas("Hola ", "que tal!"));
//        Thread th2=new Thread(new ImprimirFrasesNoSincronizadas("¿Quién eres ", "tú?"));
//        Thread th3=new Thread(new ImprimirFrasesNoSincronizadas("Muchas ", "gracias amigo!"));

        Thread th1=new Thread(new ImprimirFrasesSincronizadas("Hola ", "que tal!"));
        Thread th2=new Thread(new ImprimirFrasesSincronizadas("¿Quién eres ", "tú?"));
        Thread th3=new Thread(new ImprimirFrasesSincronizadas("Muchas ", "gracias amigo!"));

        th1.start();
        th2.start();

        //Para ver el estado TIMED_WAITING/BLOCKED de la hebra th3
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        th3.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(th3.getState());

    }


}
