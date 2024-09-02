package logic;

import java.util.List;

import dataModels.UnitDataModel;
import dataModels.WeaponDataModel;
import dice.Dice;
import dice.DiceExpresionEvaluator;
import simulation.TargetData;

// Esta clase devuelve el daño medio que causa una unidad para cada tipo de salvacion
public class UnitAverageDamageCalculator {

	// Parametros
	private static UnitDataModel unit;

	// Funciones
	public static double[] unitDamageOutAverage(UnitDataModel unidad) {
		unit = unidad;	
		double[] damageDealtList = { 0, 0, 0, 0, 0, 0 };
		List<WeaponDataModel> weaponList = unit.getWeaponList();

		for (WeaponDataModel weapon : weaponList) {
			if (weapon.isEnabled()) {
				double[] weaponDamageDealtSaveList = weaponDamageOutAverage(weapon);
				for (int i = 0; i < 6; i++) {
					damageDealtList[i] += weaponDamageDealtSaveList[i];
				}				
			}
		}

		return damageDealtList;
	}

	/**
	 * Devuelve el daño de una sola arma para cada tipo de salvación. Usando calculos en lugar de tiradas.
	 * 
	 * @return Una lista con valores de tipo Double que representa el daño para los
	 *         6 tipos de salvacion posibles.
	 */
	public static double[] weaponDamageOutAverage(WeaponDataModel weapon) {
		double[] damageDealtList = { 0, 0, 0, 0, 0, 0 };
		
		double numeroAtaques = weapon.getModels() * DiceExpresionEvaluator.promedioEvaluarExpresion(weapon.getAttacks());
		double averageDamage = DiceExpresionEvaluator.promedioEvaluarExpresion(weapon.getDamage());
		
		//double probabilidadImpactar = (7.0 - toHit) / 6.0;
		//double probabilidadHerir = (7.0 - toWound) / 6.0;
		double probabilidadImpactar = Dice.getProbabilidadImpactarHerir(weapon.getToHit());
		double probabilidadHerir = Dice.getProbabilidadImpactarHerir(weapon.getToWound());
		double probabilidadResultado6 = 1.0 / 6.0;
		//double probabilidadSalvaguarda = TargetData.target.isWard() ? Math.max(0, (TargetData.target.getWardValue() - 1) / 6) : 1;
		double probabilidadSalvaguarda = TargetData.target.isWard() ? Dice.getProbabilidadSalvacionInversa(TargetData.target.getWardValue()) : 1;

		// Comprueba si el capitan empuña el arma. Suma 1 ataque si lo hace.
		if (weapon.isChampion()) {
			numeroAtaques++;
		}
		
		// Comprueba si la unidad tiene Carga y si cuenta como que ha cargado. De hacerlo suma 1 al daño.
		if (weapon.isCharge() && unit.isHasCharged()) {
			averageDamage += 1;
		}
		for (int i = 0; i < 6; i++) {
			double probabilidadSalvarBase = ((i+2) - 1.0 + (
					TargetData.target.isEthereal() ? 0.0 : 
							(weapon.isAntiX() ? (TargetData.target.isAntiXTarget() ? (weapon.getRend() + 1.0) : 
								weapon.getRend()) : // weapon.isAntiX() pero no TargetData.target.isAntiXTarget()
									weapon.getRend() // no weapon.isAntiX()
							)
					)) /6;
			
			if (probabilidadSalvarBase > 1) {
				probabilidadSalvarBase = 1;
			}
			
			/* Calcula la probabilidad de fallo de la tirada de salvacion y luego multiplica con la probabilidad de exito de 
			 * la salvacion para obtener la probabilidad de un exito tras repetir un fallo o repetir un fallo con resultado de 1
			 */
			double probabilidadSalvar = TargetData.target.isRerollSave() ? probabilidadSalvarBase - (1 - probabilidadSalvarBase) * probabilidadSalvarBase : 
				(TargetData.target.isRerollSaveOfOne() ? probabilidadSalvarBase - (probabilidadResultado6 * probabilidadSalvarBase) : probabilidadSalvarBase);
			
			if (probabilidadSalvar > 1) {
				probabilidadSalvar = 1;
			}

			double probabilidadExitoNoAutomatico = (probabilidadImpactar - probabilidadResultado6) * probabilidadHerir * Math.max(0.0, probabilidadSalvar) * probabilidadSalvaguarda;
			double probabilidadExitoAutomatico = probabilidadResultado6 * (
					weapon.isCritMortal() ? probabilidadSalvaguarda : 
						(weapon.isCritWounds() ? Math.max(0.0, probabilidadSalvar) : 
							(weapon.isCritImpacts() ? (1.0 - Math.pow(1.0 - probabilidadHerir, 2.0)) * Math.max(0.0, probabilidadSalvar) : 
								probabilidadHerir * Math.max(0.0, probabilidadSalvar)
							)
						)
					) * probabilidadSalvaguarda;
			
			double probabilidadAtaque = probabilidadExitoNoAutomatico + probabilidadExitoAutomatico;
			damageDealtList[i] = probabilidadAtaque * numeroAtaques * averageDamage;
		}
		
		return damageDealtList;
	}
}
