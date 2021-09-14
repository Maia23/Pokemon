package view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.pokedex.Pokedex;
import model.pokemon.PokemonModel;
import view.ImageLoader;
import view.ViewManager;

/**
 * Display Panel giving informatio about a given PokemonModel
 */
public class PokemonPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final PokemonModel pokemon;
	
	private final JButton backBtn = new JButton("Back");
	private final JLabel title = new JLabel();
	private final JLabel image = new JLabel();
	private final JPanel informationPanel = new JPanel();
	
	/**
	 * Initializes the entire panel using information about a given pokemon
	 * @param pokemonID
	 */
	public PokemonPanel(int pokemonID) {
		pokemon = Pokedex.getInstance().get(pokemonID);

		setBackground(new Color(230, 230, 250));
		setBorder(new EmptyBorder(50, 50, 150, 50));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weighty = gbc.weightx = 1;
		
		//Back btn
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(backBtn, gbc);
		
		//Title
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy = 1;
		title.setText("<html><h1>" + pokemon.getName().substring(0,1).toUpperCase() + pokemon.getName().substring(1).toLowerCase() + "</h1><html>");
		add(title, gbc);
		
		
		//Pokemon Image
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy = 2;
		
		
		image.setIcon(new ImageLoader("img", pokemon.getImagePath().substring(4), 250, 250).getImageIcon());
		add(image, gbc);
		
		
		//Pokemon Info
		gbc.gridx = 1;
		//gbc.fill = GridBagConstraints.BOTH;
		
		JLabel text = new JLabel("<html>Pokemon ID: <u><b>" + pokemon.getId() + "</b></u> <br><br>"
				+ "Type: <b>" + pokemon.getType() + "</b> <br><br>"
				+ "Size: <b>" + pokemon.getHeight() + "</b> <br><br>"
				+ "Weight: <b>" + pokemon.getWeight() + "</b> <br><br></html>");
			
		informationPanel.add(text);
		
		informationPanel.setBackground(new Color(230, 230, 250));
		add(informationPanel, gbc);
		
		
		
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showLastPage();
			}
		});
	}
	

}
