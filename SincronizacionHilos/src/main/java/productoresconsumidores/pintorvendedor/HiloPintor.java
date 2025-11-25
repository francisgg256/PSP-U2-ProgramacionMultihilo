package productoresconsumidores.pintorvendedor;

import java.util.Objects;

public class HiloPintor extends Thread{

    private AlmacenCuadros almacen;

    //constructor
    public HiloPintor(AlmacenCuadros almacen) {
        Objects.requireNonNull(almacen,"ERROR: El almacen de cuadros no puede ser nulo.");

        this.almacen = almacen;
    }

    //pinta y guarda en almacén 30 cuadros
    public void run() {
        for (int i = 1; i < 30; i++)
            almacen.guardar();
    }
}
