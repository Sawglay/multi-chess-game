package properties;

import logic.*;

public class King extends Piece {
    public King(ColorSide color) { super(color); }
    @Override public boolean canMove(Board b, Move m) {
        /*This method checks for two types of valid moves:
     * 1. A standard move to any adjacent square (one step in any direction).
     * 2. A special castling move, where the king moves two squares horizontally. */
        int dr = Math.abs(m.toR - m.fromR);
        int dc = Math.abs(m.toC - m.fromC);
        if (dr<=1 && dc<=1) return true; /*Standard king move: one square in any direction. */
        if (dr==0 && dc==2) { /* Preliminary check for castling (moving 2 squares horizontally) */
            if (color == ColorSide.WHITE && m.fromR==7 && m.fromC==4) {
                if (m.toC==6) return b.get(7,5)==null && b.get(7,6)==null;
                if (m.toC==2) return b.get(7,1)==null && b.get(7,2)==null && b.get(7,3)==null;
            } else if (color == ColorSide.BLACK && m.fromR==0 && m.fromC==4) {
                if (m.toC==6) return b.get(0,5)==null && b.get(0,6)==null;
                if (m.toC==2) return b.get(0,1)==null && b.get(0,2)==null && b.get(0,3)==null;
            }
        }
        return false; /* If neither a standard move nor a castling move, it's illegal. */
    }
    @Override public String unicode() { return color==ColorSide.WHITE ? "♔" : "♚"; } /*It provides the white symbol (♔) or the black symbol (♚) based on its color. */
}
