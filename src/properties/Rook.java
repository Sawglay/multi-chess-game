package properties;

import logic.*;

public class Rook extends Piece {
    /* A rook can only move if:
     * 1. The move is perfectly horizontal (no change in row) or vertical (no change in column).
     * 2. All squares along that straight-line path are empty.
      */
    public Rook(ColorSide color) { super(color); }
    @Override public boolean canMove(Board b, Move m) {
        int dr = Math.abs(m.toR - m.fromR);
        int dc = Math.abs(m.toC - m.fromC);
        if (dr==0 || dc==0) return pathClear(b, m.fromR, m.fromC, m.toR, m.toC);/*If the move is straight, check if the path is clear. */
        return false; /*Otherwise the move is illegal */
    }
    @Override public String unicode() { return color==ColorSide.WHITE ? "♖" : "♜"; }/*It provides the white symbol (♖) or the black symbol (♜) based on its color.
      */
}
