package productoresconsumidores.buffer;

import java.util.Objects;

public class Productor implements Runnable{
    private BufferInt buffer;

    public Productor(BufferInt buffer)
    {
        Objects.requireNonNull(buffer,"ERROR: No se puede usar un buffer nulo.");

        this.buffer=buffer;
    }

    @Override
    public void run() {
        while (true)
        {
            buffer.produce();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
