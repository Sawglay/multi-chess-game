package logic;

import java.util.ArrayList;
import java.util.List;
/**
 * Castling, Pawn Promotion, Check, Checkmate,and Stalemate Detection
 * 
 * @author Wunna ,Garn & Zaid, Hassan 
 * @since 2025-07-25
 * @version UAT
 */
/*/**
 * The Board class represents the internal state and logic of the chess game.
 * It tracks the position of all pieces, whose turn it is, and calculates all
 * legal moves according to the rules of chess. It is completely separate from the GUI.
 */ 
public class Board {

/* Core State Variables ---
    // These variables define the fundamental state of the game: the 8x8 grid
    // holding the pieces, the current turn, flags for castling eligibility,
    // and a tracker for pending pawn promotions. */
    private final Piece[][] g = new Piece[8][8];
    private boolean whiteToMove = true;

    boolean whiteKingMoved = false, blackKingMoved = false;
    boolean whiteARookMoved = false, whiteHRookMoved = false;
    boolean blackARookMoved = false, blackHRookMoved = false;

    public Piece promotionPending = null;
    /*Basic Helper Methods ---
    // A group of simple utility methods to get/set pieces on the board,
    // check if coordinates are valid, and manage whose turn it is. */
    public Piece get(int r, int c) { return in(r,c) ? g[r][c] : null; }
    public void set(int r, int c, Piece p) { if (in(r,c)) g[r][c] = p; }
    public boolean in(int r, int c) { return r>=0 && r<8 && c>=0 && c<8; }
    public boolean isWhiteToMove() { return whiteToMove; }
    public void setWhiteToMove(boolean v) { whiteToMove = v; }
   /* /**
     * Resets the board to the standard starting position for a new game.
     * It clears the grid, places all 32 pieces in their initial spots,
     * and resets all game state flags (turn, castling, etc.).
     */ 

}