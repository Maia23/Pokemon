package model.capacities;

import model.Model;
import model.type.Type;
import view.ViewManager;

import java.io.Serializable;
import java.util.Objects;

public class Capacity implements Serializable {
	
	/**
	 * Capacity of a pokemon used in fights

	*/
	
	private static final long serialVersionUID = -1959246413772456271L;
	
	private final String name;
	private final Type type;
	private final int power;
	private final int precision;
	private final int accuracy;
	private final CapacityType capacityType;
	
	/**
	 * Generate a random integer between a and b.
	 * @param int a, int b
	 * @return random integer
	 * @throws IllegalArgumentException precision is not a percentage
	 * @throws IllegalArgumentException accuracy is not a percentage
	 * @throws IllegalArgumentException !(Model.minPowerCapacity <= power && power <= Model.maxPowerCapacity && power % 5 == 0)

	 */
	public Capacity(String name, Type type, int power, int precision, int accuracy, CapacityType capacityType) {
		
		super();
		if (!(Model.minPowerCapacity <= power && power <= Model.maxPowerCapacity && power % 5 == 0)) {
			ViewManager.getInstance().showErrorMessage("Bad power");
			throw new IllegalArgumentException("Bad power");
		} if (!(0 <= precision && precision <= 100)) {
			ViewManager.getInstance().showErrorMessage("Bad precision");
			throw new IllegalArgumentException("Bad precision");
		} if (!(0 <= accuracy && accuracy <= 100)) {
			ViewManager.getInstance().showErrorMessage("Bad accuracy");
			throw new IllegalArgumentException("Bad accuracy");
		}
		
		this.name = Objects.requireNonNull(name);
		this.type = Objects.requireNonNull(type);
		this.power = power;
		this.precision = precision;
		this.accuracy = accuracy;
		this.capacityType = Objects.requireNonNull(capacityType);
	}
	
	/**
	 * Getter : pokemon name
	 * @return String name

	 */
	public String getName() {
		return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	}
	
	/**
	 * Getter : pokemon type
	 * @return Type type

	 */
	public Type getType() {	
		return type;
	}
	
	/**
	 * Getter : pokemon power (between 15 and 300 and / by 5)
	 * @return int power

	 */
	public int getPower() {
		return power;
	}

	@Override
	public String toString() {
		return name +  " (type: " + type + ", power: " + power + ", precision: " + precision + ", accuracy: " + accuracy + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Capacity other = (Capacity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Attack failed ? Depends of the pokemon precision.
	 * @return boolean attackFailed

	 */
	public boolean failed() {
		return Model.randint(0, 100) <= precision;
	}
}