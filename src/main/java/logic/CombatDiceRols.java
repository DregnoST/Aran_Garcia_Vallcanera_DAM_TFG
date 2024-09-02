package logic;

import dataModels.UnitDataModel;
import dataModels.WeaponDataModel;
import dice.Dice;
import dice.DiceExpresionEvaluator;
import simulation.TargetData;

public class CombatDiceRols {
	
	public static UnitDataModel unit;
	
	/**
	 * Resuelve la tirada para Impactar de un ataque. Luego realiza la tirada para
	 * Herir.
	 * 
	 * @param weapon El arma que se usará para calcular el daño.
	 * @return Una lista con valores de tipo Double que representa el daño para los
	 *         6 tipos de salvacion posibles.
	 */
	public static double[] resolverImpactar(WeaponDataModel weapon) {
		/*
		 * Lista de enteros que define el tipo y cantidad de exitos a la tirada para
		 * impactar. En la posicion 0 se haya el numero de exitos estandar, contando los
		 * impactos adicionales. En la posicion 1 se haya el numero de exitos que han
		 * resultado en Herida Mortal. En la posición 2 se haya el numero de exitos que
		 * han resultado en Herida Automática.
		 */
		int[] exitos = { 0, 0, 0 };
		int tirada = Dice.D6.tirarDado();

		if (weapon.isCritMortal() && tirada == 6) {
			exitos[1]++;
		} else if (weapon.isCritWounds() && tirada == 6) {
			exitos[2]++;
		} else if (weapon.isCritImpacts() && tirada == 6) {
			exitos[0] += 2;
		} else if (tirada >= weapon.getToHit() && tirada != 1 || tirada == 6) {
			exitos[0]++;
		}
		return resolverHerir(weapon, exitos);
	}

	/**
	 * Resuelve la tirada para Herir de un ataque. Luego realiza la tirada para
	 * Salvar.
	 * 
	 * @param weapon El arma que se usará para calcular el daño.
	 * @param exitosImpactar Lista de enteros que define el tipo y cantidad de exitos a la
	 *               tirada para impactar. 
	 *               En la posicion [0] se haya el numero de exitos estandar, contando los impactos adicionales. 
	 *               En la posicion [1] se haya el numero de exitos que han resultado en Herida Mortal. 
	 *               En la posición [2] se haya el numero de exitos que han resultado en Herida Automática.
	 * @return Una lista con valores de tipo Double que representa el daño para los
	 *         6 tipos de salvacion posibles.
	 */
	public static double[] resolverHerir(WeaponDataModel weapon, int[] exitosImpactar) {
		int[] exitos = { 0, 0, 0 };
		exitos[1] = exitosImpactar[1];
		exitos[2] = exitosImpactar[2];
		
		for (int i = 0; i < exitosImpactar[0]; i++) {
			int tirada = Dice.D6.tirarDado();
			if (tirada >= weapon.getToWound() && tirada != 1 || tirada == 6) {
				exitos[0]++;
			}
		}
		return resolverSalvar(weapon, exitos);
	}

	//
	public static double[] resolverSalvar(WeaponDataModel weapon, int[] exitos) {
		double[] weaponDamageDealtSaveList = {0,0,0,0,0,0};
		int tiradaSalvacionModificada = 0;

		// Calcula el daño del ataque
		int attackDamage = DiceExpresionEvaluator.evaluarExpresion(weapon.getDamage());

		// Comprueba si la unidad tiene Carga y si cuenta como que ha cargado. De hacerlo suma 1 al daño.
		if (weapon.isCharge() && unit.isHasCharged()) {
			attackDamage++;
		}

		// realiza una tirada de salvacion por cada exito y Herida Automatica.
		for (int i = 0; i < (exitos[0] + exitos[2]); i++) {
			int tiradaSalvaguarda = Dice.D6.tirarDado();
			int tiradaSalvacion = Dice.D6.tirarDado();
			if (!TargetData.target.isEthereal()) { // La perforacion empeora la tirada de salvacion si el objetivo no es Etereo
				
				tiradaSalvacionModificada = tiradaSalvacion - weapon.getRend() - (weapon.isAntiX() ? (TargetData.target.isAntiXTarget() ? 1 : 0) : 0);				
			}
			
			for (int j = 0; j < 6; j++) { // Lanza salvacion y salvaguarda contra Heridas No Mortales
				
				
				if (!(tiradaSalvacionModificada >= (j + 2) && tiradaSalvacion != 1)) {
					
					if (TargetData.target.isRerollSave()) { // Repite la tirada de salvacion
						tiradaSalvacion = Dice.D6.tirarDado();
						if (!TargetData.target.isEthereal()) { // La perforacion empeora la tirada de salvacion si el objetivo no es Etereo
							tiradaSalvacionModificada = tiradaSalvacion - weapon.getRend();				
						}
						
						if (!(tiradaSalvacionModificada >= (j + 2) && tiradaSalvacion != 1)) {
							weaponDamageDealtSaveList[j] += resolverSalvaguarda(attackDamage, tiradaSalvaguarda);	
						}
						
					} else if (TargetData.target.isRerollSaveOfOne() && tiradaSalvacion == 1) { // Repite la tirada de salvacion si ha salido un 1
						tiradaSalvacion = Dice.D6.tirarDado();
						if (!TargetData.target.isEthereal()) { // La perforacion empeora la tirada de salvacion si el objetivo no es Etereo
							tiradaSalvacionModificada = tiradaSalvacion - weapon.getRend();				
						}
						if (!(tiradaSalvacionModificada >= (j + 2) && tiradaSalvacion != 1)) {
							weaponDamageDealtSaveList[j] += resolverSalvaguarda(attackDamage, tiradaSalvaguarda);
						}
						
					} else {
						weaponDamageDealtSaveList[j] += resolverSalvaguarda(attackDamage, tiradaSalvaguarda);	
					}	
				}	
			}
		}
		
		for (int i = 0; i < exitos[1]; i++) { // Lanza salvaguarda contra Heridas Mortales
			int tiradaSalvaguarda = Dice.D6.tirarDado();
			
			for (int j = 0; j < 6; j++) {
				weaponDamageDealtSaveList[j] += resolverSalvaguarda(attackDamage, tiradaSalvaguarda);
			}
		}
		return weaponDamageDealtSaveList;
	}
	
	public static double resolverSalvaguarda (int attackDamage, int tiradaSalvaguarda) {		
		if (TargetData.target.isWard()) { // En caso de fallar la salvacion se intenta salvar con Salvaguarda si se tiene.
			if (!(tiradaSalvaguarda >= TargetData.target.getWardValue() && tiradaSalvaguarda != 1)) {
				return attackDamage;
			} else {
				return 0.0;
			}	
			
		} else {
			return attackDamage;						
		}
	}

}
