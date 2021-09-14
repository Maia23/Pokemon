package model.league;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import model.fight.ComputerFight;
import model.pokedex.Pokedex;
import model.trainer.Trainer;

public class League implements Iterable<Trainer> {
	
	/**
	 * League of pokemons : set of multiple fights that the player must done
	 */
	
	private final List<Trainer> ennemies;
	private int fightNumber = -1;
	private ComputerFight fight = null;


	private final String[] headings = {"Number", "Name", "Nb Pokemons"};
	
	public String[] getHeadings() {
		return headings;
	}

	/**
	 * Create a random league
	 * @param Trainer player User pokemons
	 * @param int fightNumber Number of fights
	 * @param int pokemonsNumber Pokemon Number for each fighters 
	 * @param int leagueMasterPokemonNumber Pokemon Number for the league master
	 * @throws IllegalArgumentException if fightNumber is negative 0 or pokemonsNumber is negative or leagueMasterPokemonNumber is negative
	 * @throws NullPointerException if player is null
	 */
	public League(int fightNumber, int pokemonsNumber, int leagueMasterPokemonNumber) {
		
		if (fightNumber <= 0 || pokemonsNumber <= 0 || leagueMasterPokemonNumber <= 0)
			throw new IllegalArgumentException();
		
		ennemies = new ArrayList<Trainer>();
		Pokedex pokedex = Pokedex.getInstance();
		for (int i = 0; i < fightNumber; i++) {
			ennemies.add(pokedex.getRandomTrainer(pokemonsNumber));
		}
		ennemies.add(pokedex.getRandomTrainer(leagueMasterPokemonNumber));
	}
	
	/**
	 * Create a specific league
	 * @param Trainer List fights Pokemons of the fighters and the league master at the end
	 * @throws IllegalArgumentException if fights.size() is inferior to 1
	 * @throws NullPointerException if player or fights is null
	 */
	public League(List<Trainer> fights) {
		if (Objects.requireNonNull(fights).size() <= 1)
			throw new IllegalArgumentException();
		this.ennemies = fights;
	}
	
	/**
	 * Return true if the actual fight ended and there is a next fight
	 * @throws IllegalStateException if fight not ended
	 * @return true : has next fight, false : league ended
	 */
	public boolean hasNextFight() {
		if (fight == null)
			return 0 <= fightNumber + 1 && fightNumber + 1 < ennemies.size();
		if (!fight.ended())
			throw new IllegalStateException("Fight not ended!");
		return fight.wins() && (0 <= fightNumber + 1 && fightNumber + 1 < ennemies.size());
	}
	
	/**
	 * Return the next fight
	 * @return ComputerFight fight : return the next fight
	 * @throws IllegalStateException if there is no more fights
	 */
	public ComputerFight nextFight() {
		if (!hasNextFight())
			throw new IllegalStateException("No more fights!");
		fightNumber++;
		fight = new ComputerFight(Objects.requireNonNull(ennemies.get(fightNumber)));
		return fight;
	}
	
	/**
	 * Return if the user wins
	 * @return true : victory, false : defeat
	 */
	public boolean wins() {
		return fightNumber == ennemies.size() - 1 && fight.wins(); 
	}
	
	/**
	 * Return the actual fight number
	 * @return integer fightNumber
	 */
	public int getFightNumber() {
		return fightNumber;
	}
	
	/*
	 * Return the actual fight
	 * @return ComputerFight fight
	 */
	public ComputerFight getFight() {
		return fight;
	}

	/*
	 * Iterate over fights
	 * @return ComputerFight Iterator
	 */
	@Override
	public Iterator<Trainer> iterator() {
		return ennemies.iterator();
	}
	
	/*
	 * Return the number of fights
	 * @return int fightNumber
	 */
	public int size() {
		return ennemies.size();
	}
}
