package logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Clase encargada de gestionar multiples peticiones sobre un mismo metodo
public class Debouncer {
	
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long delay; // Retardo antes de ejecutar la última llamada
    private ScheduledFuture<?> future;

    public Debouncer(long delay, TimeUnit unit) {
        this.delay = unit.toMillis(delay);
    }

    public void call(Runnable action) {
        // Cancelar cualquier tarea pendiente
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
        
        // Planificar una nueva tarea
        future = scheduler.schedule(action, delay, TimeUnit.MILLISECONDS);
    }
    
    public void shutdown() {
        scheduler.shutdownNow();
    }

}
