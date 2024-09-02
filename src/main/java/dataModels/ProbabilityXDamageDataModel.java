package dataModels;

public class ProbabilityXDamageDataModel {
	// modelo de datos
	private String unitName;
	private double[] probability;

	public ProbabilityXDamageDataModel(String unitName, double[] probability) {
		this.unitName = unitName;
		this.probability = probability;
	}

	public String getUnitName() {
		return unitName;
	}


	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public double[] getProbability() {
		return probability;
	}


	public void setProbability(double[] probability) {
		this.probability = probability;
	}
	
	
}
