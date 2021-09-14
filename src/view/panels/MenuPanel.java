package view.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import view.GameWindow;
import view.ImageLoader;
import view.ViewManager;

/**
 * Display Panel for the Menu Page

 *
 */
public class MenuPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	
	private final JLabel menuTitle = new JLabel();
	private final JButton pokedexBtn = new JButton("POKEDEX");
	private final JButton myTeamBtn = new JButton("My Team");
	private final JButton battleBtn = new JButton("Battle");
	private final JButton multiplayerBtn = new JButton("Multiplayer");
	private final JButton leagueBtn = new JButton("League");
	private final JButton homeMadeLeagueBtn = new JButton("Home made league");
	private final JButton healBtn = new JButton("Heal the team");
	private final JButton saveBtn = new JButton("Save Game");
	private final JButton loadBtn = new JButton("Load Saved Game");
	private final JButton exitBtn = new JButton("Exit");
	
	/**
	 * Initializes all needed variables for the Menu Panel, it adds listeners to all buttons
	 */
	public MenuPanel() {
        setBorder(new EmptyBorder(100, 100, 100, 100));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        
        menuTitle.setIcon(new ImageLoader("other", "logoPokemon.png", 350, 150).getImageIcon());
        add(menuTitle, gbc);
       
        gbc.insets = new Insets(50, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.add(pokedexBtn, gbc);
        gbc.insets = new Insets(10, 0, 0, 0);
        buttons.add(myTeamBtn, gbc);
        buttons.add(healBtn, gbc);
        buttons.add(battleBtn, gbc);
        buttons.add(multiplayerBtn, gbc);
        buttons.add(homeMadeLeagueBtn, gbc);
        buttons.add(leagueBtn, gbc);
        buttons.add(saveBtn, gbc);
        buttons.add(loadBtn, gbc);
        buttons.add(exitBtn, gbc);
        gbc.weighty = 5;
        add(buttons, gbc);
        
        
        pokedexBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showPokedex();
			}
		});
        
        myTeamBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showMyTeam();
			}
		});
        
        healBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Controller.getInstance().heal()) {
					ViewManager.getInstance().showDialogMessage("Cannot heal an empty team");
				}
			}
		});
        
        battleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Controller.getInstance().classicalFight()) {
					ViewManager.getInstance().showDialogMessage("Cannot start a battle, please add Pokemons to your team and dont forget to set their attacks!");
				}
			}
		});
        
        multiplayerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Controller.getInstance().multiplayerFight()) {
					ViewManager.getInstance().showDialogMessage("Cannot start a multiplayer battle, please add Pokemons to your team and dont forget to set their attacks!");
				}
			}
		});
        
        leagueBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Controller.getInstance().classicalLeague()) {
					ViewManager.getInstance().showDialogMessage("Cannot start a league, please add Pokemons to your team and dont forget to set their attacks!");
				}
			}
		});
        
        homeMadeLeagueBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Controller.getInstance().homeMadeLeague()) {
					ViewManager.getInstance().showDialogMessage("Cannot start a home made league, please add Pokemons to your team and dont forget to set their attacks!");
				}
			}
		});
        
        saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(GameWindow.getInstance(), "You will lose your saved game, are you sure?") == 0)
					Controller.getInstance().save();
			}
		});
        
        loadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(GameWindow.getInstance(), "You will lose your current game, are you sure?") == 0)
					Controller.getInstance().load();
			}
		});
        
        exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
    }
	
	
	

}
