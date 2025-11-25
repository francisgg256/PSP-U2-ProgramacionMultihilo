package productoresconsumidores.libros;

// This class will notify thread(in case of notify) which is waiting on book object.
// It will not give away lock as soon as notify is called, it first complete its synchronized block.
// So in this example, BookWriter will complete the book and notify it to BookReaders.
public class EscritorLibro implements Runnable {
    Libro libro;

    public EscritorLibro(Libro libro) {
        setLibro(libro);
    }

    public void setLibro(Libro libro) {
        if (libro==null)
            throw new NullPointerException("ERROR: Un libro no puede ser nulo.");

        this.libro = libro;
    }

    @Override
    public void run() {
        synchronized (libro) {
            System.out.println("El autor está empezando a escribir el libro: " +libro.getTitulo() );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            libro.setEstaCompletado(true);
            System.out.println("El libro acaba de terminar de escribirse.");

            libro.notify();
            System.out.println("notify a un lector");
        }
    }
}
