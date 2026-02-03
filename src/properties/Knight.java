package properties;

import logic.*;

public class Knight extends Piece {
    /*A knight moves in an "L" shape:
     * two squares in one cardinal direction (horizontal or vertical), then one
     * square in a perpendicular direction. */

    public Knight(ColorSide color) { super(color); }
    @Override public boolean canMove(Board b, Move m) {
        int dr = Math.abs(m.toR - m.fromR);
        int dc = Math.abs(m.toC - m.fromC);
        return dr*dc==2;
    } /*  /**
     * Returns the Unicode character for the Knight piece.
     * It provides the white symbol (♘) or the black symbol (♞) based on its color.
     */ 
    
    @Override public String unicode() { return color==ColorSide.WHITE ? "♘" : "♞"; }
}