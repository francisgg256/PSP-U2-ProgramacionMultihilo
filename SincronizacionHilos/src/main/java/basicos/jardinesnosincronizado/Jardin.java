package basicos.jardinesnosincronizado;

//clase que simula las entradas y las salidas al Jardín
public class Jardin {

    private int cuenta; //para contar las entradas y salidas al Jardín

    public Jardin() {
        cuenta = 100; //inicalmente hay 100 personas en le jardín
    }

    //metodo que increamenta en 1 la varibale cuenta
    public void incrementaCuenta() {
        //muestra el hilo que entra en el metodo
        System.out.println("----- Entra en el Jardín el " + Thread.currentThread().getName());

        cuenta++;

        //cuenta cada acceso al jardín y muestra el número de accesos
        System.out.println("Asistentes presentes en el jardín: " + cuenta);

    }

    //metodo que decrementa en 1 la varibale cuenta
    public  void  decrementaCuenta() {
        //muestra el hilo que sale en el metodo
        System.out.println("----- Sale del Jardín el " + Thread.currentThread().getName());

        cuenta--;

        //cuenta cada acceso al jardín y muestra el número de accesos
        System.out.println("Asistentes presentes en el jardín: " + cuenta);
    }

    public int getCuenta() {
        return cuenta;
    }
}
