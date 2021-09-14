package model.pokedex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import model.Loader;
import model.Model;
import model.pokemon.Pokemon;
import model.pokemon.PokemonModel;
import model.trainer.Trainer;
import model.type.Type;

public class Pokedex implements Loader, Iterable<PokemonModel> {
	
	/**
	 * Singleton which load pokedex from Model.pokedexFile path
	 */
	
	private Pokedex() {}
 
    private static Pokedex pokedex = new Pokedex();
     
    public static Pokedex getInstance() {   
    	return pokedex;
    }
	
	private final List<PokemonModel> pokemons = new ArrayList<PokemonModel>();
	
	private final String[] headings = {"ID", "Name", "Weight", "Type"};
	
	public String[] getHeadings() {
		return headings;
	}
	
	/**
	 * Load pokemon models from Model.pokedexFile path
	 */
	public void load() {
		try (
				BufferedReader csvReader = new BufferedReader(new FileReader(Model.pokedexFile));
			) {
				String row = csvReader.readLine();
				while ((row = csvReader.readLine()) != null) {
				    String[] data = row.split(",");
				    add(new PokemonModel(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]), Type.valueOf(data[5]), data.length == 7 ? Type.valueOf(data[6]) : null));
				}
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Add pokemon model
	 * @param PokemonModel pokemonModel
	 * @throws IllegalArgumentException if pokemon model already created
	 * @throw NullPointerException if pokemonModel is null
	 */
	private void add(PokemonModel pokemonModel) {
		if (pokemons.contains(pokemonModel))
			throw new IllegalArgumentException("Pokemon already in Pokedex");
		pokemons.add(Objects.requireNonNull(pokemonModel));
	}
	
	/**
	 * Get pokemon model that corresponds at the id
	 * @param int i
	 * @return PokemonModel pokemonModel
	 * @throws IllegalArgumentException if index out of bounds
	 */
	public PokemonModel get(int i) {
		if (!(0 <= i - 1 && i - 1 <= pokemons.size()))
			throw new IllegalArgumentException("Index out of range");
		return pokemons.get(i - 1);
	}
	
	/**
	 * Generate a random trainer of numberOfElements pokemons with random capacities
	 * @param int numberOfElements Number of pokemons
	 * @return Trainer trainer
	 * @throws IllegalArgumentException if numberOfElements is negative
	 */
	public Trainer getRandomTrainer(int numberOfElements) {	
		if (numberOfElements < 0)
			throw new IllegalArgumentException("Number of pokemons can't be negative!");
		
		List<Pokemon> res = new ArrayList<Pokemon>();
		int length = pokemons.size() - 1;
		
		for (int i = 0; i < numberOfElements; i++) {
			Pokemon pokemon = pokemons.get(Model.randint(0, length)).create(null);
	        pokemon.learnRandomCapacities();
	        res.add(pokemon);
	    }
		return new Trainer(res);
	}
	
	/**
	 * Iterate into the pokemonModels list
	 * @return PokemonModel Iterator
	 */
	@Override
	public Iterator<PokemonModel> iterator() {
		return pokemons.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Pokedex\n");
		pokemons.stream().forEach((pokemon) -> {
			res.append(pokemon.toString());
			res.append("\n");
		});
		return res.toString();
	}

	/**
	 * Return the number of pokemons loaded
	 * @return int pokemonNb
	 */
	public int size() {
		return pokemons.size();
	}
}
