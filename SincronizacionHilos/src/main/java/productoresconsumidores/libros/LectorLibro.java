package productoresconsumidores.libros;

// It will first take a lock on book object
// Then, the thread will wait until other thread call notify method, then after it will complete its processing.
// So in this example, it will wait for BookWriter to complete the book.
public class LectorLibro implements Runnable {
    private Libro libro;

    public LectorLibro(Libro libro) {

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
            System.out.println(Thread.currentThread().getName()+" está esperando a que el libro "+libro.getTitulo() + " esté completo") ;
            try {
                libro.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+": El libro ya ha sido completado. Ahora sí puedes leerlo.");
        }
    }
}
