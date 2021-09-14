package view.panels;

import controller.Controller;
import model.capacities.Capacity;
import model.fight.ComputerFight;

public class ComputerBattlePanel extends MultiplayerBattlePanel {
	
	private static final long serialVersionUID = 1884276474139945713L;
	
	private final ComputerFight battle;
	
	/**
	 * Initializes the entire page using information about both players
	 * @param ComputerFight battle
	 */
	public ComputerBattlePanel(ComputerFight battle) {
		super(battle);
		this.battle = battle;
	}
	
	@Override
	protected void attackAction(Capacity capacity) {
		Controller.getInstance().classicalFight(battle, capacity);
	}
	

}
