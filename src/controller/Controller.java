package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Loader;
import model.Model;
import model.capacities.CapacityLoader;
import model.capacities.Capacity;
import model.fight.ComputerFight;
import model.fight.Fight;
import model.fight.FightTurn;
import model.league.League;
import model.pokedex.Pokedex;
import model.pokemon.Pokemon;
import model.trainer.UserTrainer;
import model.type.TypeLoader;
import view.ViewManager;

public class Controller {

	/**
	 * MVC : Controller
	 */

	private static Controller controller = new Controller();

	public static Controller getInstance() {
		return controller;
	}

	private final ViewManager viewManager;
	private final Pokedex pokedex;

	private UserTrainer userTrainer;

	public UserTrainer getUserTrainer() {
		return userTrainer;
	}

	/**
	 * Loads main data for Controller to work
	 */
	private Controller() {

		viewManager = ViewManager.getInstance();
		pokedex = Pokedex.getInstance();

		List<Loader> loaders = new ArrayList<Loader>() {
			private static final long serialVersionUID = 1287360205032979558L;
			{
				add(pokedex);
				add(CapacityLoader.getInstance());
				add(TypeLoader.getInstance());
				add(viewManager);
			}
		};

		for (Loader loader : loaders)
			loader.load();

		userTrainer = new UserTrainer();
	}


	/**
	 * Allows the creation of a Pokemon Instance based on a PokemonModel
	 * @param int id
	 */
	public void pokemonCreation(int id) {
		userTrainer.add(pokedex.get(Integer.valueOf(id)).create(null));
	}

	/**
	 * Remove a pokemon of the user team
	 * @param int i
	 */
	public void pokemonRemove(int i) {
		userTrainer.remove(i);
	}

	/**
	 * Rename a pokemon of the user team
	 * @param int index
	 * @param String name
	 */
	public void editPokemonName(int index, String name) {
		userTrainer.get(index).setName(name);
	}

	/**
	 * Gives a given capacity to a given pokemon
	 * @param Pokemon pokemon
	 * @param Capacity capacity
	 */
	public void capacityCreation(Pokemon pokemon, Capacity capacity) {
		pokemon.learnCapacity(capacity);
	}

	/**
	 * Removes a capacity from a given Pokemon
	 * @param Pokemon pokemon
	 * @param Capacity capacity
	 */
	public void forgetCapacity(Pokemon pokemon, Capacity capacity) {
		pokemon.forgetCapacity(capacity);
	}

	/**
	 * Loads a previous saved Team
	 */
	public void load() {
		
		UserTrainer beforeLoadState = userTrainer;
		try (
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Model.saveFile));
		) {
			
			userTrainer = (UserTrainer) inputStream.readObject();
		} catch (Exception e) {
			System.out.println("Error when loading!");
			userTrainer = beforeLoadState;
			//e.printStackTrace();
		}
		
	}

	/**
	 * Save the team
	 */
	public void save() {
		
		try (
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Model.saveFile));
		) {
			outputStream.writeObject(userTrainer);
		} catch (Exception e) {
			System.out.println("Error when saving!");
		}
		
	}

	/**
	 * Initializes a new fight with a random Trainer
	 * @return true if all conditions were met to start a battle, false otherwise
	 */
	public boolean classicalFight() {

		if (!userTrainer.readyForBattle()) {
			return false;
		}
		
		classicalFight(new ComputerFight(), null);
		return true;
	}

	/**
	 * Fight turn of a classical battle
	 * @return true if all conditions were met to start a battle, false otherwise
	 */
	private void classicalFightAction(ComputerFight fight, Capacity capacity) {

		if (capacity != null && Objects.requireNonNull(fight).getTurn() == FightTurn.user) {
			int damage = fight.attack(capacity);
			fight.setLastActionPlayer(fight.getAttacker().getName() + " attacked using " + capacity.getName() + " dealing " + damage + " damage");
		}

		if (fight.getTurn() == FightTurn.ennemy) {
			computerAttack(fight);
		}
		
	}

	/**
	 * Battle : fight given in parameter
	 * @param ComputerFight fight
	 * @param Capacity capacity
	 */
	public void classicalFight(ComputerFight fight, Capacity capacity) {
		classicalFightAction(fight, capacity);
		viewManager.updateComputerBattlePanel(fight);
	}

	/**
	 * Automatic attack by the computer
	 * @param ComputerFight battle
	 */
	public void computerAttack(ComputerFight battle) {
		if (battle.getTurn() == FightTurn.ennemy) {
			battle.attack();
		}
	}
	
	/**
	 * Initializes a new multiplayer fight with a random Trainer
	 * @return true if all conditions were met to start a battle, false otherwise
	 */
	public boolean multiplayerFight() {
		if (!userTrainer.readyForBattle()) {
			return false;
		} else {
			multiplayerFight(new Fight(), null);
			return true;
		}
	}
	
	/**
	 * Turn of a multiplayer battle
	 * @return true if all conditions were met to start a battle, false otherwise
	 */
	public boolean multiplayerFight(Fight fight, Capacity capacity) {

		if (!userTrainer.readyForBattle()) {
			return false;
		}
		
		multiplayerFightAction(fight, capacity);
		viewManager.updateMultiplayerBattlePanel(fight);
		
		return true;
	}

	/**
	 * Action of a multiplayer battle
	 * @param Fight fight
	 * @param Capacity capacity
	 */
	private void multiplayerFightAction(Fight fight, Capacity capacity) {
		if (capacity != null) {
			fight.attack(capacity);
			if (fight.getTurn() == FightTurn.user) {
				fight.setLastActionPlayer(fight.getAttacker().getName() + " attacked used " + capacity.getName() + " doing " + capacity.getPower() + " damage");
			} else {
				fight.setLastActionComputer(fight.getAttacker().getName() + " attacked used " + capacity.getName() + " doing " + capacity.getPower() + " damage");
			}
		}
		
	}
	
	/**
	 * User team fight a league of 4 trainers having 5 pokemons and a master having 6 pokemons
	 */
	public boolean classicalLeague() {

		if (!userTrainer.readyForBattle()) {
			return false;
		}
		
		viewManager.showLeague(new League(4, 5, 6));
		return true;
	}
	
	/**
	 * User team fight a league of 8 trainers having 6 pokemons and a master having 6 pokemons too
	 */
	public boolean homeMadeLeague() {

		if (!userTrainer.readyForBattle()) {
			return false;
		}
		
		viewManager.showLeague(new League(8, 6, 6));
		return true;
	}

	/**
	 * Next battle for league if exists
	 * @return boolean nextBattleCreated
	 */
	public boolean nextBattle(League league) {
		if (userTrainer.readyForBattle() && league.hasNextFight()) {
			league.nextFight();
			viewManager.updateLeaguePanel(league);
			return true;
		}
		return false;
	}

	/**
	 * Fight turn of a league
	 * @param League league
	 * @param Capacity capacity
	 */
	public void leagueFight(League league, Capacity capacity) {
		classicalFightAction(league.getFight(), capacity);
		viewManager.updateLeaguePanel(league);
	}
	
	/**
	 * Heal the user team if possible
	 * @return true if the pokemons team is empty, false otherwise
	 */
	public boolean heal() {
		if (!userTrainer.isEmpty())
			return false;
		
		userTrainer.heal();
		return true;
	}

	public static void main(String[] args) {

	}
}
