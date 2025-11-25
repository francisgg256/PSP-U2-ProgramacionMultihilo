package locks.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContadorConLock {
    private int valor = 0;
    private Lock lock=new ReentrantLock();

    public void incrementar() {

        lock.lock();// Bloquea el recurso

        try {
            valor++;
        } finally {
            lock.unlock(); //Siempre se libera, incluso si hay excepción
        }

    }

    public int getValor() {
        return valor;
    }
}
