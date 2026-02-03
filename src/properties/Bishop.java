package properties;
import logic.*;

public class Bishop extends Piece {
    public Bishop(ColorSide color) { super(color); }
    /*/**
     * Defines the Bishop's movement rules. A bishop can only move if:
     * 1. The move is perfectly diagonal (the number of rows moved equals the number of columns moved).
     * 2. All squares along that diagonal path are empty.
      */
    @Override public boolean canMove(Board b, Move m) {
        int dr = Math.abs(m.toR - m.fromR);
        int dc = Math.abs(m.toC - m.fromC);
        if (dr==dc) return pathClear(b, m.fromR, m.fromC, m.toR, m.toC);
        return false;
    }
    @Override public String unicode() { return color==ColorSide.WHITE ? "♗" : "♝"; }
}
