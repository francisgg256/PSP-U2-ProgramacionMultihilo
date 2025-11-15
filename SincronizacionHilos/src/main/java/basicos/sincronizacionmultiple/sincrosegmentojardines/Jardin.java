package basicos.sincronizacionmultiple.sincrosegmentojardines;


public class Jardin {

    private int cuenta; //para contar las entradas y salidas al Jardín

    public Jardin() {
        cuenta = 100; //inicalmente hay 100 personas en le jardín
    }

    public  void incrementaCuenta()
    {

        System.out.println("----- Entra en el Jardín el " + Thread.currentThread().getName());

        cuenta++;

        //cuenta cada acceso al jardín y muestra el número de accesos
        System.out.println("Asistentes presentes en el jardín: " + cuenta);

    }

    public  void  decrementaCuenta()
    {

        System.out.println("----- Sale del Jardín el " + Thread.currentThread().getName());


        cuenta--;

        //cuenta cada acceso al jardín y muestra el número de accesos
        System.out.println("Asistentes presentes en el jardín: " + cuenta);

    }
}
