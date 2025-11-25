package productoresconsumidores.pintorvendedor;

public class Main {
    public static void main(String[] args) {

       //recurso compartido por los hilos, el almacén
       AlmacenCuadros almacen= new AlmacenCuadros();

       //se crean los hilos
       HiloPintor pintor=new HiloPintor(almacen);
       HiloVendedor vendedor=new HiloVendedor(almacen);

       //se inician los hilos
       pintor.start();
       vendedor.start();

    }
} // fin clase Main
