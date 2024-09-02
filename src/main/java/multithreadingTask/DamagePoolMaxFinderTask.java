package multithreadingTask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DamagePoolMaxFinderTask extends RecursiveTask<Double>{
	
	private static final int LIMITE = 2500; // Umbral para dividir la tarea
	private double[][] damagePool;
    private int inicio;
    private int fin;
    
	public DamagePoolMaxFinderTask(double[][] damagePool, int inicio, int fin) {
		this.damagePool = damagePool;
		this.inicio = inicio;
		this.fin = fin;
	}

	@Override
	protected Double compute() {
		// si la cantidad a ejecutar está dentro del limite, esta se ejecuta normalmente
		if ((fin - inicio) <= LIMITE) {
			return findMaxSequentially(damagePool, inicio, fin);
		} else { // en caso contrario la tarea se dividen en distintos hilos
			int mitad = inicio + (fin - inicio) / 2; // calculo del punto medio evitando desbordamientos
			// la tarea se divide en 2 bloques
			DamagePoolMaxFinderTask tareaBloque1 = new DamagePoolMaxFinderTask(damagePool, inicio, mitad);
			DamagePoolMaxFinderTask tareaBloque2 = new DamagePoolMaxFinderTask(damagePool, mitad + 1, fin);
			
			tareaBloque1.fork();
			double resultadoTereaBloque2 = tareaBloque2.compute();
			double resultadoTareaBloque1 = tareaBloque1.join();
			
			return (double) Math.max(resultadoTareaBloque1, resultadoTereaBloque2);
		}	
	}
	
	private double findMaxSequentially(double[][] damagePool, int start, int end) {
        double max = Double.MIN_VALUE;
        for (int i = start; i < end; i++) {
            for (int j = 0; j < 6; j++) {
                if (damagePool[i][j] > max) {
                    max = damagePool[i][j];
                }
            }
        }
        return max;
    }  
	
	public static double findMaxDamageInPool(double[][] damagePool) {
	    ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
	    DamagePoolMaxFinderTask finder = new DamagePoolMaxFinderTask(damagePool, 0, damagePool.length);
	    return pool.invoke(finder);
	}

}
