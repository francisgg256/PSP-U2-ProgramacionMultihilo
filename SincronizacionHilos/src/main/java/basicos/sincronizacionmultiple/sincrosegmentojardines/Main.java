package basicos.sincronizacionmultiple.sincrosegmentojardines;

public class Main {


    public static void main(String[] args) {
        Jardin jardin = new Jardin();

        for (int i = 1; i <= 10; i++) {
            (new EntraJardin("Asistente " + i, jardin)).start();
        }//crea e inicia 10 hilos que simulan entrada al jardín

        for (int i = 1; i <= 15; i++) {
            (new SaleJardin("Asistente " + i, jardin)).start();
        }//crea e inicia 15 hilos que simulan salida del jardín
    }
}

