package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dataModels.UnitDataModel;
import dataModels.WeaponDataModel;
import dice.DiceExpresionEvaluator;
import simulation.SimulationMain;

public class UnitDamageCalculator {
	
	// Parametros
	private static UnitDataModel unit;
	
	
	// Funciones
	
	/**
	 * Devuelve el daño combinado de cada arma para cada tipo de salvación.
	 * 
	 * @return Una lista con valores de tipo Double que representa el daño para los
	 *         6 tipos de salvacion posibles.
	 */
	public static double[][] unitDamageOutMultiThreading(UnitDataModel unidad) throws InterruptedException, ExecutionException {
		unit = unidad;
		CombatDiceRols.unit = unit;
		
		List<WeaponDataModel> weaponList = unit.getWeaponList();
		double[][] totalDamagePerSimulation = new double[SimulationMain.simulaciones][6];

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<double[][]>> results = new ArrayList<>();
		
		// crea tareaas para cada arma
		for (WeaponDataModel weapon : weaponList) {
			Callable<double[][]> task = () -> {
				double[][] partialDamagePerSimulation = new double[SimulationMain.simulaciones][6];
				for (int i = 0; i < SimulationMain.simulaciones; i++) {
					double[] damageThisSimulation = weaponDamageOut(weapon);

					
					// Suma el daño de esta simulación al total de daños de todas las armas en la misma simulación
					for (int j = 0; j < 6; j++) {
						totalDamagePerSimulation[i][j] += damageThisSimulation[j];
					}
				}
				
				return partialDamagePerSimulation;
			};
			
			results.add(executor.submit(task));

		}
		
        // Recolectar y combinar resultados
        for (Future<double[][]> result : results) {
            double[][] partialResults = result.get();
            for (int i = 0; i < SimulationMain.simulaciones; i++) {
                for (int j = 0; j < 6; j++) {
                    totalDamagePerSimulation[i][j] += partialResults[i][j];
                }
            }
        }

		return totalDamagePerSimulation;
	}
	
	
	public static double[][] unitDamageOutSingleTrhead(UnitDataModel unidad) {
		unit = unidad;
		CombatDiceRols.unit = unit;
		
		List<WeaponDataModel> weaponList = unit.getWeaponList();
		double[][] totalDamagePerSimulation = new double[SimulationMain.simulaciones][6];
		
		for (WeaponDataModel weapon : weaponList) {
			for (int i = 0; i < SimulationMain.simulaciones; i++) {
				double[] damageThisSimulation = weaponDamageOut(weapon);

				
				// Suma el daño de esta simulación al total de daños de todas las armas en la misma simulación
				for (int j = 0; j < 6; j++) {
					totalDamagePerSimulation[i][j] += damageThisSimulation[j];
				}
			}
		}

		return totalDamagePerSimulation;
	}
	
	/**
	 * Devuelve el daño de una sola arma para cada tipo de salvación.
	 * 
	 * @return Una lista con valores de tipo Double que representa el daño para los
	 *         6 tipos de salvacion posibles.
	 */
	public static double[] weaponDamageOut(WeaponDataModel weapon) {
		double[] damageDealtList = { 0, 0, 0, 0, 0, 0 };
		double[] attackDamageDealtList = new double[6];
		int numeroLanzamientosIniciales = weapon.getModels() * DiceExpresionEvaluator.evaluarExpresion(weapon.getAttacks());

		// Comprueba si el capitan empuña el arma. Suma 1 ataque si lo hace.
		if (weapon.isChampion()) {
			numeroLanzamientosIniciales++;
		}

		for (int i = 0; i < numeroLanzamientosIniciales; i++) {
			/*
			 * Suma el daño de cada ataque y lo guarda en una lista con la suma de los daños
			 * para cada tipo de salvacion.
			 */
			
			attackDamageDealtList = CombatDiceRols.resolverImpactar(weapon);
			for (int j = 0; j < 6; j++) {
				damageDealtList[j] += attackDamageDealtList[j];
			}
		}

		
		return damageDealtList;
	}
}
