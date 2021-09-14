package view.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.league.League;
import model.trainer.Trainer;
import view.ViewManager;
import view.util.JUtilities;

public class LeaguePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JPanel topSide;
	private final JButton backBtn;
	private final JLabel title = new JLabel("<html><h1>LEAGUE</h1></html>");

	private final JScrollPane scrollPane;
	private final JTable table;

	private final JButton startBattleBtn = new JButton("Start Combat");

	private String[][] data;

	public LeaguePanel(League league) {
		loadData(league);

		setBorder(new EmptyBorder(50, 50, 150, 50));
		setLayout(new BorderLayout());

		topSide = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		
		//Back btn
		backBtn = new JButton("Back");
		topSide.add(backBtn, gbc);

		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		topSide.add(title, gbc);
		
		add(topSide, BorderLayout.PAGE_START);

		//Table
		table = new JTable(data, league.getHeadings());
		table.setVisible(true);
		table.setDefaultEditor(Object.class, null);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		JUtilities.setCellsAlignment(table, SwingConstants.CENTER);
		
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		
		//Start Battle button
		add(startBattleBtn, BorderLayout.PAGE_END);
		
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.getInstance().showMenu();
			}
		});
		
		startBattleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean	fightStarts = Controller.getInstance().nextBattle(league);
				if (!fightStarts) {
					ViewManager.getInstance().showDialogMessage("Cannot start a Battle, please add Pokemons to your team and dont forget to set their attacks!");
				}
			}
		});
		
	}

	private void loadData(League league) {
		
		data = new String[league.size()][];
		int i = 0;
		
		for (Trainer trainer : league) {
			String[] tmp = new String[3];

			tmp[0] = String.valueOf(i);
			tmp[1] = trainer.getName();
			tmp[2] = String.valueOf(trainer.getNbPokemons());

			data[i++] = tmp;
		}
	}

}
