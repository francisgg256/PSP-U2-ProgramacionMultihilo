package productoresconsumidores.panaderia;

public class ProductorConsumidorPan {

    public static void main(String[] args) {
        Panaderia panaderia=new Panaderia();

        Thread panadero= new Thread(new Panadero(panaderia),"Panadero");
        Thread consumidor= new Thread(new Consumidor(panaderia),"Consumidor");

        panadero.start();
        consumidor.start();




        // Esperamos a que terminen para terminar el main correctamente
        try {
            panadero.join();
            consumidor.join();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


}
