package locks.lock;

public class ContadorSinLock {
    private int valor = 0;

    public void incrementar() {
        valor++;
    }

    public int getValor() {
        return valor;
    }
}
