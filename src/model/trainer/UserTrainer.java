package model.trainer;

import java.io.Serializable;

import model.Model;

public class UserTrainer extends Trainer implements Serializable {

	private static final long serialVersionUID = -4849414890679357296L;
	
	public UserTrainer() {
		super("User");
	}
	
	/**
	 * Return true it's impossible to add more pokemons
	 * @return boolean isFull 
	 */
	@Override
	public boolean isFull() {
		return Model.maxPokemonsForUser <= size();
	}
}
