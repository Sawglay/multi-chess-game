package properties;

import logic.*;

public class Pawn extends Piece {
    public Pawn(ColorSide color) { super(color); }

/*This logic is complex because pawns move
     * and capture differently.
     * 1. Standard Move: One square forward into an empty square.
     * 2. Initial Move: Two squares forward from its starting row, if the path is clear.
     * 3. Capture: One square diagonally forward, but only if an opponent's piece is on that square. */ 

    @Override public boolean canMove(Board b, Move m) {
        int dir = (color == ColorSide.WHITE) ? -1 : 1;
        int startRow = (color == ColorSide.WHITE) ? 6 : 1;
        int dr = m.toR - m.fromR;
        int dc = Math.abs(m.toC - m.fromC);
        Piece target = b.get(m.toR, m.toC);

        if (dc==0) { /*Case 1 & 2: Moving forward (no change in column). */
            if (target != null) return false;
            if (dr == dir) return true;
            if (m.fromR == startRow && dr == 2*dir) {
                if (b.get(m.fromR + dir, m.fromC) == null) return true;
            }
            return false;
        } /*Case 3: Diagonal capture. */
        if (dc==1 && dr==dir) {
            return target != null && target.color != this.color;
        }
        return false;
    }
/* /**
     * A special override for the Pawn. Unlike other pieces, a Pawn attacks
     * squares it cannot move to (unless an opponent is there). This method
     * correctly identifies the two diagonal squares in front of the pawn as
     * the ones it attacks.
     */
    @Override public boolean canAttack(Board b, Move m) {
        int dir = (color == ColorSide.WHITE) ? -1 : 1;
        int dr = m.toR - m.fromR;
        int dc = Math.abs(m.toC - m.fromC);
        return (dc==1 && dr==dir); /*A pawn only ever attacks one step diagonally forward. */
    }

    @Override public String unicode() { return color==ColorSide.WHITE ? "♙" : "♟"; } /*It provides the white symbol (♙) or the black symbol (♟) based on its color. */
}
