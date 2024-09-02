package logic;

import java.util.List;

import dataModels.ProbabilityXDamageDataModel;
import dataModels.UnitDataModel;
import dataModels.UnitManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import multithreadingTask.DamagePoolMaxFinderTask;
import simulation.Handler;

public class ProbabilityXDamage {
	
	private static String tempUnitName;
	
	// int[simulacion][resultados simulacion]
	public static void loadData(List<double[][]> allUnitDamage) {
		
		Platform.runLater(() -> {
			Handler.probXDamageSave2.clear();
			Handler.probXDamageSave3.clear();
			Handler.probXDamageSave4.clear();
			Handler.probXDamageSave5.clear();
			Handler.probXDamageSave6.clear();
			Handler.probXDamageSave7.clear();
			
			Handler.probXDamageOrLessSave2.clear();
			Handler.probXDamageOrLessSave3.clear();
			Handler.probXDamageOrLessSave4.clear();
			Handler.probXDamageOrLessSave5.clear();
			Handler.probXDamageOrLessSave6.clear();
			Handler.probXDamageOrLessSave7.clear();

			ObservableList<UnitDataModel> unitList = UnitManager.units;
			
			Handler.maxDamage = 0;
			double max = Double.MIN_VALUE;
			
			for (int i = 0; i < unitList.size(); i++) { // los metodos trabajan con datos para una unidad
				Handler.tempMaxDamage = 0;
				
				double temp = DamagePoolMaxFinderTask.findMaxDamageInPool(allUnitDamage.get(i)); // multinucleo
				Handler.tempMaxDamage = temp;
				tempUnitName = unitList.get(i).getUnitName();
				
				if (temp > max) {
					max = temp;
				}
				
				
				// X Damage
				propXDamageSave2(allUnitDamage.get(i));
				propXDamageSave3(allUnitDamage.get(i));
				propXDamageSave4(allUnitDamage.get(i));
				propXDamageSave5(allUnitDamage.get(i));
				propXDamageSave6(allUnitDamage.get(i));
				propXDamageSave7(allUnitDamage.get(i));
				
				// X Damage o menos
				propXDamageOrLessSave2(allUnitDamage.get(i));
				propXDamageOrLessSave3(allUnitDamage.get(i));
				propXDamageOrLessSave4(allUnitDamage.get(i));
				propXDamageOrLessSave5(allUnitDamage.get(i));
				propXDamageOrLessSave6(allUnitDamage.get(i));
				propXDamageOrLessSave7(allUnitDamage.get(i));
			}
			
			Handler.maxDamage = max;
			
		});
	}
        
    public static double fintMaxDamageInPool(double[][] damagePool) {
    	double max = Double.MIN_VALUE;
		
    	for (int i = 0; i < damagePool.length; i++) {
    		for (int j = 0; j < 6; j++) {
    			if (damagePool[i][j] > max) {
    				max = damagePool[i][j];
    			}				
			}
		}

    	return max;	
    }
    
    // devuelve un porcentaje
    public static double[] calculatePropXDamage(double[] damagePool) {
    	// habria que pasarlo todos los daños a enteros pero no hay tiempo, aqui un parche para arreglarlo
    	int temp = 1;
    	try {
    		temp += (int) Handler.tempMaxDamage;
		} catch (Exception e) {
			temp = Integer.MAX_VALUE;
		}
    	
    	double[] probPool = new double[temp];
    	
    	for (int i = 0; i < probPool.length; i++) {
    		double contador = 0.0;
    		
    		for (double damage : damagePool) {
				if (damage == i) {
					contador++;
				}
			}
    		
    		probPool[i] = (contador / damagePool.length) * 100.0;
		}
    	
    	return probPool;
    }
    
