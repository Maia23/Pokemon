package model.capacities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Loader;
import model.Model;
import model.type.Type;

public class CapacityLoader implements Loader {
	
	/**
	 * Singleton which load capacities from Model.movesFile path

	 */
	
	private CapacityLoader() {}
	 
    private static CapacityLoader capacityLoader = new CapacityLoader();
    
    private static final String[] headings = {"Name", "Power", "Type"};

	public static CapacityLoader getInstance() {   
    	return capacityLoader;
    }
	
	private final Map<String, Capacity> capacities = new HashMap<String, Capacity>();
	
	 public static String[] getHeadings() {
			return headings;
	}
	
	/**
	* Load capacities from Model.movesFile path

	*/
	public void load() {
		
		try (
				BufferedReader csvReader = new BufferedReader(new FileReader(Model.movesFile));
			) {
				String row = csvReader.readLine();
				
				while ((row = csvReader.readLine()) != null) {
					String[] data = row.split(",");
				    int power = Model.minPowerCapacity, accuracy = 0;
				    if (!data[3].equals("")) {
				    	power = Integer.parseInt(data[3]);
				    	if (power < Model.minPowerCapacity)
				    		power = Model.minPowerCapacity;
				    	if (Model.maxPowerCapacity < power)
				    		power = Model.maxPowerCapacity;
				    }
				    if (!data[5].equals(""))
				    	accuracy = Integer.parseInt(data[5]);
				    add(new Capacity(data[1], Type.valueOf(data[2]), power , Integer.parseInt(data[4]), accuracy, CapacityType.valueOf(data[6])));
				}
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Add capacities
	 * @param Capacity capacity
	 * @throws IllegalArgumentException if capacity already created
	 * @throw NullPointerException if capacity is null

	 */
	private void add(Capacity capacity) {
		capacities.compute(Objects.requireNonNull(capacity).getName(), (_name, _capacity) -> {
			if (_capacity != null) 
				throw new IllegalArgumentException("Capacity already created");
			return capacity;
		});
	}
	
	/**
	 * Get capacities that corresponds with the predicate
	 * @param Predicate predicate
	 * @throws NullPointerException if predicate is null

	 */
	public List<Capacity> getCapacity(Predicate<Capacity> p) {
		return capacities.values().stream().filter(Objects.requireNonNull(p)).collect(Collectors.toList());
	}
	
	/**
	 * Get a capacities capacityName name
	 * @param String predicate
	 * @throws NullPointerException if capacityName is null

	 */
	public Capacity getCapacity(String capacityName) {
		return capacities.get(Objects.requireNonNull(capacityName));
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Capacities\n");
		capacities.values().stream().forEach((capacity) -> {
			res.append(capacity.toString());
			res.append("\n");
		});
		return res.toString();
	}
	
}
