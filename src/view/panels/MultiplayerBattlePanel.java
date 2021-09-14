package view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.capacities.Capacity;
import model.fight.Fight;
import model.fight.FightTurn;
import model.pokemon.Pokemon;
import model.trainer.Trainer;
import view.GameWindow;
import view.ImageLoader;
import view.ViewManager;

/**
 * Display of a standard combat page
 */
public class MultiplayerBattlePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Fight battle;

	private final JButton exitBtn = new JButton("Escape from Battle");

	private final Trainer player1;
	private final Trainer player2;
	
	private final JLabel player1Name = new JLabel();
	private final JLabel player2Name = new JLabel();

	private JPanel firstPlayerPanel;
	private JPanel secondPlayerPanel;

	private final JPanel attacksPanel = new JPanel();

	private final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
	private final JLabel lastActionPlayer = new JLabel();
	private final JLabel lastActionComputer = new JLabel();
	private final JButton changePokemon = new JButton("Switch Pokemon");

	/**
	 * Initializes the entire page using information about both players
	 * @param player
	 * @param opponent
	 */
	public MultiplayerBattlePanel(Fight battle) {
		this.battle = battle;

		this.player1 = Controller.getInstance().getUserTrainer();
		this.player2 = battle.getEnnemy();
		
		player1Name.setText(Controller.getInstance().getUserTrainer().getName());
		player2Name.setText(battle.getEnnemy().getName());

		setBackground(new Color(230, 230, 250));
		setBorder(new EmptyBorder(50, 50, 150, 50));
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		//Init exit button		Cell : 0,0
		gbc.gridx = gbc.gridy = 0;
		gbc.weighty = gbc.weightx = 0.5;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(exitBtn, gbc);

		updateTrainersPanels();

		//Trainer Panel
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		add(firstPlayerPanel, gbc);

		//Opponnent Panel
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 3;

		add(secondPlayerPanel, gbc);

		//Attacks Panel
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.weightx = 4;
		//gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		
		if (battle.getTurn() == FightTurn.user) {
			updateAttacksPanel(player1.getAttacker());
		} else {
			updateAttacksPanel(player2.getAttacker());
		}
		
		add(attacksPanel, gbc);

		attacksPanel.setVisible(true);

		//BottomPanel
		gbc.gridy = 3;
		gbc.gridx = 3;
		gbc.weightx = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		bottomPanel.setBackground(new Color(230, 230, 250));
		add(bottomPanel, gbc);

		//Switch Pokemon btn & infos
		bottomPanel.add(lastActionPlayer);
		bottomPanel.add(lastActionComputer);
		lastActionPlayer.setSize(150, 150);
		lastActionPlayer.setText(battle.getLastActionPlayer());
		lastActionPlayer.setVisible(true);
		lastActionComputer.setSize(150, 150);
		lastActionComputer.setText(battle.getLastActionComputer());
		lastActionComputer.setVisible(true);

		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.NONE;
		add(changePokemon, gbc);

		changePokemon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchPokemon(player1);
			}
		});

		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(GameWindow.getInstance(), "Leaving means you lose the battle, are you sure?");
				if (res == 0)
					ViewManager.getInstance().showMenu();
			}
		});

	}

	/**
	 * Changes interface according to battle results 
	 * @param trainer

	 */
	private void updateTrainersPanels() {

		Pokemon pokemonInField;

		for (int i = 0; i < 2; i++) {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBackground(new Color(230, 230, 250));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = gbc.gridy = 0;
			gbc.weighty = gbc.weightx = 1;

			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(0, 0, 20, 0);
			gbc.ipadx = 50;
			gbc.ipady = -50;

			if (i == 0) {
				pokemonInField = player1.getAttacker();
				panel.add(player1Name);
			} else {
				pokemonInField = player2.getAttacker();
				panel.add(player2Name);
			}

			gbc.gridy += 1;
			JProgressBar barToUpdate = new JProgressBar(0, pokemonInField.getMaxLifePoints());
			int currentLifePoints = pokemonInField.getLifePoints();

			barToUpdate.setString(currentLifePoints + "/" + pokemonInField.getMaxLifePoints());
			barToUpdate.setStringPainted(true);
			barToUpdate.setValue(currentLifePoints);
			//barToUpdate.setBackground(COLOR);
			barToUpdate.setVisible(true);
			barToUpdate.setPreferredSize(new Dimension(200, 100));

			panel.add(barToUpdate, gbc);

			gbc.gridy += 1;
			gbc.ipadx = gbc.ipady = 0;

			JLabel pokemon = new JLabel(new ImageLoader(pokemonInField.getImagePath(), 250, 200).getImageIcon());

			panel.add(pokemon, gbc);

			if (i == 0) {
				firstPlayerPanel = panel;
			} else {
				secondPlayerPanel = panel;
			}
		}
	}
	
	protected void attackAction(Capacity capacity) {
		Controller.getInstance().multiplayerFight(battle, capacity);
	}

	/**
	 * Modifies the attacks a user can choose depending on which pokemon is in the field
	 * @param pokemon

	 */
	private void updateAttacksPanel(Pokemon pokemon) {


		attacksPanel.setVisible(true);

		List<Capacity> masteredCapacities = Objects.requireNonNull(pokemon).getMasteredCapacities();

		for (Capacity capacity : masteredCapacities) {

			JButton button = new JButton(capacity.getName());

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					attackAction(capacity);
				}
			});
			
			attacksPanel.add(button);
		
		}
	}

	/**
	 * Method to change the pokemon in the field
	 * @param player

	 */
	private void switchPokemon(Trainer player) {
		String[] pokemons = new String[player.size()];
		int i = 0;
		for (Pokemon pokemon : player) {
			pokemons[i] = i + " -> " + pokemon.getName();
			i++;
		}

		String chosenPokemon = (String) JOptionPane.showInputDialog(null, "Choose a Pokemon!", "Switch Pokemon", JOptionPane.QUESTION_MESSAGE, null, pokemons, pokemons[0]);

		if (chosenPokemon != null) {
			String array[] = chosenPokemon.split(" ", 2);
			int index = Integer.valueOf(array[0]);

			Controller.getInstance().getUserTrainer().changeAttacker(player.get(index));

			ViewManager.getInstance().showMultiplayerBattle(battle);
		}
	}


}
