package semaphore.terminalweb;

public class ServidorWeb {

    //clase que simula los accesos a un servidor
    private int cuenta;

    public ServidorWeb() {
        cuenta = 0;
    }

    public void incrementaCuenta() {
        System.out.println("----- Accede al servidor " + Thread.currentThread().getName());

        cuenta++;

        //cuenta cada acceso al servidor y muestra el número de accesos
        System.out.println("Total clientes que han accedido al servidor: " + cuenta);
    }

    public int getCuenta()
    {
        return cuenta;
    }
}
