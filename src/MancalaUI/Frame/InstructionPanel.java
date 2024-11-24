package MancalaUI.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InstructionPanel extends JPanel {
    public InstructionPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("<html><center>Follow the rules of Mancala.<br>Select your opponent, strategy, and complexity.</center></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener((ActionEvent e) -> frame.switchPanel("Config"));

        add(instructionLabel, BorderLayout.CENTER);
        add(continueButton, BorderLayout.SOUTH);
    }
}
