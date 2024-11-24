package MancalaUI.Logic;

import java.util.Vector;

public class MancalaSearch extends GameSearch {
    private MancalaPosition position;

    public MancalaSearch(MancalaPosition position) {
        this.position = position;
    }

    @Override
    public boolean drawnPosition(Position p) {
        MancalaPosition pos = (MancalaPosition) p;
        return pos.isGameOver() && pos.getScore(0) == pos.getScore(1); // Égalité
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        int playerScore = pos.getScore(player ? 0 : 1);
        int opponentScore = pos.getScore(player ? 1 : 0);
        return pos.isGameOver() && playerScore > opponentScore;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        return pos.evaluatePosition(); // Utilisation de la méthode dans MancalaPosition
    }

    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        Vector<Position> moves = new Vector<>();

        // Générer les mouvements possibles
        for (int i = 0; i < 6; i++) {
            int pit = player ? i : i + 7;
            if (pos.getBoard()[pit] > 0) {
                MancalaPosition cloned = (MancalaPosition) pos.clone();
                cloned.makeMove(new MancalaMove(pit));
                moves.add(cloned);
            }
        }

        return moves.toArray(new Position[0]);
    }

    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        MancalaPosition pos = (MancalaPosition) p;
        MancalaPosition newPos = (MancalaPosition) pos.clone();
        newPos.makeMove(move);
        return newPos;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        return depth >= getComplexity();
    }

    /**
     * Méthode publique pour accéder à alphaBeta
     */
    public Vector<Object> performAlphaBeta(Position p, boolean player) {
        return alphaBeta(0, p, player); // Appelle la méthode protégée
    }
}
