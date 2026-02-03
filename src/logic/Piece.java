package logic;

import properties.ColorSide;

public abstract class Piece {
    /*These are the core properties of every piece. `color` is final because a
    // piece never changes its color. `moved` is a flag to track if a piece
    // has made its first move, which is important for rules like castling. */
    public final ColorSide color;
    public boolean moved = false;

    public Piece(ColorSide color) { this.color = color; }
    /* /**
     * These are the "abstract" methods that every specific piece (Pawn, Rook, etc.)
     * MUST implement. This design forces each piece to define its own unique
     * movement rules */

    public abstract boolean canMove(Board b, Move m);
    public boolean canAttack(Board b, Move m) { return canMove(b, m); }
    public abstract String unicode();
    /* /**
     * This is a reusable helper method for sliding pieces (Rook, Bishop, Queen).
     * It checks if the path between a starting square and an ending square is
     * completely empty, which is required for their movement */

    protected boolean pathClear(Board b, int r1, int c1, int r2, int c2) {
        int dr = Integer.compare(r2, r1);
        int dc = Integer.compare(c2, c1);
        int r = r1 + dr, c = c1 + dc;
        while (r != r2 || c != c2) {
            if (b.get(r, c) != null) return false;
            r += dr; c += dc;
        }
        return true; /*The path is clear  */
    }
}

