package view.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


import controller.Controller;
import model.pokedex.Pokedex;
import model.pokemon.PokemonModel;
import view.ViewManager;
import view.util.JUtilities;

/**
 * Standard Display Panel for Pokedex
 */
public class PokedexPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JButton backBtn;
	private final JScrollPane scrollPane;
	private final JTable table;

	private final JPanel bottomSide = new JPanel(new GridLayout(2,1));
	private final JButton infoBtn;
	private final JButton addToTeam;

	private final String[][] data = new String[Pokedex.getInstance().size()][];

	/**
	 * Loads all pokemons and initializes a table with all the information
	 */
	public PokedexPanel() {
		loadData();
		setBorder(new EmptyBorder(50, 50, 150, 50));
		setLayout(new BorderLayout());

		//Back btn
		backBtn = new JButton("Back");
		add(backBtn, BorderLayout.PAGE_START);


		//Pokedex table
		table = new JTable(data, Pokedex.getInstance().getHeadings());
		table.setVisible(true);
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JUtilities.setCellsAlignment(table, SwingConstants.CENTER);

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);


		//More info btn
		infoBtn = new JButton("More Info");
		bottomSide.add(infoBtn);

		//AddToTeam button
		addToTeam = new JButton("Add to my team");
		bottomSide.add(addToTeam);
		
		add(bottomSide, BorderLayout.PAGE_END);


		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showLastPage();
			}
		});

		infoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if (i != -1) {
					ViewManager.getInstance().showPokemonPage(Integer.parseInt((String) table.getModel().getValueAt(i, 0)));
				}
			}
		});
		
		addToTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (Controller.getInstance().getUserTrainer().isFull()) {
					ViewManager.getInstance().showDialogMessage("Your team is full, remove one Pokemon!");
					return ;
				}
				
				int i = table.getSelectedRow();
				if (i != -1) {
					String selectedPokemonID = (String) table.getValueAt(i, 0);
					selectedPokemonID = selectedPokemonID.replaceFirst("^0*", "");
					
					Controller.getInstance().pokemonCreation(Integer.valueOf(selectedPokemonID));
					
					ViewManager.getInstance().showDialogMessage("Pokemon Added!");
					
				}
			}
		});
	}

	/**
	 * Loads all Pokemon information
	 * 

	 */
	public void loadData() {
		
		Pokedex pokedex = Pokedex.getInstance();
		
		int i = 0;
		for (PokemonModel pokemonModel : pokedex) {
			
			String[] tmp =  new String[4];
			tmp[0] = pokemonModel.getId();
			tmp[1] = pokemonModel.getName();
			tmp[2] = String.valueOf(pokemonModel.getWeight());
			tmp[3] = pokemonModel.getType();
			data[i] = tmp;
			i++;
		}
	}

}
