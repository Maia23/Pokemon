package model.pokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Model;
import model.capacities.Capacity;
import model.type.Type;
import model.type.TypeLoader;

public final class Pokemon extends PokemonModel implements Serializable {
	
	/**
	 * Pokemon ready to fight
	 */

	private static final long serialVersionUID = 6933897162139706376L;

	private int level = 1;
	private int experience = 0;
	private int life;
	private final int maxLifePoints;

	private final int attack;
	private final int defense;
	private final int specialAttack;
	private final int specialDefense;

	private final int speed;

	private String nickname;

	private List<Capacity> capacities = new ArrayList<Capacity>();

	private final static DamagesCalculator damagesCalculator = new DamagesCalculator();
	
	public Pokemon(int id, String name, String imagePath, int height, int weight, Type type1, Type type2, int life,
			int attack, int defense, int specialAttack, int specialDefense, int speed, String nickname) {
		super(id, name, imagePath, height, weight, type1, type2);
		
		this.life = requirePositive(life, "life");
		maxLifePoints = life;
		this.attack = requirePositive(attack, "attack");
		this.defense = requirePositive(defense, "defense");
		this.specialAttack = requirePositive(specialAttack, "specialAttack");
		this.specialDefense = requirePositive(specialDefense, "specialDefense");
		this.speed = requirePositive(speed, "speed");
		this.nickname = nickname == null ? null : nickname.substring(0,1).toUpperCase() + nickname.substring(1).toLowerCase();
	}
	
	private int requirePositive(int i, String name) {
		if (i < 0)
			throw new IllegalArgumentException(Objects.requireNonNull(name) + " must be positive!");
		return i;
	}
	
	/**
	 * Return the classical name of the pokemon or the nickname if modified
	 * @return String name

	 */
	@Override
	public String getName() {
		if (nickname != null)
			return nickname;
		return super.getName();
	}
	
