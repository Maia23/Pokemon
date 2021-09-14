package model.trainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import model.Model;
import model.capacities.Capacity;
import model.pokemon.Pokemon;

public class Trainer implements Iterable<Pokemon>, Serializable {
	
	/**
	 * A trainer of pokemons : name + team of pokemons
	 */
	
	private static final long serialVersionUID = -1767024274054994588L;
	
	private final List<Pokemon> pokemons;
	private final String name;
	
	public Trainer() {
		this(new ArrayList<Pokemon>());
	}
	
	public Trainer(List<Pokemon> pokemons) {
		this(pokemons, Model.randomName());
	}
	
	public Trainer(String name) {
		this(new ArrayList<Pokemon>(), Objects.requireNonNull(name));
	}
	
	public Trainer(List<Pokemon> pokemons, String name) {
		if (Objects.requireNonNull(pokemons).contains(null))
			throw new IllegalArgumentException();
		
		this.pokemons = pokemons;
		this.name = Objects.requireNonNull(name);
	}

	/**
	 * Iterate into pokemons
	 * @return Pokemon Iterator 
	 */
	@Override
	public Iterator<Pokemon> iterator() {
		return pokemons.iterator();
	}
	
	/**
	 * Change the active pokemon of the list
	 * @return Pokemon Iterator 
	 * @throws IllegalArgumentException if pokemon is not in list
	 * @throws NullPointerException if pokemon is null
	 */
	public void changeAttacker(Pokemon pokemon) {		
		int index = pokemons.indexOf(Objects.requireNonNull(pokemon));
		if (index == -1)
			throw new IllegalArgumentException("Pokemon is not in list!");
		Collections.swap(pokemons, 0, index);
	}
	
	/**
	 * Add a pokemon into the list
	 * @param Pokemon pokemon
	 * @throws NullPointerException if pokemon is null
	 */
	public void add(Pokemon pokemon) {
		if (isFull())
			throw new IllegalStateException("Trainer can't have more pokemons!");
		
		pokemons.add(Objects.requireNonNull(pokemon));
	}
	
	/**
	 * Get pokemon attacker
	 * @return Pokemon pokemon
	 * @throws IllegalStateException if pokemon is index out of bounds
	 */
	public Pokemon getAttacker() {
		return get(0);
	}
	
	/**
	 * Get pokemon that corresponds to the index
	 * @return Pokemon pokemon
	 * @throws IllegalStateException if pokemon is index out of bounds
	 */
	public Pokemon get(int i) {
		outOfRange(i);
		return pokemons.get(i);
	}

	/**
	 * Delete pokemon that corresponds to the index
	 * @throws IllegalStateException if pokemon is index out of bounds
	 */
	public void remove(int i) {
		outOfRange(i);
		pokemons.remove(i);
	}

	/**
	 * Get the number of pokemons
	 * @return integer numberOfPokemons
	 */
	
	public int size() {
		return pokemons.size();
	}
	
	/**
	 * Prevent from index out of bounds
	 * @throws IllegalStateException if pokemon is index out of bounds
	 */
	private void outOfRange(int i) {	
		if (!(0 <= i && i < size())) {
			throw new IllegalStateException("Out of range!");
		}
	}

	/**
	 * Return true if it's impossible to add more pokemons
	 * @return boolean isFull
	 */
	public boolean isFull() {
		return Model.maxPokemonsByTrainer <= size();
	}
	
	/**
	 * Return true if the team isEmpty
	 * @return boolean isEmpty
	 */
	public boolean isEmpty() {
		return pokemons.isEmpty();
	}

	/**
	 * Attack another trainer
	 * @return boolean isEmpty
	 */
	public int attack(Trainer defenser, Capacity capacity) {
		int damages = getAttacker().attack(Objects.requireNonNull(defenser).getAttacker(), Objects.requireNonNull(capacity));
		update();
		defenser.update();
		return damages;
	}
	
	/**
	 * Allows to check if a Trainer any Pokemon without attacks in his Team
	 * @return true if any of his Pokemons has no attacks, false otherwise
	 */
	public boolean readyForBattle() {
		if (pokemons.isEmpty())
			return false;
		for (Pokemon pokemon : pokemons) {
			if (pokemon.getMasteredCapacities().isEmpty())
				return false;
		}
		return true;
	}
	
	/**
	 * Remove pokemons that are dead
	 */
	public void update() {
		int i = 0;
		while (i < pokemons.size()) {
			if (pokemons.get(i).isDead()) {
				pokemons.remove(i);
			} else {
				i++;
			}
		}
	}
	
	/*
	 * Heal the pokemon list
	 */
	public void heal() {
		for (Pokemon pokemon : pokemons) {
			pokemon.heal();
		}
	}

	@Override
	public String toString() {
 		return (name == null ? "Trainer" : name) + " " + pokemons;
	}

	/*
	 * Get the trainer name
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Return the nb of living pokemons of a trainer
	 * @return int pokemonsNb
	 */
	public int getNbPokemons() {
		return pokemons.size();
	}
}
