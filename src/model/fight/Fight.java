package model.fight;

import java.util.Objects;

import controller.Controller;
import model.Model;
import model.capacities.Capacity;
import model.pokedex.Pokedex;
import model.pokemon.Pokemon;
import model.trainer.Trainer;

public class Fight {
	
	/**
	 * Fight between Controller.getInstance().getUserTrainer() and a ennemy that can be random
	 */
	private final Trainer ennemy;
	private FightTurn turn;
	
	private String lastActionPlayer = "";
	private String lastActionComputer = "";
	
	public Fight() {
		this(Pokedex.getInstance().getRandomTrainer(Model.defaultTrainerForFightNb));
	}
	
	public Fight(Trainer ennemy) {
		Objects.requireNonNull(Controller.getInstance().getUserTrainer());
		this.ennemy = Objects.requireNonNull(ennemy);
		fastest();
	}
	
	/**
	 * Select the fastest pokemon
	 */
	private void fastest() {
		Pokemon userPokemon = Objects.requireNonNull(Controller.getInstance().getUserTrainer()).getAttacker();
		Pokemon ennemyPokemon = this.ennemy.getAttacker();
		this.turn = userPokemon.isFasterThan(ennemyPokemon) ? FightTurn.user : FightTurn.ennemy;
	}
	
	/**
	 * Change the turn
	 */
	private void next() {
		if (Objects.requireNonNull(Controller.getInstance().getUserTrainer()).isEmpty()) {
			turn = FightTurn.ennemyVictory;
			return ;
		}
		
		if (ennemy.isEmpty()) {
			turn = FightTurn.userVictory;
			return ;
		}
					
		this.turn = turn.equals(FightTurn.user) ? FightTurn.ennemy : FightTurn.user;
	}
	
	/**
	 * Ensures the fight is not ended
	 * @throws IllegalStateException if the fight already ended

	 */
	private void requireNonEnded() {
		if (turn.ended())
			throw new IllegalStateException("Fight already ended!");
	}
	
	/**
	 * The trainer ready to fight
	 * @return Trainer trainer The trainer ready to fight
	 * @throws IllegalStateException if the fight already ended
	 */
	public Trainer getAttacker() {
		requireNonEnded();
		return turn.equals(FightTurn.user) ? Objects.requireNonNull(Controller.getInstance().getUserTrainer()) : ennemy;
	}
	
	/**
	 * The Trainer which is about to be attacked
	 * @return Trainer trainer The trainer which is about to be attacked
	 * @throws IllegalStateException if the fight already ended
	 */
	public Trainer getDefenser() {
		requireNonEnded(); 
		return turn.equals(FightTurn.user) ? ennemy : Objects.requireNonNull(Controller.getInstance().getUserTrainer());
	}
	
	/**
	 * Active team attack non active team with the capacity given in parameter
	 * @param Capacity capacity Capacity used to attack
	 * @return Damages done
	 * @throws IllegalStateException if the fight already ended
	 * @throws NullPointerException if capacity is null
	 */
	public int attack(Capacity capacity) {
		requireNonEnded(); 
		
		Trainer attacker = getAttacker();
		Trainer defenser = getDefenser();
		int size = defenser.size();
		
		int damages = attacker.attack(defenser, Objects.requireNonNull(capacity));
		
		this.lastActionPlayer = getAttacker().getName() + " attacked using " + capacity.getName() + " dealing " + damages + " damages";
		
		if (getDefenser().isEmpty()) {
			next();
		} else if (getDefenser().size() != size) {
			next();
			fastest();
		} else {
			next();
		}
		
		return damages;
	}
	
	/**
	 * Active team give up
	 * @throws IllegalStateException if the fight already ended
	 */
	public void giveUp() {
		requireNonEnded();
		if (ennemy.equals(getAttacker())) {
			turn = FightTurn.userVictory;
		} else {
			turn = FightTurn.ennemyVictory;
		}
	}
	
	/**
	 * Return the state of the fight
	 * @return FightTurn.user : user plays, FightTurn.ennemy : computer plays, FightTurn.userVictory : user wins or FightTurn.ennemyVictory : user lost
	 */
	public FightTurn getTurn() {
		return turn;
	}
	
	/**
	 * Change the active attacker
	 * @param Pokemon pokemon Pokemon which replace active pokemon
	 * @throws IllegalStateException if the fight already ended
	 * @throws NullPointerException if pokemon is null

	 */
	public void changeAttacker(Pokemon pokemon) {
		requireNonEnded();
		getAttacker().changeAttacker(pokemon);
		next();
		fastest();
	}
	
	/**
	 * Return true if the fight ended
	 * @return boolean ended
	 */
	public boolean ended() {
		return turn.ended();
	}
	
	/**
	 * Return true if the user wins
	 * @return boolean wins

	 */
	public boolean wins() {
		return turn == FightTurn.userVictory;
	}

	/**
	 * Return the ennemy trainer
	 * @return Trainer ennemy
	 */
	public Trainer getEnnemy() {
		return ennemy;
	}

	// Used for view
	
	public String getLastActionPlayer() {
		return lastActionPlayer;
	}

	public String getLastActionComputer() {
		return lastActionComputer;
	}

	public void setLastActionPlayer(String lastActionPlayer) {
		this.lastActionPlayer = lastActionPlayer;
	}

	public void setLastActionComputer(String lastActionComputer) {
		this.lastActionComputer = lastActionComputer;
	}
	

	
	
}
