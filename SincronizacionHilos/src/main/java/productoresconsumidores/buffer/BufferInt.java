package productoresconsumidores.buffer;

import java.util.ArrayList;
import java.util.List;

public class BufferInt {
    private List<Integer> buffer=new ArrayList<>();
    private int capacidad=5;

    public synchronized void produce()
    {
        //if(buffer.size()==capacidad) {
        while(buffer.size()==capacidad) {
            System.out.println("Buffer completo, productor esperando...");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Productor añade nuevos items...");
        for(int i=0;i<capacidad;i++)
        {
            buffer.add(i);
            System.out.println("Productor añade el item " + i);
        }

        notify();
    }

    public synchronized void consume()
    {
        //if (buffer.size()<capacidad)
        while (buffer.size()<capacidad)
        {
            System.out.println("Buffer vacío, consumidor esperando...");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        while(!buffer.isEmpty())
        {
            int item=buffer.remove(0);
            System.out.println("Consumidor consume item " + item);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        notify();
    }

}
