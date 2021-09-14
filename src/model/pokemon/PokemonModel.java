package model.pokemon;

import model.Model;
import model.capacities.CapacityLoader;
import model.capacities.Capacity;
import model.type.Type;
import model.type.TypesCell;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PokemonModel implements Serializable {
	
	 /**
	 * Model of a pokemon. It don't have capacities, life, experience, etc...
	 * Used to create pokemons and stocked into pokedex
	 */
	 
	private static final long serialVersionUID = 8946730638591286262L;
	
	private final int id;
	private final String name;
	
	private final String imagePath;
	
	private final int height;
	private final int weight;
	
	private final Type type1;
	private final Type type2;
	
	public PokemonModel(int id, String name, String imagePath, int height, int weight, Type type1, Type type2) {
		super();
		this.id = id;
		this.name = Objects.requireNonNull(name).substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
		this.imagePath = Objects.requireNonNull(imagePath);
		this.height = height;
		this.weight = weight;
		this.type1 = Objects.requireNonNull(type1);
		this.type2 = type2;
	}
	
	public PokemonModel(int id, String name, String imagePath, int height, int weight, Type type1) {
		this(id, name, imagePath, height, weight, type1, null);
	}
	
	/**
	 * Get pokemon id (used in pictures)
	 * @return integer id
	 */
	public String getId() {
		return Model.format(id);
	}
	
	/**
	 * Get classical pokemon name (ex: Pikachu)
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get pokemon image path
	 * @return String path
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Get pokemon height
	 * @return integer height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get pokemon weight
	 * @return integer weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Get pokemon type(s) (1 or 2).
	 * @return String type
	 */
	public String getType() {
		return type1.toString().toUpperCase() + (type2 != null ? " " + type2.toString().toUpperCase() : "");
	}

	/**
	 * Get pokemon typeCells. Used in the damages calculation.
	 * @return String type
	 */
	public TypesCell getTypeCells(PokemonModel victim) {
		return new TypesCell(type1, type2, victim.type1);
	}
	
	/**
	 * Return all capacities that can lean a pokemon
	 * @return Capacity List capacities
	 */
	public List<Capacity> getPossibleCapacities() {
		return CapacityLoader.getInstance().getCapacity(capacity -> {
			Type _type = capacity.getType();
			if (type2 == null)
				return _type.equals(Type.normal) || _type.equals(type1);
			return _type.equals(Type.normal) || _type.equals(type1) || _type.equals(type2);
		});
	}
		
	/**
	 * Create a pokemon that corresponds to the pokemonModel
	 * @param String nickname
	 * @return Pokemon pokemon
	 */
	public Pokemon create(String nickname) {
		return new Pokemon(id, name, imagePath, height, weight, type1,  type2, Model.randint(500, 1000), Model.randint(300, 700), Model.randint(25, 100), Model.randint(150, 250), Model.randint(125, 225), Model.randint(1, 10), nickname);
		
	}
	
	@Override
	public String toString() {
		return getName() + " [" + id + "]"; 
	}
}
