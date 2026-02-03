package logic;

import java.util.Objects;
/*/**
 * This class is a simple data structure that represents a single move in the chess game.
 * It doesn't contain any complex logic; it just holds the starting and ending
 * coordinates of a piece's movement.
 */ 

public class Move {
    // These public, final variables store the essential data for a move:
    // the "from" row/column and the "to" row/column. It also includes a
    // special boolean flag to identify if the move is a castling maneuver. */

    public final int fromR, fromC, toR, toC;
    public final boolean castle;
/*
     * The constructors are used to create a new Move object. One is a shortcut
     * for regular moves (where `castle` is false), and the other allows
     * specifying the castle flag directly.
     */ 

    public Move(int fr, int fc, int tr, int tc) {
        this(fr, fc, tr, tc, false);
    }
    public Move(int fr, int fc, int tr, int tc, boolean castle) {
        this.fromR=fr; this.fromC=fc; this.toR=tr; this.toC=tc; this.castle=castle;
    }
    @Override public boolean equals(Object o) { /*/**
 * These methods help compare and manage moves efficiently:
 * 
 * - `equals()` → Checks if two moves start and end on the same squares.
 * - `equalsWithFlags()` → Like equals(), but also considers whether the move is a castling move.
 * - `hashCode()` → Creates a unique number for the move, which helps store and find moves faster in collections like lists or maps.
 */
 
        if (!(o instanceof Move)) return false;
        Move m = (Move)o;
        return fromR==m.fromR && fromC==m.fromC && toR==m.toR && toC==m.toC;
    }
    public boolean equalsWithFlags(Move m) {
        return equals(m) && castle==m.castle;
    }
    @Override public int hashCode() { return Objects.hash(fromR,fromC,toR,toC); }
}
