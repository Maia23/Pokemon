package model.fight;

public enum FightTurn {
	/**
	 * Enum of the turn type of a Fight
	 */
	
	user(false),
	ennemy(false),
	userVictory(true),
	ennemyVictory(true);
	
	private final boolean ended;
	
	private FightTurn(boolean ended) {
		this.ended =  ended;
	}
	
	/**
	 * Return true if the fight ended
	 * @return boolean ended
	 */
	public boolean ended() {
		return ended;
	}
}
