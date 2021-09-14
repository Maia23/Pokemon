package view.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Model;
import model.capacities.Capacity;
import model.capacities.CapacityLoader;
import model.pokemon.Pokemon;
import view.ViewManager;
import view.util.JUtilities;

/**
 * Display Panel for Managing Pokemon's attacks
 */
public class PokemonAttacksPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Pokemon pokemon;

	private final JPanel topSide = new JPanel(new GridLayout(2,1));
	private final JButton backBtn;
	private final JLabel title = new JLabel();
	private final JScrollPane scrollPane;
	private final JTable table;
	private final JLabel chosenAttacks;
	private final JPanel bottomSide = new JPanel(new GridLayout(2,1));
	private final JButton addAttack;
	private final JButton removeAttack;

	private String[][] data;

	/**
	 * Initializes interface and loads data concerning a given Pokemon
	 * @param pokemon
	 */
	public PokemonAttacksPanel(Pokemon pokemon) {
		this.pokemon = Objects.requireNonNull(pokemon);
		load();
		setBorder(new EmptyBorder(50, 50, 100, 50));
		setLayout(new BorderLayout());

		//Top Side Panel

		//Back btn
		backBtn = new JButton("Back");
		backBtn.setBounds(30, 30, 150, 100);
		topSide.add(backBtn);

		//Title Label
		title.setText("<html><h1>" + pokemon.getName() + " attacks: </h1></html>");
		topSide.add(title);

		add(topSide, BorderLayout.PAGE_START);

		//Bottom Side Panel

		//Add Attack btn
		addAttack = new JButton("Add Attack");
		addAttack.setBounds(150, 150, 300, 150);
		bottomSide.add(addAttack);

		//Add Remove Attack btn
		removeAttack = new JButton("Remove Attack");
		removeAttack.setBounds(150, 150, 300, 150);
		bottomSide.add(removeAttack);

		add(bottomSide, BorderLayout.PAGE_END);

		//Add Chosen Attacks
		chosenAttacks = new JLabel();
		add(chosenAttacks, BorderLayout.EAST);
		loadLearnedAttacks();
		

		//Attacks table
		table = new JTable(data, CapacityLoader.getHeadings());
		table.setVisible(true);
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JUtilities.setCellsAlignment(table, SwingConstants.CENTER);

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showLastPage();
			}
		});

		addAttack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					if (pokemon.getMasteredCapacities().size() >= Model.maxAttacksByPokemon) {
						ViewManager.getInstance().showDialogMessage("This Pokemon already knows " + Model.maxAttacksByPokemon + " attacks!");
					} else {
						String attackName = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
						Capacity capacity = CapacityLoader.getInstance().getCapacity(attackName);
						if (!pokemon.mastered(capacity)) {
							Controller.getInstance().capacityCreation(pokemon, capacity);
							loadLearnedAttacks();
						}
					}
				}
			}
		});

		removeAttack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] attacks = new String[Model.maxAttacksByPokemon];
				int size = pokemon.getMasteredCapacities().size();
				for (int i = 0; i < size; i++)
					attacks[i] = pokemon.getMasteredCapacities().get(i).getName();
				
				String chosenAttack = (String) JOptionPane.showInputDialog(null,"Choose an attack to remove", "Remove Attack",JOptionPane.QUESTION_MESSAGE, null, attacks, attacks[0]);
				if (chosenAttack == null)
					return ;
				Controller.getInstance().forgetCapacity(pokemon, CapacityLoader.getInstance().getCapacity(chosenAttack));
				loadLearnedAttacks();
			}
		});
	}

	/**
	 * Loads all data about attacks for the given Pokemon
	 * 

	 */
	private void load() {
		
		List<Capacity> attacks = pokemon.getPossibleCapacities();
		
		data = new String[attacks.size()][];
		
		for (int i = 0; i < data.length; i++) {
			Capacity capacity = attacks.get(i);
			String[] tmp =  new String[3];
			tmp[0] = capacity.getName();
			tmp[1] = String.valueOf(capacity.getPower());
			tmp[2] = capacity.getType().toString();
			data[i] = tmp;
		}
	}
	
	/**
	 * Refreshes information about learned attacks for the given Pokemon
	 * 

	 */
	private void loadLearnedAttacks() {
		
		chosenAttacks.setText("<html><h1>Chosen Attacks:</h1> <br><br> <ul>");
		
		for (int i = 0; i < pokemon.getMasteredCapacities().size(); i++) {
			String attackName = pokemon.getMasteredCapacities().get(i).getName();
			chosenAttacks.setText(chosenAttacks.getText().concat("<li><b>" + attackName + "<b></li>"));
		}
	}

}
