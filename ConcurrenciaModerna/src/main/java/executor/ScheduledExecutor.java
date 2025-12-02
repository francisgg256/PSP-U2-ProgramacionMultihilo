package executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {

    public static class StockMarket implements Runnable{

        @Override
        public void run() {
            System.out.println("Actualizando el stock de datos de la web...");
        }
    }

    public static void main(String[] args) {
        ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(new StockMarket(),1000,5000, TimeUnit.MILLISECONDS);
    }
}
