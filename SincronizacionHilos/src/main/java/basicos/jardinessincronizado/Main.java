package basicos.jardinessincronizado;

public class Main {


    public static void main(String[] args) {
        Jardin jardin = new Jardin();

        System.out.println("Número inicial de asistentes al jardín: " + jardin.getCuenta());
        System.out.println("=================================================");
        //100

        //entrada de 10 hilos al jardín
        for (int i = 1; i <= 10; i++) {
            (new EntraJardin("Asistente " + i, jardin)).start();
        }
        //110

        for (int i = 1; i <= 15; i++) {
            (new SaleJardin("Asistente " + i, jardin)).start();
        }//salida de 15 hilos al jardín
        //95

        System.out.println("Número final de asistentes en el jardín: " + jardin.getCuenta());
        System.out.println("=================================================");
    }
}

