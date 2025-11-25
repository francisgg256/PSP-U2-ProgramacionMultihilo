package productoresconsumidores.pintorvendedor;

import java.util.Objects;

public class HiloVendedor extends Thread {

    private AlmacenCuadros almacen;
    //constructor
    public HiloVendedor(AlmacenCuadros almacen) {
        Objects.requireNonNull(almacen,"ERROR: El almacén de cuadros no puede ser nulo.");

        this.almacen = almacen;
    }

    //vende (saca) del almacén 30 cuadros
    public void run() {
        for (int i = 1; i < 30; i++)
            almacen.sacar();
    }
}
