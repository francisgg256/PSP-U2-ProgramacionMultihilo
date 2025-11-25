package productoresconsumidores.panaderia;

public class Panaderia {
    private String pan;
    private boolean hayPan;

    public synchronized void hornear(String masa)
    {
        while(hayPan)
        {
            try
            {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        this.pan=masa;
        System.out.println("Panadero hornea pan " + this.pan);
        hayPan=true;
        notify();
    }

    public synchronized String consumir()
    {
        while (!hayPan)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Cliente consume pan " + this.pan);
        hayPan=false;
        notify();
        return pan;
    }
}