	/**
	 * Set name of the pokemon or the nickname

	 */
	public void setName(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Return the lifepoints of the pokemon
	 * @return integer life

	 */
	public int getLifePoints() {
		return life;
	}
	
	/**
	 * Return the maximum of lifepoints of the pokemon
	 * @return integer life

	 */
	public int getMaxLifePoints() {
		return maxLifePoints;
	}

	/**
	 * Return the mastered capacities. They can be used in fights.
	 * @return Capacity List masteredCapacities

	 */
	public List<Capacity> getMasteredCapacities() {
		return List.copyOf(capacities);
	}
	
	/**
	 * Return a random mastered capacities. It can be used in fights.
	 * @return Capacity masteredCapacity

	 */
	public Capacity getRandomMasteredCapacity() {
		return capacities.get(Model.randint(0, Model.maxAttacksByPokemon - 1));
	}

	/**
	 * Pokemon forget all capacities and learn new Model.nbAttacksByPokemon random attacks.

	 */
	public void learnRandomCapacities() {
		List<Capacity> possibleCapacities = getPossibleCapacities();
		int length = possibleCapacities.size() - 1;
		
		if (!(Model.maxAttacksByPokemon <= length))
			throw new IllegalStateException("Pokemon don't have enough possible capacities!");
		
		capacities = new ArrayList<Capacity>();
		while (capacities.size() != Model.maxAttacksByPokemon) {
			Capacity capacity = possibleCapacities.get(Model.randint(0, length));
			if (!capacities.contains(capacity))
				learnCapacity(capacity);
		}
	}
	
	/**
	 * Pokeman can learn this capacity ? Throw exception if not
	 * @param Capacity capacity Capacity to learn
	 * @throws IllegalStateException if pokemon is dead
	 * @throws IllegalStateException if pokemon can't learn more capacities
	 * @throws IllegalArgumentException if capacity already added
	 * @throws IllegalArgumentException if capacity can't be learned (cf pokemonModel)
	 * @throws NullPointerException if capacity is null
	 * @return Capacity capacity Initial capacity if valid

	 */
	private Capacity requireLearnableCapacity(Capacity capacity) {
		if (isDead())
			throw new IllegalStateException("Pokemon is dead!");
		if (!(capacities.size() <= Model.maxAttacksByPokemon))
			throw new IllegalStateException("Pokemon can't learn more capacities!");
		if (capacities.contains(Objects.requireNonNull(capacity)))
			throw new IllegalArgumentException("Capacity already added!");
		if (!getPossibleCapacities().contains(capacity))
			throw new IllegalArgumentException("Capacity can't be learned by this pokemon!");
		return capacity;
	}

	/**
	 * Learn a new capacity
	 * @param Capacity capacity Capacity to learn
	 * @throws IllegalStateException if pokemon is dead
	 * @throws IllegalStateException if pokemon can't learn more capacities
	 * @throws IllegalArgumentException if capacity already added
	 * @throws IllegalArgumentException if capacity can't be learned (cf pokemonModel)
	 * @throws NullPointerException if capacity is null

	 */
	public void learnCapacity(Capacity capacity) {
		capacities.add(requireLearnableCapacity(capacity));
	}

	/**
	 * Learn a new capacity and forget oldCapacity
	 * @param Capacity oldCapacity Capacity to forgot
	 * @param Capacity newCapacity Capacity to learn
	 * @throws IllegalStateException if pokemon is dead
	 * @throws IllegalStateException if pokemon can't learn more capacities
	 * @throws IllegalArgumentException if capacity already added
	 * @throws IllegalArgumentException if capacity can't be learned (cf pokemonModel)
	 * @throws NullPointerException if capacity is null

	 */
	public void learnAndForgetCapacity(Capacity oldCapacity, Capacity newCapacity) {
		requireLearnableCapacity(newCapacity);
		if (!capacities.contains(Objects.requireNonNull(oldCapacity)))
			throw new IllegalArgumentException("Capacity not learned!");
		capacities.remove(oldCapacity);
		capacities.add(newCapacity);
	}
	
	/**
	 * Forget a capacity already mastered
	 * @param Capacity capacity Capacity to forget
	 * @throws IllegalStateException if pokemon is dead
	 * @throws IllegalArgumentException if capacity can't be learned (cf pokemonModel)
	 * @throws NullPointerException if capacity is null

	 */
	public void forgetCapacity(Capacity capacity) {
		if (isDead())
			throw new IllegalStateException("Pokemon is dead!");
		if (!capacities.contains(Objects.requireNonNull(capacity)))
			throw new IllegalArgumentException("Capacity not learned!");
		capacities.remove(capacity);
	}

	/**
	 * Prevent the creation of a Pokemon from another Pokemon that is not a PokemonModel 
	 * @deprecated

	 */
	@Override
	public Pokemon create(String nickname) {
		return null;
	}

	/**
	 * Learn a new capacity and forget oldCapacity
	 * @param Capacity capacity Capacity to learn
	 * @param Pokemon victim Victim to attack
	 * @return integer damages Damages caused to victim
	 * @throws IllegalStateException if attacker or victim is dead
	 * @throws IllegalArgumentException if capacity not mastered
	 * @throws NullPointerException if victim or capacity is null

	 */
	public int attack(Pokemon victim, Capacity capacity) {
		if (isDead() || Objects.requireNonNull(victim).isDead())
			throw new IllegalStateException("Pokemon is dead!");
		
		if (!capacities.contains(Objects.requireNonNull(capacity)))
			throw new IllegalArgumentException("Capacity not mastered!");
		
		if (capacity.failed())
			return 0;
		
		int damages = damagesCalculator.getDamages(level, attack, capacity.getPower(), victim.defense, TypeLoader.getInstance().getConstant(getTypeCells(victim)));
		victim.life -= damages;
		if (victim.life <= 0)
			victim.life = 0;
		return damages;
	}

	/**
	 * Pokemon is dead ?
	 * @return true is life is inferior to 0, false else

	 */
	public boolean isDead() {
		return life <= 0;
	}
	
	public void heal() {
		this.life = this.maxLifePoints;
	}

	/**
	 * Pokemon p1 is faster than another Pokemon p2 ?
	 * @param Pokemon pokemon
	 * @return true if p1 faster, false else
	 * @throws NullPointerException if Pokemon p2 is null
	 */
	public boolean isFasterThan(Pokemon pokemon) {
		return Objects.requireNonNull(pokemon).speed < speed;
	}

	/**
	 * Return true if pokemon mastered this capacity
	 * @param Capacity capacity
	 * @return true if mastered, false else
	 * @throws NullPointerException if capacity is null

	 */
	public boolean mastered(Capacity capacity) {
		return capacities.contains(Objects.requireNonNull(capacity));
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder(super.toString());
		res.append(" : ");
		res.append(life);
		res.append(" pv ");
		/*
		//Print attacks
		res.append(", attacks : ");
		capacities.forEach(name -> {
			res.append(name);
			res.append(" ");
		});
		 */
		return res.toString();
	}
}
