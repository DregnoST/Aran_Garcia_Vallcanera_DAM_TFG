package dice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiceExpresionEvaluator {
	
	public static int evaluarExpresion(String entrada) {
		// Toma la cadena de entrada, elimina los espacios y separa por operadores (+ o -)
		// convierte el array resultante en un stream para poder aplicar operaciones de filtro ( Arrays.stream(...) )
		// filtra el stream para eliminar cualquier elemento vacio ( .filter(item -> !item.isEmpty()) )
		// convierte el stream filtrado de nuevo en lista ( .collect(Collectors.toList()) )
		List<String> items = Arrays.stream(String.valueOf(entrada).replaceAll("\\s", "").split("(?=[+-])"))
				.filter(item -> !item.isEmpty())
				.collect(Collectors.toList());
		
		int resultado = 0;
		for (String item : items) {
			int signo = 1; // Predetermminado a suma
			
			if (item.startsWith("-")) {
				signo = -1; // Cambia a resta si el item comienza con '-'
			}
			
			item = item.replace("+", "").replace("-", ""); // Elimina los signos para análisis
			if (item.toLowerCase().contains("d")) {
				resultado += (signo * tirarDadoMultipleEvaluado(item));
			} else {
				resultado += (signo * Integer.parseInt(item));
			}
		}
		
		return resultado;
	}
	
	private static int tirarDadoMultipleEvaluado(String diceNotation) {
        String[] parts = diceNotation.split("[dD]");
        int numDice = parts[0].isEmpty() ? 1 : Integer.parseInt(parts[0]);
        int numSides = Integer.parseInt(parts[1]);
        return Dice.tiradaMultiple(numDice, numSides);
    }
	
	public static double promedioEvaluarExpresion(String entrada) {
		// Toma la cadena de entrada, elimina los espacios y separa por operadores (+ o -)
		// convierte el array resultante en un stream para poder aplicar operaciones de filtro ( Arrays.stream(...) )
		// filtra el stream para eliminar cualquier elemento vacio ( .filter(item -> !item.isEmpty()) )
		// convierte el stream filtrado de nuevo en lista ( .collect(Collectors.toList()) )
		List<String> items = Arrays.stream(String.valueOf(entrada).replaceAll("\\s", "").split("(?=[+-])"))
				.filter(item -> !item.isEmpty())
				.collect(Collectors.toList());
		
		double resultado = 0;
		for (String item : items) {
			int signo = 1; // Predetermminado a suma
			
			if (item.startsWith("-")) {
				signo = -1; // Cambia a resta si el item comienza con '-'
			}
			
			item = item.replace("+", "").replace("-", ""); // Elimina los signos para análisis
			if (item.toLowerCase().contains("d")) {
				resultado += (signo * promedioDadoMultipleEvaluado(item));
			} else {
				resultado += (signo * Integer.parseInt(item));
			}
		}
		
		return resultado;
	}
	
	private static double promedioDadoMultipleEvaluado(String diceNotation) {
        String[] parts = diceNotation.split("[dD]");
        int numDice = parts[0].isEmpty() ? 1 : Integer.parseInt(parts[0]);
        int numSides = Integer.parseInt(parts[1]);
        return Dice.tiradaMultiplePromedio(numDice, numSides);
    }
}
