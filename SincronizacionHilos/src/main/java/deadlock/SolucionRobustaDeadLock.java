package deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SolucionRobustaDeadLock {


        public static void main(String[] args) {
            NoDeadlockTryLock ejemplo = new NoDeadlockTryLock();
            Thread t1 = new Thread(ejemplo::metodoA, "Hilo-1");
            Thread t2 = new Thread(ejemplo::metodoB, "Hilo-2");
            t1.start();
            t2.start();
        }

    public static class NoDeadlockTryLock {
        private final ReentrantLock lock1 = new ReentrantLock();
        private final ReentrantLock lock2 = new ReentrantLock();

        public void metodoA() {
            try {
                if (lock1.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " bloqueó lock1");
                        // Intenta obtener lock2 sin bloquear indefinidamente
                        if (lock2.tryLock(200, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println(Thread.currentThread().getName() + " bloqueó lock2");
                                // trabajo crítico
                            } finally {
                                lock2.unlock();
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + " NO pudo bloquear lock2, liberando lock1 y reintentando después");
                            // estrategia: reintentar, o hacer otra acción
                        }
                    } finally {
                        lock1.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " NO pudo bloquear lock1");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void metodoB() {
            try {
                if (lock2.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " bloqueó lock2");
                        if (lock1.tryLock(200, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println(Thread.currentThread().getName() + " bloqueó lock1");
                                // trabajo crítico
                            } finally {
                                lock1.unlock();
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + " NO pudo bloquear lock1, liberando lock2 y reintentando después");
                        }
                    } finally {
                        lock2.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " NO pudo bloquear lock2");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
