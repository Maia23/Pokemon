package view.panels;

import controller.Controller;
import model.capacities.Capacity;
import model.league.League;

public class LeagueBattlePanel extends ComputerBattlePanel {
	
	private final League league;
	
	private static final long serialVersionUID = -9085956092982301223L;

	public LeagueBattlePanel(League league) {
		super(league.getFight());
		this.league = league;
	}
	
	protected void attackAction(Capacity capacity) {
		Controller.getInstance().leagueFight(league, capacity);
	}
	
}
