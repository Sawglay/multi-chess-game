package properties;

import logic.*;

public class Queen extends Piece {
    /*A queen can move if:
     * 1. The move is perfectly diagonal (like a Bishop).
     * 2. The move is perfectly horizontal or vertical (like a Rook).
     * 3. The path along that line (diagonal, horizontal, or vertical) is empty.
     */

    public Queen(ColorSide color) { super(color); }
    @Override public boolean canMove(Board b, Move m) {
        int dr = Math.abs(m.toR - m.fromR); /* If the move is diagonal (dr==dc) OR straight (dr==0 or dc==0),
        // then check if the path is clear. */
        int dc = Math.abs(m.toC - m.fromC);
        if (dr==dc || dr==0 || dc==0) return pathClear(b, m.fromR, m.fromC, m.toR, m.toC);
        return false;/*Otherwise the move is illegal */
    }
    @Override public String unicode() { return color==ColorSide.WHITE ? "♕" : "♛"; } /*
     * Returns the Unicode character for the Queen piece.
     * It provides the white symbol (♕) or the black symbol (♛) based on its color.
     */ 
}