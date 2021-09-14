package model;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
public class Model {
	
	/**
	 * MVC : Model
	 * Contains static constants and useful methods of the game
	 */
	
	public static final int maxPokemonsByTrainer = 10;
	public static final int defaultTrainerForFightNb = 6;
	
	public static final int maxPokemonsForUser = 6;
	
	public static final int maxAttacksByPokemon = 4;
	
	public static final int minPowerCapacity = 15;
	public static final int maxPowerCapacity = 300;
	
	
	private static final Random random = new Random();
	
	public static final File saveFile = new File("config/serializable.ser");
	public static final File movesFile = new File("assets/moves.csv");
	public static final File pokedexFile = new File("assets/pokedex.csv");
	public static final File typesFile = new File("assets/grid_types.csv");
	
	private static final DecimalFormat formatter = new DecimalFormat("000");
	
	private static final List<String> fighterNames = List.of("Aurore", "Barbara", "Chrys", "Chen", "Clem", "Conway", "Daisy", "Delia", "Drew", "Flora", "Gilles", "Giovani", "Jacky", "James", "Jenny", "Jessie", "Ondine", "Paul", "Pierre", "Sacha");
	
	private Model() {
		
	}
	
	/**
	 * Generate a random integer between a and b.
	 * @param int a, int b
	 * @return random integer
	 * @throws IllegalArgumentException if b is inferior to a
	 */
	public static int randint(int a, int b) {
		if (b < a)
			throw new IllegalArgumentException("Int a must be inferior to int b!");
		return a + random.nextInt(b - a + 1);
	}
	
	/**
	 * Format an integer for length 3.
	 * Ex : 1 -> 001
	 * @param int i
	 * @return String for i with length 3
	 */
	public static String format(int i) {
		return formatter.format(i);
	}

	/**
	 * Return a random name.
	 * @return String name
	 */
	public static String randomName() {
		return fighterNames.get(randint(0, fighterNames.size() - 1));
	}
}
