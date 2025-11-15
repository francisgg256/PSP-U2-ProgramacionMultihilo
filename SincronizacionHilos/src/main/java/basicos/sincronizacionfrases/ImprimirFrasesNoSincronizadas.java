package basicos.sincronizacionfrases;

public class ImprimirFrasesNoSincronizadas implements Runnable {
    private String frase1;
    private String frase2;

    public ImprimirFrasesNoSincronizadas(String frase1, String frase2) {
        setFrase1(frase1);
        setFrase2(frase2);
    }

    public void setFrase1(String frase1) {
        if (frase1==null)
            throw new NullPointerException("ERROR: La frase1 no puede ser nula.");

        if (frase1.isBlank())
            throw new IllegalArgumentException("ERROR: La frase1 no puede estar vacía.");

        this.frase1 = frase1;
    }

    public void setFrase2(String frase2) {
        if (frase2==null)
            throw new NullPointerException("ERROR: La frase2 no puede ser nula.");

        if (frase2.isBlank())
            throw new IllegalArgumentException("ERROR: La frase2 no puede estar vacía.");

        this.frase2 = frase2;
    }

    @Override
    public void run() {
        System.out.print(frase1);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        System.out.println(frase2);
    }
}