    // devuelve un porcentaje
    public static double[] calculatePropXDamageOrLess(double[] damagePool) {
    	// habria que pasarlo todos los daños a enteros pero no hay tiempo, aqui un parche para arreglarlo
    	int temp = 1;
    	try {
    		temp += (int) Handler.tempMaxDamage;
		} catch (Exception e) {
			temp = Integer.MAX_VALUE;
		}
    	
    	double[] probPool = new double[temp];
    	
    	for (int i = 0; i < probPool.length; i++) {
    		double contador = 0.0;
    		
    		for (double damage : damagePool) {
				if (damage <= i) {
					contador++;
				}
			}
    		
    		probPool[i] = (contador / damagePool.length) * 100.0;
		}
    	
    	return probPool;
    }
    
    // X Damage
    private static void propXDamageSave2(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 2+
    	double[] save2DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save2DamagePool[i] = damagePool[i][0];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save2DamagePool); // probabilidades para salvacion 2+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave2.add(probData);    	
    }
    
    private static void propXDamageSave3(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 3+
    	double[] save3DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save3DamagePool[i] = damagePool[i][1];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save3DamagePool); // probabilidades para salvacion 3+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave3.add(probData);    	
    }
    
    private static void propXDamageSave4(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 4+
    	double[] save4DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save4DamagePool[i] = damagePool[i][2];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save4DamagePool); // probabilidades para salvacion 4+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave4.add(probData);    	
    }
    
    private static void propXDamageSave5(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 5+
    	double[] save5DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save5DamagePool[i] = damagePool[i][3];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save5DamagePool); // probabilidades para salvacion 5+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave5.add(probData);    	
    }
    
    private static void propXDamageSave6(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 6+
    	double[] save6DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save6DamagePool[i] = damagePool[i][4];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save6DamagePool); // probabilidades para salvacion 6+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave6.add(probData);    	
    }
    
    private static void propXDamageSave7(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 7+
    	double[] save7DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save7DamagePool[i] = damagePool[i][5];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamage(save7DamagePool); // probabilidades para salvacion 7+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageSave7.add(probData);    	
    }
    
    // X Damage o menos
    private static void propXDamageOrLessSave2(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 2+
    	double[] save2DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save2DamagePool[i] = damagePool[i][0];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save2DamagePool); // probabilidades para salvacion 2+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave2.add(probData);    	
    }
    
    private static void propXDamageOrLessSave3(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 3+
    	double[] save3DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save3DamagePool[i] = damagePool[i][1];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save3DamagePool); // probabilidades para salvacion 3+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave3.add(probData);
    }
    
    private static void propXDamageOrLessSave4(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 4+
    	double[] save4DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save4DamagePool[i] = damagePool[i][2];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save4DamagePool); // probabilidades para salvacion 4+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave4.add(probData);
    }
    
    private static void propXDamageOrLessSave5(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 5+
    	double[] save5DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save5DamagePool[i] = damagePool[i][3];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save5DamagePool); // probabilidades para salvacion 5+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave5.add(probData);
    }
    
    private static void propXDamageOrLessSave6(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 6+
    	double[] save6DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save6DamagePool[i] = damagePool[i][4];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save6DamagePool); // probabilidades para salvacion 6+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave6.add(probData);
    }
    
    private static void propXDamageOrLessSave7(double[][] damagePool) {
    	// lista con el daño de una unidad de cada simulacion para salvacion 7+
    	double[] save7DamagePool = new double[damagePool.length];
    	
    	for (int i = 0; i < damagePool.length; i++) {
			for (int j = 0; j < damagePool[i].length; j++) {
				save7DamagePool[i] = damagePool[i][5];
			}
		}
    	
    	// Añadir a la observable list
    	double[] probPool =  calculatePropXDamageOrLess(save7DamagePool); // probabilidades para salvacion 7+ de una unidad
    	ProbabilityXDamageDataModel probData = new ProbabilityXDamageDataModel(tempUnitName, probPool);
    	Handler.probXDamageOrLessSave7.add(probData);
    }
}
