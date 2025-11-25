package productoresconsumidores.libros;

public class PrestamoLibro {
    public static void main(String args[])
    {
        // Book object on which wait and notify method will be called
        Libro libro=new Libro("El Alquimista");
        LectorLibro lectorJuan=new LectorLibro(libro);
        LectorLibro lectorAna=new LectorLibro(libro);

        // BookReader threads which will wait for completion of book
        Thread juanThread=new Thread(lectorJuan,"Juan");
        Thread anaThread=new Thread(lectorAna,"Ana");

        anaThread.start();
        juanThread.start();

        // To ensure both readers started waiting for the book
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // BookWriter thread which will notify once book get completed
        EscritorLibro escritor=new EscritorLibro(libro);
        Thread escritorThread=new Thread(escritor);
        escritorThread.start();
    }
}
