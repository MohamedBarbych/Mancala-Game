package MancalaUI.Frame;

import MancalaUI.Logic.*;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GamePanel extends JPanel {
    private MancalaPosition model; // Modèle du jeu
    private MancalaSearch search; // Algorithme de recherche
    private JPanel[][] pitPanels; // Cases des joueurs
    private JPanel[] mancalaPanels; // Mancalas des joueurs
    private JLabel playerTurnLabel; // Indicateur de tour du joueur
    private boolean isAgainstAI; // Détermine si l'adversaire est l'IA

    public GamePanel(MainFrame frame) {
        setLayout(new BorderLayout());
        model = new MancalaPosition(); // Initialisation du modèle
        search = new MancalaSearch(model); // Initialisation de la recherche

        mancalaPanels = new JPanel[2]; // Deux mancalas

        // Indicateur du tour
        playerTurnLabel = new JLabel("Player A's Turn", SwingConstants.CENTER);
        playerTurnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(playerTurnLabel, BorderLayout.NORTH);

        // Plateau de jeu
        JPanel boardPanel = createBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Boutons d'action
        JPanel bottomPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveGame());

        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> requestHelp());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadGame());

        bottomPanel.add(saveButton);
        bottomPanel.add(helpButton);
        bottomPanel.add(loadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBoard(); // Mise à jour initiale du plateau
    }

    public void configureGame(int opponentType, int complexity, String strategy, String heuristic) {
        model.setComplexity(complexity);
        model.setStrategy(strategy);
        model.setHeuristic(heuristic);
        search.setComplexity(complexity);
        search.setStrategy(strategy);
        isAgainstAI = (opponentType == 1); // 1 = Joueur contre IA
    }

    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout(10, 10));
        boardPanel.setBackground(new Color(139, 69, 19)); // Marron clair

        // Mancala gauche (B)
        mancalaPanels[0] = createMancalaPanel("B");
        boardPanel.add(mancalaPanels[0], BorderLayout.WEST);

        // Mancala droite (A)
        mancalaPanels[1] = createMancalaPanel("A");
        boardPanel.add(mancalaPanels[1], BorderLayout.EAST);

        // Plateau central
        JPanel pitsPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        pitsPanel.setBackground(new Color(139, 69, 19));
        pitPanels = new JPanel[2][6];

        // Ligne supérieure (B6 → B1)
        for (int i = 5; i >= 0; i--) {
            pitPanels[1][i] = createPitPanel("B" + (6 - i), i + 7);
            pitsPanel.add(pitPanels[1][i]);
        }

        // Ligne inférieure (A1 → A6)
        for (int i = 0; i < 6; i++) {
            pitPanels[0][i] = createPitPanel("A" + (i + 1), i);
            pitsPanel.add(pitPanels[0][i]);
        }

        boardPanel.add(pitsPanel, BorderLayout.CENTER);
        return boardPanel;
    }

    private JPanel createMancalaPanel(String playerLabel) {
        JPanel mancalaPanel = new JPanel();
        mancalaPanel.setLayout(new BorderLayout());
        mancalaPanel.setPreferredSize(new Dimension(100, 150));
        mancalaPanel.setBackground(new Color(139, 69, 19));

        JLabel label = new JLabel(playerLabel, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);

        JLabel mancalaGrains = new JLabel();
        mancalaGrains.setFont(new Font("Arial", Font.PLAIN, 20));
        mancalaGrains.setForeground(Color.BLACK);

        mancalaPanel.add(label, BorderLayout.NORTH);
        mancalaPanel.add(mancalaGrains, BorderLayout.CENTER);
        return mancalaPanel;
    }

    private JPanel createPitPanel(String label, int pitIndex) {
        JPanel pitPanel = new JPanel();
        pitPanel.setLayout(new BorderLayout());
        pitPanel.setBackground(new Color(160, 82, 45)); // Marron foncé

        JLabel pitLabel = new JLabel(label, SwingConstants.CENTER);
        pitLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pitLabel.setForeground(Color.WHITE);

        JPanel grainsPanel = new JPanel();
        grainsPanel.setLayout(new GridLayout(2, 3));
        grainsPanel.setOpaque(false);
        updateGrains(grainsPanel, pitIndex);

        pitPanel.add(pitLabel, BorderLayout.NORTH);
        pitPanel.add(grainsPanel, BorderLayout.CENTER);

        pitPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleMove(pitIndex);
            }
        });

        return pitPanel;
    }

    private void updateGrains(JPanel grainsPanel, int pitIndex) {
        grainsPanel.removeAll();
        int grains = model.getBoard()[pitIndex];
        for (int i = 0; i < grains; i++) {
            JLabel grain = new JLabel("●", SwingConstants.CENTER);
            grain.setForeground(Color.YELLOW);
            grainsPanel.add(grain);
        }
        grainsPanel.revalidate();
        grainsPanel.repaint();
    }

    private void refreshBoard() {
        int[] board = model.getBoard();

        // Cases
        for (int i = 0; i < 6; i++) {
            updateGrains((JPanel) pitPanels[0][i].getComponent(1), i); // Ligne inférieure (A)
            updateGrains((JPanel) pitPanels[1][5 - i].getComponent(1), i + 7); // Ligne supérieure (B)
        }

        // Mancalas
        ((JLabel) mancalaPanels[0].getComponent(1)).setText(String.valueOf(board[13])); // Mancala B
        ((JLabel) mancalaPanels[1].getComponent(1)).setText(String.valueOf(board[6])); // Mancala A
    }

    private void handleMove(int pitIndex) {
        if (!model.isValidMove(new MancalaMove(pitIndex))) {
            JOptionPane.showMessageDialog(this, "Invalid move! Try again.");
            return;
        }

        model.makeMove(new MancalaMove(pitIndex));
        refreshBoard();

        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! Winner: Player " +
                    (model.getScore(0) > model.getScore(1) ? "A" : "B"));
        } else if (isAgainstAI && model.getCurrentPlayer() == 1) {
            performAIMove();
        } else {
            playerTurnLabel.setText("Player " + (model.getCurrentPlayer() == 0 ? "A" : "B") + "'s Turn");
        }
    }

    private void performAIMove() {
        JOptionPane.showMessageDialog(this, "AI is making a move...");
        Vector<Object> result = search.performAlphaBeta(model, GameSearch.PROGRAM);
        Position bestMove = (Position) result.elementAt(1);
        model = (MancalaPosition) bestMove;
        refreshBoard();

        if (model.isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game Over! Winner: Player " +
                    (model.getScore(0) > model.getScore(1) ? "A" : "B"));
        } else {
            playerTurnLabel.setText("Player " + (model.getCurrentPlayer() == 0 ? "A" : "B") + "'s Turn");
        }
    }

    private void saveGame() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename to save:");
        if (filename != null && !filename.trim().isEmpty()) {
            MancalaPosition.saveGame(model, filename.trim());
            JOptionPane.showMessageDialog(this, "Game saved successfully!");
        }
    }
    private void requestHelp() {
        if (model.getHelpRequestsLeft() > 0) {
            MancalaMove bestMove = model.aiMove();
            JOptionPane.showMessageDialog(this, "Suggested Move: Pit " + bestMove.getPit());
            model.decrementHelpRequestsLeft(); // Décrémenter les aides restantes
        } else {
            JOptionPane.showMessageDialog(this, "No help requests left!");
        }
    }
    private void loadGame() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename to load:");
        if (filename != null && !filename.trim().isEmpty()) {
            MancalaPosition loadedModel = MancalaPosition.loadGame(filename.trim());
            if (loadedModel != null) {
                model = loadedModel;
                refreshBoard();
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load game.");
            }
        }
    }
}
