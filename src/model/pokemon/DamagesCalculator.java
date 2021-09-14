package model.pokemon;

public class DamagesCalculator {
	public int getDamages(double attackerLevel, double attackerAttack, double attackerPower, double victimDefense, double victimTypeConstants) {
		return (int) ((((attackerLevel * 0.4 + 2.0) * attackerAttack * attackerPower) / (victimDefense * 50.0) + 20) * victimTypeConstants);
	}
}
