package productoresconsumidores.buffer;

import java.util.Objects;

public class Consumidor implements Runnable {

    private BufferInt buffer;

    public Consumidor(BufferInt buffer)
    {
        Objects.requireNonNull(buffer,"ERROR: No se puede usar un buffer nulo.");

        this.buffer=buffer;
    }

    @Override
    public void run() {
        while (true)
        {
            buffer.consume();
        }
    }
}
