package model.fight;

import model.capacities.Capacity;
import model.trainer.Trainer;

public class ComputerFight extends Fight {
	
	/**
	 * Fight vs computer (random method)
	 */
	
	public ComputerFight() {
		super();
	}
	
	public ComputerFight(Trainer ennemies) {
		super(ennemies);
	}

	/**
	 * User turn : attack capacity
	 * @throws IllegalStateException if it is the turn of the computer
	 * @throws IllegalStateException if the fight already ended
	 * @throws NullPointerException if capacity is null
	 * @return integer damages
	 */
	@Override
	public int attack(Capacity capacity) {
		if (super.getTurn().equals(FightTurn.ennemy))
			throw new IllegalStateException("User can't choose the computer's attack!");
		return super.attack(capacity);
	}
	
	/**
	 * Computer turn : attack random capacity
	 * @throws IllegalStateException if it is the turn of the player
	 * @throws IllegalStateException if the fight already ended
	 * @throws NullPointerException if capacity is null
	 * @return integer damages
	 */
	public int attack() {
		if (super.getTurn().equals(FightTurn.user))
			throw new IllegalStateException("User must choose an attack!");
		return super.attack(getAttacker().getAttacker().getRandomMasteredCapacity());
	}
}
