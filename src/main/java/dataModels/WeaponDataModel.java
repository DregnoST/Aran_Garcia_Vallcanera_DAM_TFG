package dataModels;

import org.bson.Document;

public class WeaponDataModel {

	// propiedades de arma
	private String weaponName;
	private boolean enabled;
	private int models;
	private String attacks;
	private int toHit;
	private int toWound;
	private int rend;
	private String damage;
	
	
	// habilidades de arma
	private boolean champion;
	private boolean antiX;
	private boolean charge;
	private boolean critImpacts;
	private boolean critWounds;
	private boolean critMortal;
	
	// Constructores
	public WeaponDataModel() {
		super();
		this.weaponName = "weapon";
		this.enabled = true;
		this.models = 1;
		this.attacks = "1";
		this.toHit = 4;
		this.toWound = 4;
		this.rend = 0;
		this.damage = "1";
		this.champion = false;
		this.antiX = false;
		this.charge = false;
		this.critImpacts = false;
		this.critWounds = false;
		this.critMortal = false;
	}
	
	public WeaponDataModel(String weaponName) {
		super();
		this.weaponName = weaponName;
		this.enabled = true;
		this.models = 1;
		this.attacks = "1";
		this.toHit = 4;
		this.toWound = 4;
		this.rend = 0;
		this.damage = "1";
		this.champion = false;
		this.antiX = false;
		this.charge = false;
		this.critImpacts = false;
		this.critWounds = false;
		this.critMortal = false;
	}

	public WeaponDataModel(String weaponName, int models, String attacks, int toHit, int toWound, int rend, String damage, boolean champion,
			boolean antiX, boolean charge, boolean critImpacts, boolean critWounds, boolean critMortal) {
		super();
		this.weaponName = weaponName;
		this.enabled = true;
		this.models = models;
		this.attacks = attacks;
		this.toHit = toHit;
		this.toWound = toWound;
		this.rend = rend;
		this.damage = damage;
		this.champion = champion;
		this.antiX = antiX;
		this.charge = charge;
		this.critImpacts = critImpacts;
		this.critWounds = critWounds;
		this.critMortal = critMortal;
	}
	
	// Gettesrs y Setters
	public String getWeaponName() {
		return weaponName;
	}

	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getModels() {
		return models;
	}
	public void setModels(int models) {
		this.models = models;
	}
	public String getAttacks() {
		return attacks;
	}
	public void setAttacks(String attacks) {
		this.attacks = attacks;
	}
	public int getToHit() {
		return toHit;
	}
	public void setToHit(int toHit) {
		this.toHit = toHit;
	}
	public int getToWound() {
		return toWound;
	}
	public void setToWound(int toWound) {
		this.toWound = toWound;
	}
	public int getRend() {
		return rend;
	}
	public void setRend(int rend) {
		this.rend = rend;
	}
	public String getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		this.damage = damage;
	}
	public boolean isChampion() {
		return champion;
	}
	public void setChampion(boolean champion) {
		this.champion = champion;
	}
	public boolean isAntiX() {
		return antiX;
	}
	public void setAntiX(boolean antiX) {
		this.antiX = antiX;
	}
	public boolean isCharge() {
		return charge;
	}
	public void setCharge(boolean charge) {
		this.charge = charge;
	}
	public boolean isCritImpacts() {
		return critImpacts;
	}
	public void setCritImpacts(boolean critImpacts) {
		this.critImpacts = critImpacts;
	}
	public boolean isCritWounds() {
		return critWounds;
	}
	public void setCritWounds(boolean critWounds) {
		this.critWounds = critWounds;
	}
	public boolean isCritMortal() {
		return critMortal;
	}
	public void setCritMortal(boolean critMortal) {
		this.critMortal = critMortal;
	}	
	
	public Document toDocument() {
		return new Document("weaponName", weaponName)
                .append("enabled", enabled)
                .append("models", models)
                .append("attacks", attacks)
                .append("toHit", toHit)
                .append("toWound", toWound)
                .append("rend", rend)
                .append("damage", damage)
                .append("champion", champion)
                .append("antiX", antiX)
                .append("charge", charge)
                .append("critImpacts", critImpacts)
                .append("critWounds", critWounds)
                .append("critMortal", critMortal);
	}
	
	public static WeaponDataModel fromDocument(Document doc) {
		return new WeaponDataModel(
					doc.getString("weaponName"),
					doc.getInteger("models"),
					doc.getString("attacks"),
					doc.getInteger("toHit"),
					doc.getInteger("toWound"),
					doc.getInteger("rend"),
					doc.getString("damage"),
					doc.getBoolean("champion"),
					doc.getBoolean("antiX"),
					doc.getBoolean("charge"),
					doc.getBoolean("critImpacts"),
					doc.getBoolean("critWounds"),
					doc.getBoolean("critMortal")
				);
	}
	
}
