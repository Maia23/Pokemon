package view;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Loader;
import model.fight.ComputerFight;
import model.fight.Fight;
import model.league.League;
import model.pokemon.Pokemon;
import view.panels.MultiplayerBattlePanel;
import view.panels.ComputerBattlePanel;
import view.panels.LeaguePanel;
import view.panels.MenuPanel;
import view.panels.MyTeamPanel;
import view.panels.PokedexPanel;
import view.panels.PokemonAttacksPanel;
import view.panels.PokemonPanel;

/**
 * Class implementing the main controller of every visual component
 */
public final class ViewManager implements Loader {

	private final static ViewManager viewManager = new ViewManager();

	private ViewManager() {}

	/**
	 * Allows to retrieve the ViewManager outside the class
	 * @return ViewManager
	 */
	public static ViewManager getInstance() {   
		return viewManager;
	}

	private final GameWindow gameWindow = new GameWindow();
	private final CardLayout layout = new CardLayout();

	private String lastPageID = null;

	private MenuPanel menuPage = new MenuPanel();
	private PokedexPanel pokedexPage = null;
	private MyTeamPanel myTeamPage = null;
	private PokemonPanel pokemonPage = null;
	private PokemonAttacksPanel pokemonAttacksPage = null;
	private MultiplayerBattlePanel multiplayerBattlePanel = null;
	private ComputerBattlePanel computerBattlePanel = null;
	//private LeagueBattlePanel leagueBattlePanel = null;
	private LeaguePanel leaguePage = null;

	/**
	 * Initialises the main variables for the ViewManager to start
	 */
	@Override
	public void load() {
		gameWindow.setVisible(true);
		gameWindow.setLayout(layout);
		gameWindow.add(menuPage, "menu");

		menuPage.setName("menu");

	}

	/**
	 * Shows simple error message with a personalized message
	 * @param message

	 */
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(gameWindow, message, "Something went wrong :(", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Shows default dialog box with personalized message
	 * @param message

	 */
	public void showDialogMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * Changes visible panel and shows the menu to the user
	 * 

	 */
	public void showMenu() {
		updateLastPageID();
		layout.show(gameWindow.getContentPane(), "menu");
	}

	/**
	 * Changes visible panel and shows Pokedex to user
	 * 

	 */
	public void showPokedex() {
		updateLastPageID();
		pokedexPage = new PokedexPanel();
		pokedexPage.setName("pokedex");
		gameWindow.add(pokedexPage, "pokedex");
		layout.show(gameWindow.getContentPane(), "pokedex");
	}

	/**
	 * Changes visible panel and shows page MyTeam to user 
	 * 

	 */
	public void showMyTeam() {
		updateLastPageID();
		loadMyTeamPage();
		layout.show(gameWindow.getContentPane(), "myTeam");
	}

	/**
	 * Loads and shows a new CombatPage using information given by Trainers in param
	 * @param ComputerFight battle
	 */
	public void showComputerBattle(ComputerFight battle) {
		updateLastPageID();
		computerBattlePanel = new ComputerBattlePanel(battle);
		computerBattlePanel.setName("computerCombat");
		gameWindow.add(computerBattlePanel, "computerCombat");
		layout.show(gameWindow.getContentPane(), "computerCombat");
	}
	
	public void showMultiplayerBattle(Fight battle) {
		updateLastPageID();
		multiplayerBattlePanel = new MultiplayerBattlePanel(battle);
		multiplayerBattlePanel.setName("multiplayerCombat");
		gameWindow.add(multiplayerBattlePanel, "multiplayerCombat");
		layout.show(gameWindow.getContentPane(), "multiplayerCombat");
	}
	
	public void showLeagueBattle(League league) {
		updateLastPageID();
		computerBattlePanel = new ComputerBattlePanel(league.getFight());
		computerBattlePanel.setName("computerCombat");
		gameWindow.add(computerBattlePanel, "computerCombat");
		layout.show(gameWindow.getContentPane(), "computerCombat");
	}

	/**
	 * Changes visible Panel and shows LeaguePage to user
	 * 

	 */
	public void showLeague(League league) {
		updateLastPageID();
		leaguePage = new LeaguePanel(league);
		leaguePage.setName("league");
		gameWindow.add(leaguePage, "league");
		layout.show(gameWindow.getContentPane(), "league");
	}

	/**
	 * Loads and shows a new Pokemon About page to a given pokemon
	 * @param pokemonID

	 */
	public void showPokemonPage(int pokemonID) {
		updateLastPageID();
		pokemonPage = new PokemonPanel(pokemonID);
		pokemonPage.setName("pokemon");
		gameWindow.add(pokemonPage, "pokemon");
		layout.show(gameWindow.getContentPane(), "pokemon");
	}

	/**
	 * Loads and shows a page about a given Pokemon Attacks
	 * @param pokemon

	 */
	public void showPokemonAttacksPage(Pokemon pokemon) {
		updateLastPageID();
		pokemonAttacksPage = new PokemonAttacksPanel(pokemon);
		pokemonAttacksPage.setName("pokemonAttacks");
		gameWindow.add(pokemonAttacksPage, "pokemonAttacks");
		layout.show(gameWindow.getContentPane(), "pokemonAttacks");
	}

	/**
	 * Initialises a TeamPage for user
	 * 

	 */
	public void loadMyTeamPage() {
		myTeamPage = new MyTeamPanel();
		myTeamPage.setName("myTeam");
		gameWindow.add(myTeamPage, "myTeam");
	}

	/**
	 * Refresh information in MyTeamPage for user
	 * 

	 */
	public void refreshMyTeamPage() {
		loadMyTeamPage();
		layout.show(gameWindow.getContentPane(), "myTeam");
	}

	/**
	 * Allows to change visible panel to the last one visible (back)
	 * 

	 */
	public void showLastPage() {
		loadMyTeamPage();
		layout.show(gameWindow.getContentPane(), lastPageID);
		lastPageID = "menu";
	}

	/**
	 * Internal method to update last page visited
	 * 

	 */
	private void updateLastPageID() {
		for (Component comp : gameWindow.getContentPane().getComponents()) {
			if (comp.isVisible() == true) {
				JPanel card = (JPanel) comp;
				if (lastPageID == null || !lastPageID.equals(card.getName())) {
					lastPageID = card.getName();
				}
			}
		}
	}

	public void updateComputerBattlePanel(ComputerFight fight) {
		if (fight.ended()) {
			if (fight.wins()) {
				showDialogMessage("You won!");
			} else {
				showDialogMessage("You lost!");
			}
			showMenu();
			return ;
		}
		showComputerBattle(fight);
	}
	
	public void updateMultiplayerBattlePanel(Fight fight) {
		if (fight.ended()) {
			if (fight.wins()) {
				showDialogMessage("Player 1 won!");
			} else {
				showDialogMessage("Player 2 won!");
			}
			showMenu();
			return ;
		}
		showMultiplayerBattle(fight);
	}

	public void updateLeaguePanel(League league) {
		if (league.getFight().ended()) {
			if (league.getFight().wins()) {
				if (league.hasNextFight()) {
					league.nextFight();
					showLeagueBattle(league);
				} else {
					showDialogMessage("You won!");
					showMenu();
				}
			} else {
				showDialogMessage("You lost!");
				showMenu();
			}
		} else {
			showLeagueBattle(league);
		}
	}
}
