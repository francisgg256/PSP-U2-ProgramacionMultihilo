package productoresconsumidores.libros;

import java.util.Objects;

public class Libro {

    private String titulo;
    private boolean estaCompletado;

    public Libro(String titulo) {
        setTitulo(titulo);
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        if (titulo==null)
            throw new NullPointerException("ERROR: El título de un libro no puede ser nulo.");

        if (titulo.isBlank())
            throw new IllegalArgumentException("ERROR: El título de un libro no puede estar vacío.");

        this.titulo=titulo;
    }

    public boolean isEstaCompletado() {
        return estaCompletado;
    }

    public void setEstaCompletado(boolean estaCompletado) {
        this.estaCompletado = estaCompletado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro libro)) return false;
        return Objects.equals(titulo, libro.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(titulo);
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", estaCompletado=" + estaCompletado +
                '}';
    }
}
