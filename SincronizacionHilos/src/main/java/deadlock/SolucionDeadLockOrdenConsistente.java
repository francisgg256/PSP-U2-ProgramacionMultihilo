package deadlock;

public class SolucionDeadLockOrdenConsistente {


    public static void main(String[] args) {
            NoDeadlockOrdered ejemplo = new NoDeadlockOrdered();
            Thread t1 = new Thread(ejemplo::metodoA, "Hilo-1");
            Thread t2 = new Thread(ejemplo::metodoB, "Hilo-2");
            t1.start();
            t2.start();
        }

        public static class NoDeadlockOrdered {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        // Ambos métodos adquieren primero lock1 y después lock2
        public void metodoA() {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " bloqueó lock1");
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " bloqueó lock2");
                    // trabajo crítico
                }
            }
        }

        public void metodoB() {
            synchronized (lock1) {                // <-- NOTA: cambiamos el orden (antes era lock2 primero)
                System.out.println(Thread.currentThread().getName() + " bloqueó lock1");
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " bloqueó lock2");
                    // trabajo crítico
                }
            }
        }

    }
}
