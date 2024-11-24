package MancalaUI.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    public ConfigPanel(MainFrame frame) {
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel opponentLabel = new JLabel("Opponent:");
        JComboBox<String> opponentDropdown = new JComboBox<>(new String[]{"Player 2 (Human)", "AI"});

        JLabel complexityLabel = new JLabel("Game Complexity:");
        JComboBox<String> complexityDropdown = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});

        JLabel strategyLabel = new JLabel("Game Strategy:");
        JComboBox<String> strategyDropdown = new JComboBox<>(new String[]{"Offensive", "Defensive"});

        JLabel heuristicLabel = new JLabel("Heuristic:");
        JComboBox<String> heuristicDropdown = new JComboBox<>(new String[]{"Basic", "Advanced"});

        JButton startButton = new JButton("Start the Game");
        startButton.addActionListener((ActionEvent e) -> {
            GamePanel gamePanel = frame.getGamePanel();
            gamePanel.configureGame(
                    opponentDropdown.getSelectedIndex(),
                    Integer.parseInt((String) complexityDropdown.getSelectedItem()),
                    (String) strategyDropdown.getSelectedItem(),
                    (String) heuristicDropdown.getSelectedItem()
            );

            frame.switchPanel("Game");
        });

        add(opponentLabel);
        add(opponentDropdown);
        add(complexityLabel);
        add(complexityDropdown);
        add(strategyLabel);
        add(strategyDropdown);
        add(heuristicLabel);
        add(heuristicDropdown);
        add(new JLabel()); // Placeholder
        add(startButton);
    }
}
