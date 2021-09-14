package model.type;

import java.util.Objects;

public class TypesCell {
	
	/**
	 * Get a constant from two types of an attacker and 1 for defenser. Used for the attack calculation.
	 * @param Type type1 Type 1 of attacker
	 * @param Type type2 (optionnal) Type 2 of attacker
	 * @param Type defense Type 1 of defenser
	 * @throw NullPointerException if type1 or defense is null
	 */
	
	private final Type attack;
	private final Type attack2;
	private final Type defense;
	
	public TypesCell(Type attack1, Type attack2, Type defense) {
		this.attack = Objects.requireNonNull(attack1);
		this.attack2 = attack2;
		this.defense = Objects.requireNonNull(defense);
	}
	
	public TypesCell(Type attack1, Type defense) {
		this(attack1, null, defense);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attack == null) ? 0 : attack.hashCode());
		result = prime * result + ((attack2 == null) ? 0 : attack2.hashCode());
		result = prime * result + ((defense == null) ? 0 : defense.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypesCell other = (TypesCell) obj;
		if (attack != other.attack)
			return false;
		if (attack2 != other.attack2)
			return false;
		if (defense != other.defense)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TypesCell [attack=" + attack + ", attack2=" + attack2 + ", defense=" + defense + "]";
	}
}
