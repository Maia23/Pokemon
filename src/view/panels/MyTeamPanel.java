package view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Model;
import model.pokemon.PokemonModel;
import model.trainer.UserTrainer;
import view.GameWindow;
import view.ImageLoader;
import view.ViewManager;

/**
 * Standard display for MyTeam Page
 */
public class MyTeamPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private final ArrayList<JLabel> pokemonLabels = new ArrayList<JLabel>();
	private int selectedIndex = -1;


	private final JButton backBtn = new JButton("Back");
	private final JLabel title = new JLabel();
	private final JPanel pokemonPanel = new JPanel(new GridBagLayout());
	private final JButton pokemonAttacks = new JButton("Manage Attacks");
	private final JButton pokemonName = new JButton("Change Pokemon Name");
	private final JButton removePokemon = new JButton("Remove Pokemon");


	public MyTeamPanel() {
		
		UserTrainer userTrainer = Controller.getInstance().getUserTrainer();

		setBackground(new Color(230, 230, 250));
		setBorder(new EmptyBorder(50, 50, 150, 50));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weighty = gbc.weightx = 1;

		//BackBtn
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(backBtn, gbc);

		//Title
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridy = 1;
		title.setText("<html><h1>My Pokemons</h1><html>");
		add(title, gbc);


		//Pokemon Panel
		gbc.gridwidth = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < Model.maxPokemonsForUser; i++) {
			gbc.gridx += 1;
			JLabel label = new JLabel();
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.BOTTOM);

			if (i < userTrainer.size() && userTrainer.get(i) != null) {
				PokemonModel pokemon = userTrainer.get(i);
				label.setIcon(new ImageLoader("img", pokemon.getImagePath().substring(4), 100, 100).getImageIcon());
				label.setText(pokemon.getId() + " : " + pokemon.getName());
				addPokemonMouseListener(label, pokemon);

			} else {
				setEmptySlot(label);
				addEmptySlotListener(label);
			}

			pokemonLabels.add(label);
			pokemonPanel.add(label, gbc);
		}

		add(pokemonPanel, gbc);


		//Manage attacks btn
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		pokemonAttacks.setEnabled(false);
		add(pokemonAttacks, gbc);

		//Pokemon Name btn
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		pokemonName.setEnabled(false);
		add(pokemonName, gbc);

		//Remove Pokemon btn
		gbc.gridy = 5;
		removePokemon.setEnabled(false);
		add(removePokemon, gbc);


		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showLastPage();
			}
		});

		pokemonAttacks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showPokemonAttacksPage(userTrainer.get(selectedIndex));
			}
		});

		pokemonName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nickname = JOptionPane.showInputDialog(GameWindow.getInstance(), "Choose a nickname for your pokemon", userTrainer.get(selectedIndex).getName());
				if (nickname == null)
					return ;
				Controller.getInstance().editPokemonName(selectedIndex, nickname);
				ViewManager.getInstance().refreshMyTeamPage();
			}
		});

		removePokemon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < pokemonLabels.size(); i++) {
					if (pokemonLabels.get(i).getBorder() != null) {
						setEmptySlot(pokemonLabels.get(i));
						Controller.getInstance().pokemonRemove(i);
						ViewManager.getInstance().refreshMyTeamPage();
					}
				}
			}
		});
	}

	/**
	 * Adds a sign plus image to a given Label
	 * @param label

	 */
	private void setEmptySlot(JLabel label) {
		Objects.requireNonNull(label).setText(null);
		label.setIcon(new ImageLoader("other", "plus.png", 100, 100).getImageIcon());
	}


	/**
	 * Adds a mouse listener to a given Label containing a PokemonModel
	 * @param label
	 * @param pokemon

	 */
	private void addPokemonMouseListener(JLabel label, PokemonModel pokemon) {
			Objects.requireNonNull(pokemon);
			Objects.requireNonNull(label).addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (label.getBorder() == null) {
					for (JLabel pokemonLabel : pokemonLabels) {
						pokemonLabel.setBorder(null);
					}
					label.setBorder(BorderFactory.createLineBorder(Color.black));
					pokemonAttacks.setEnabled(true);
					pokemonName.setEnabled(true);
					removePokemon.setEnabled(true);
					
					for (int i = 0; i < pokemonLabels.size(); i++) {
						if (pokemonLabels.get(i).equals(label)) {
							selectedIndex = i;
						}
					}
					
				} else {
					ViewManager.getInstance().showPokemonPage(Integer.parseInt(pokemon.getId()));
				}
			}
		});
	}

	/**
	 * Adds a default listener to every cell without loaded Pokemon
	 * @param label

	 */
	private void addEmptySlotListener(JLabel label) {

		Objects.requireNonNull(label).addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (label.getBorder() == null) {
					for (JLabel pokemonLabel : pokemonLabels) {
						pokemonLabel.setBorder(null);
					}

					label.setBorder(BorderFactory.createLineBorder(Color.black));
					pokemonAttacks.setEnabled(false);
					pokemonName.setEnabled(false);
					removePokemon.setEnabled(false);
				} else {
					ViewManager.getInstance().showPokedex();
				}
			}
		});
	}


}
