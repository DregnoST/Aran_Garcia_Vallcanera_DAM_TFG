package simulation;

public class TargetData {
	
	public static TargetData target = new TargetData();

	// Propiedades
	private boolean rerollSaveOfOne;
	private boolean rerollSave;
	private boolean ethereal;
	private boolean antiXTarget;
	private boolean ward;
	private int wardValue;
	private boolean wardValueValidation;
	
	// Constructores

	public TargetData() {
		super();
		this.rerollSaveOfOne = false;
		this.rerollSave = false;
		this.ethereal = false;
		this.antiXTarget = false;
		this.ward = false;
		this.wardValue = 6;
		this.wardValueValidation = true;
	}

	// Getters y Setters
	public boolean isRerollSaveOfOne() {
		return rerollSaveOfOne;
	}

	public void setRerollSaveOfOne(boolean rerollSaveOfOne) {
		this.rerollSaveOfOne = rerollSaveOfOne;
	}

	public boolean isRerollSave() {
		return rerollSave;
	}

	public void setRerollSave(boolean rerollSave) {
		this.rerollSave = rerollSave;
	}

	public boolean isEthereal() {
		return ethereal;
	}

	public void setEthereal(boolean ethereal) {
		this.ethereal = ethereal;
	}

	public boolean isWard() {
		return ward;
	}

	public void setWard(boolean ward) {
		this.ward = ward;
	}

	public int getWardValue() {
		return wardValue;
	}

	public void setWardValue(int wardValue) {
		this.wardValue = wardValue;
	}

	public boolean isAntiXTarget() {
		return antiXTarget;
	}

	public void setAntiXTarget(boolean antiXTarget) {
		this.antiXTarget = antiXTarget;
	}

	public boolean isWardValueValidation() {
		return wardValueValidation;
	}

	public void setWardValueValidation(boolean wardValueValidation) {
		this.wardValueValidation = wardValueValidation;
	}
	
}
