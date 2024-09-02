package dice;

import java.util.Random;

public class Dice {

	// Instancias compartidas
	public static final Dice D3 = new Dice(3);
	public static final Dice D6 = new Dice(6);
	
	// Parametros
	private int caras;
	private static Random aleatorio = new Random();

	// Constructor
	public Dice() {
		super();
		this.caras = 6;
	}

	public Dice(int caras) {
		super();
		this.caras = caras;
	}

	public Dice(String caras) {
		super();
		this.caras = Integer.parseInt(caras);
	}

	// FUNCIONES
	public int tirarDado() {
		return aleatorio.nextInt(caras) + 1;
	}
	
	public static int obtenerLanzarMultiple (String entrada) {
		String[] partes = entrada.toLowerCase().split("d");
		int numDados = partes[0].isEmpty() ? 1 : Integer.parseInt(partes[0]);
		int caras = Integer.parseInt(partes[1]);
		
		return tiradaMultiple(numDados, caras);
	}
	
	// Lanza varios dados y suma los resultados
	public static int tiradaMultiple (int numDados, int caras) {
		Dice dado = new Dice(caras);
		int total = 0;
		for (int i = 0; i < numDados; i++) {
			total += dado.tirarDado();
		}
		return total;
	}
	
	public static double obtenerLanzarMultiplePromedio (String entrada) {
		String[] partes = entrada.toLowerCase().split("d");
		int numDados = partes[0].isEmpty() ? 1 : Integer.parseInt(partes[0]);
		int caras = Integer.parseInt(partes[1]);
		
		return tiradaMultiplePromedio(numDados, caras);
	}
	
	public static double tiradaMultiplePromedio (int numDados, int caras) {		
		return numDados * getMedia(caras);
	}

	
	public double getMedia() { // Obtener la media del dado
		return (this.caras + 1) / 2;
	}
	
	
	public static double getMedia(int caras) { // Obtener la media del dado con parametro
		return (caras + 1) / 2;
	}

	
	public double getProbabilidad(int objetivo) { // Obtener la probabilidad de obtener un valor >= al 'objetivo'
		/**
		 * Obtenemos el numero de resultados que son >= al 'objetivo' E.J. si objetivo
		 * es 5 en un D6 significa que un resultado de 5 o 6 superará al objetivo (2 resultados)
		 */
		int numerador = this.caras - objetivo + 1;
		/**
		 * Ajustamos el numerador para evitar que sea negativo o mayor que el numero de
		 * caras del dado
		 */
		numerador = Math.min(Math.max(numerador, 0), this.caras);
		/**
		 * Calculamos y devolvemos la probabilidad, representada con un valor entre 0 y 1
		 */
		return (double) numerador / this.caras;
	}

	
	public double getProbabilidadInversa(int objetivo) { // Obtener la probabilidad de obtener un valor < al 'objetivo'
		return 1 - this.getProbabilidad(objetivo);
	}

	public static double getProbabilidadImpactarHerir(int objetivo) {
		// Los 1 para impactar y herir siempre son fallos y los 6 para impactar siempre son aciertos
		if (objetivo < 2) {
			objetivo = 2;
		} else if (objetivo > 6) {
			objetivo = 6;
		}

		int numerador = 6 - objetivo + 1;
		numerador = Math.min(Math.max(numerador, 0), 6);
		return (double) numerador / 6;
	}
	
	public static double getProbabilidadImpactarHerirInversa(int objetivo) {
		return 1 - Dice.getProbabilidadImpactarHerir(objetivo);
	}
	
	public static double getProbabilidadSalvacion(int objetivo) {
		// Los 1 para salvar siempre son fallos
		if (objetivo < 2) {
			objetivo = 2;
		}
		
		int numerador = 6 - objetivo + 1;
		numerador = Math.min(Math.max(numerador, 0), 6);
		return (double) numerador / 6;
	}
	
	public static double getProbabilidadSalvacionInversa(int objetivo) {
		return 1 - Dice.getProbabilidadSalvacion(objetivo);
	}
	
	/**
	 * Intenta crear un objeto Dice a partir de diferentes tipos de entrada, los cuales
	 * pueden ser Dice, String, Integer, Double. En caso de ser un String, este debe
	 * tener un letra "D" o "d" al principio seguido de uno o mas numeros y nada mas.
	 * @param val El objeto a analizar.
	 * @return Un objeto de tipo Dice o una excepcion si no se ha encontrado alguno 
	 * de los formatos esperados. 
	 */
	public static Object parse(Object val) {
		if (val instanceof Dice) {
			return val;
		} else if (val instanceof String) {
			String strVal = (String) val;
            if (strVal.matches("^[dD](\\d+)$")) {
            	return new Dice(Integer.parseInt(strVal.substring(1)));
            } 
		} else if (val instanceof Integer) {
			int num = (Integer) val;
			if (!isNumberValid(num)) {
                throw new IllegalArgumentException("Invalid Value or Dice (" + val + ")");
            }
			return new Dice(num);
		} else if (val instanceof Double) {
			double num = (Double) val;
			if (!isNumberValid(num)) {
                throw new IllegalArgumentException("Invalid Value or Dice (" + val + ")");
            }
			return new Dice((int)num);
		}
		
		throw new IllegalArgumentException("Invalid input type");
	}
	
	private static boolean isNumberValid(Number number) {
        return !Double.isNaN(number.doubleValue());
    }

	// Getters y Setters
	public int getCaras() {
		return caras;
	}

	public void setCaras(int caras) {
		this.caras = caras;
	}

	// ToString
	@Override
	public String toString() {
		return "D" + this.caras;
	}
}
