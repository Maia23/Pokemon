package model.type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.Loader;
import model.Model;

public class TypeLoader implements Loader {
	
	/**
	 * Loading types for pokemons
	 */
	
	private TypeLoader() {}
	 
    private static TypeLoader types = new TypeLoader();
     
    public static TypeLoader getInstance() {   
    	return types;
    }
    
    private final Map<TypesCell, Double> attackConstants = new HashMap<TypesCell, Double>();
    
    /**
	 * Load types from Model.typesFile path
	 */
    public void load() {
    	try (
				BufferedReader csvReader = new BufferedReader(new FileReader(Model.typesFile));
			) {
				String row = csvReader.readLine();
				String[] columns = row.split(",");
				int length = columns.length;
				while ((row = csvReader.readLine()) != null) {
				    String[] data = row.split(",");
				    for (int i = 2; i < length; i++) {
				    	if (data[1].equals(" ")) {
				    		add(new TypesCell(Type.valueOf(data[0].trim().toLowerCase()), Type.valueOf(columns[i].trim().toLowerCase())), Double.parseDouble(data[i].trim().toLowerCase()));
				    	} else { 
			    			add(new TypesCell(Type.valueOf(data[0].trim().toLowerCase()), Type.valueOf(data[1].trim().toLowerCase()), Type.valueOf(columns[i].trim().toLowerCase())), Double.parseDouble(data[i].trim().toLowerCase()));
			    		}	
		    		}
				}
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    
    /**
	 * Add capacities
	 * @param Capacity capacity
	 * @throws IllegalArgumentException if type already created
	 * @throws IllegalArgumentException if constant is negative
	 * @throw NullPointerException if typesCell or constant is null
	 */
	private void add(TypesCell typesCell, Double constant) {
		if (Objects.requireNonNull(constant) < 0)
			throw new IllegalArgumentException("Constant can't be negative!");
			
		attackConstants.compute(Objects.requireNonNull(typesCell), (_typesCell, _constant) -> {
			if (_constant != null) 
				throw new IllegalArgumentException("Type already added");
			return constant;
		});
	}
	
	/**
	 * Get a constant from TypesCell
	 * @param TypesCell typesCell
	 * @throws NullPointerException if typesCell is null
	 */
	public Double getConstant(TypesCell typesCell) {
		return attackConstants.get(Objects.requireNonNull(typesCell));
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Types\n");
		attackConstants.entrySet().stream().forEach((entry) -> {
			res.append(entry.getKey().toString());
			res.append(" --> ");
			res.append(entry.getValue().toString());
			res.append("\n");
		});
		return res.toString();
	}
	
	
}
