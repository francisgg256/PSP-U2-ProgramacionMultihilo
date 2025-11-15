package basicos.sincronizametodo;

public class Main {

    public static void main(String[] args) {

        ServidorWeb servidor = new ServidorWeb();

        System.out.println("Número inicial de clientes conectados al servidor: " + servidor.getCuenta());
        System.out.println("=================================================");
        //crea un objeto ServidorWeb
        HiloTerminal cliente1 = new HiloTerminal("Cliente 1", servidor);
        HiloTerminal cliente2 = new HiloTerminal("Cliente 2",servidor);
        HiloTerminal cliente3 = new HiloTerminal("Cliente 3",servidor);
        HiloTerminal cliente4 = new HiloTerminal("Cliente 4",servidor);
        //Se crean cuatro hilos

        cliente1.start();
        cliente2.start();
        cliente3.start();
        cliente4.start();
        //se inician los cuatro hilos

        System.out.println("Número final de clientes conectados al servidor: " + servidor.getCuenta());
        System.out.println("=================================================");
    }
}
