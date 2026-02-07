package logic;

import java.util.ArrayList;
import java.util.List;

import logging.LoggerOfThisApplication;
import properties.Bishop;
import properties.ColorSide;
import properties.King;
import properties.Knight;
import properties.Pawn;
import properties.Queen;
import properties.Rook;
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
public void reset() {
        for (int r=0;r<8;r++) for (int c=0;c<8;c++) g[r][c]=null;
        for (int c=0;c<8;c++) {
            set(6,c,new Pawn(ColorSide.WHITE));
            set(1,c,new Pawn(ColorSide.BLACK));
        }
        set(7,0,new Rook(ColorSide.WHITE)); set(7,7,new Rook(ColorSide.WHITE));
        set(0,0,new Rook(ColorSide.BLACK)); set(0,7,new Rook(ColorSide.BLACK));
        set(7,1,new Knight(ColorSide.WHITE)); set(7,6,new Knight(ColorSide.WHITE));
        set(0,1,new Knight(ColorSide.BLACK)); set(0,6,new Knight(ColorSide.BLACK));
        set(7,2,new Bishop(ColorSide.WHITE)); set(7,5,new Bishop(ColorSide.WHITE));
        set(0,2,new Bishop(ColorSide.BLACK)); set(0,5,new Bishop(ColorSide.BLACK));
        set(7,3,new Queen(ColorSide.WHITE)); set(0,3,new Queen(ColorSide.BLACK));
        set(7,4,new King(ColorSide.WHITE));  set(0,4,new King(ColorSide.BLACK));

        whiteToMove = true;
        whiteKingMoved = blackKingMoved = false;
        whiteARookMoved = whiteHRookMoved = false;
        blackARookMoved = blackHRookMoved = false;
        promotionPending = null;
    }

    // public boolean tryMakeMove(Move m) {
    //     Piece src = get(m.fromR, m.fromC);
    //     if (src == null) return false;
    //     if ((src.color == ColorSide.WHITE) != whiteToMove) return false;
    //     if (!in(m.toR, m.toC)) return false;

    //     List<Move> legal = legalMoves(whiteToMove ? ColorSide.WHITE : ColorSide.BLACK);
    //     boolean ok = legal.stream().anyMatch(x -> x.equals(m) || x.equalsWithFlags(m));
    //     if (!ok) return false;

    //     applyMove(m);
    //     whiteToMove = !whiteToMove;
    //     return true;
    // }

       /*
     * The main public method for attempting a move. It first checks if the move
     * is among the list of all possible legal moves. If it is, the move is
     * applied to the board, and the turn is switched to the other player.
     */ 
    public boolean tryMakeMove(Move m) {
    Piece src = get(m.fromR, m.fromC);
    if (src == null) return false;
    if ((src.color == ColorSide.WHITE) != whiteToMove) return false;
    if (!in(m.toR, m.toC)) return false;

    List<Move> legal = legalMoves(whiteToMove ? ColorSide.WHITE : ColorSide.BLACK);
    boolean ok = legal.stream().anyMatch(x -> x.equals(m) || x.equalsWithFlags(m));
    if (!ok) return false;

    // Apply the move first so algebraic(...) sees the "after" board.
    applyMove(m);

    // Build algebraic string (e.g., e2e4, O-O, O-O-O)
    String alg = algebraic(m, this);

    // Toggle turn
    whiteToMove = !whiteToMove;

    // Print to VS Code terminal
    LoggerOfThisApplication.move(alg, whiteToMove);

    return true;
}
   /*
    /**
     * An internal method that executes a move on the board without validating it.
     * It handles piece movement, captures, special castling logic, and updates
     * the state flags required for castling eligibility. It also identifies when
     * a pawn has reached the promotion rank.
     */ 
    void applyMove(Move m) {
        Piece src = get(m.fromR, m.fromC);

        if (src instanceof King && Math.abs(m.toC - m.fromC) == 2) {
            set(m.toR, m.toC, src);
            set(m.fromR, m.fromC, null);
            if (src.color == ColorSide.WHITE) whiteKingMoved = true; else blackKingMoved = true;
            if (m.toC == 6) {
                Piece rook = get(m.toR, 7);
                set(m.toR, 5, rook); set(m.toR, 7, null);
                if (src.color == ColorSide.WHITE) whiteHRookMoved = true; else blackHRookMoved = true;
            } else {
                Piece rook = get(m.toR, 0);
                set(m.toR, 3, rook); set(m.toR, 0, null);
                if (src.color == ColorSide.WHITE) whiteARookMoved = true; else blackARookMoved = true;
            }
            return;
        }

        set(m.toR, m.toC, src);
        set(m.fromR, m.fromC, null);

        if (src instanceof King) {
            if (src.color == ColorSide.WHITE) whiteKingMoved = true; else blackKingMoved = true;
        }
        if (src instanceof Rook) {
            if (src.color == ColorSide.WHITE) {
                if (m.fromR==7 && m.fromC==0) whiteARookMoved = true;
                if (m.fromR==7 && m.fromC==7) whiteHRookMoved = true;
            } else {
                if (m.fromR==0 && m.fromC==0) blackARookMoved = true;
                if (m.fromR==0 && m.fromC==7) blackHRookMoved = true;
            }
        }

        promotionPending = null;
        if (src instanceof Pawn) {
            if ((src.color == ColorSide.WHITE && m.toR == 0) || (src.color == ColorSide.BLACK && m.toR == 7)) {
                promotionPending = src;
            }
        }
    }

    // public void promotePawn(Piece pawn, String to) {
    //     for (int r=0;r<8;r++) for (int c=0;c<8;c++) {
    //         if (get(r,c) == pawn) {
    //             Piece repl;
    //             repl = switch (to) {
    //                 case "Rook" -> new Rook(pawn.color);
    //                 case "Bishop" -> new Bishop(pawn.color);
    //                 case "Knight" -> new Knight(pawn.color);
    //                 default -> new Queen(pawn.color);
    //             };
    //             set(r,c,repl); promotionPending = null; return;
    //         }
    //     }
    // }
    /* Replaces a pawn that has reached the end of the board with a new piece
     * (Queen, Rook, Bishop, or Knight) chosen by the player.
     */ 
    public void promotePawn(Piece pawn, String to) {
    for (int r=0;r<8;r++) for (int c=0;c<8;c++) {
        if (get(r,c) == pawn) {
            Piece repl = switch (to) {
                case "Rook" -> new Rook(pawn.color);
                case "Bishop" -> new Bishop(pawn.color);
                case "Knight" -> new Knight(pawn.color);
                case "Queen" -> new Queen(pawn.color);
                default -> new Pawn(pawn.color);
            };
            set(r,c,repl); 
    /* Check if a pawn has reached the last rank, setting it as "pending" for promotion. */
            promotionPending = null;

            // Terminal line like: [PROMO] e8 → Queen
            char file = (char)('a' + c);
            int rank = 8 - r;
            LoggerOfThisApplication.promotion("" + file + rank, to);

            return;
        }
    }
    }
    /*Game End-State Logic ---
    // This section contains methods to determine if a player is in check,
    // if the game has ended in checkmate, or if it has ended in a stalemate. */
    public boolean inCheck(ColorSide side) {
        int[] k = findKing(side);
        if (k == null) return false;
        return isSquareAttacked(k[0], k[1], side.opposite());
    }

    public boolean isCheckmate() {
        ColorSide side = whiteToMove ? ColorSide.WHITE : ColorSide.BLACK;
        return inCheck(side) && legalMoves(side).isEmpty();
    }

    public boolean isStalemate() {
        ColorSide side = whiteToMove ? ColorSide.WHITE : ColorSide.BLACK;
        return !inCheck(side) && legalMoves(side).isEmpty();
    }

    public static String algebraic(Move m, Board bAfter) {
        if (Math.abs(m.toC - m.fromC) == 2 && (bAfter.get(m.toR, m.toC) instanceof King)) {
            return (m.toC == 6 ? "O-O" : "O-O-O");
        }
        char fileFrom = (char)('a' + m.fromC);
        char fileTo   = (char)('a' + m.toC);
        int rankFrom  = 8 - m.fromR;
        int rankTo    = 8 - m.toR;
        return "" + fileFrom + rankFrom + fileTo + rankTo;
    }
/*/**
     * The core move generation engine. It iterates through every piece for a given
     * side, calculates all of its possible "pseudo-legal" moves, and then filters
     * out any move that would leave the king in check. It also adds any valid
     * castling moves.
     */ 
    public List<Move> legalMoves(ColorSide side) {
        List<Move> moves = new ArrayList<>();
        for (int r=0;r<8;r++) for (int c=0;c<8;c++) {
            Piece p = get(r,c);
            if (p == null || p.color != side) continue;
            for (int rr=0;rr<8;rr++) for (int cc=0;cc<8;cc++) {
                Move m = new Move(r,c,rr,cc);
                if (isPseudoLegal(p, m) && safeAfterMove(m, side)) moves.add(m);
            }
        }
        moves.addAll(castlingMoves(side));
        return moves;
    }
    /*
    /**
     * A specialized method to generate only castling moves. It checks all the
     * strict conditions for castling: neither king nor rook has moved, the squares
     * between them are empty, and the king does not start in, move through, or
     * end up in check.
     */ 
    private List<Move> castlingMoves(ColorSide side) {
        List<Move> out = new ArrayList<>();
        int row = (side == ColorSide.WHITE) ? 7 : 0;
        Piece king = get(row,4);
        if (!(king instanceof King) || inCheck(side)) return out;

        boolean km = (side==ColorSide.WHITE) ? whiteKingMoved : blackKingMoved;
        boolean aMoved = (side==ColorSide.WHITE) ? whiteARookMoved : blackARookMoved;
        boolean hMoved = (side==ColorSide.WHITE) ? whiteHRookMoved : blackHRookMoved;

        if (!km && !hMoved && get(row,5)==null && get(row,6)==null && !isSquareAttacked(row,5,side.opposite()) && !isSquareAttacked(row,6,side.opposite())) {
            out.add(new Move(row,4,row,6,true));
        }
        if (!km && !aMoved && get(row,1)==null && get(row,2)==null && get(row,3)==null
                && !isSquareAttacked(row,3,side.opposite()) && !isSquareAttacked(row,2,side.opposite())) {
            out.add(new Move(row,4,row,2,true));
        }
        return out;
    }

    /* /**
     * A critical helper method that simulates a move to see if it's "safe."
     * It temporarily makes the move, checks if the king is in check *after*
     * the move, and then immediately undoes the move to restore the board to
     * its original state.
     */
    private boolean safeAfterMove(Move m, ColorSide side) {
        Piece src = get(m.fromR, m.fromC);
        Piece dst = get(m.toR, m.toC);
        boolean wkm=whiteKingMoved, bkm=blackKingMoved, war=whiteARookMoved, whr=whiteHRookMoved, bar=blackARookMoved, bhr=blackHRookMoved;

        if (src instanceof King && Math.abs(m.toC - m.fromC) == 2) {
            set(m.toR, m.toC, src); set(m.fromR, m.fromC, null);
            if (m.toC==6) { Piece rook = get(m.toR,7); set(m.toR,5,rook); set(m.toR,7,null); }
            else { Piece rook = get(m.toR,0); set(m.toR,3,rook); set(m.toR,0,null); }
        } else {
            set(m.toR, m.toC, src); set(m.fromR, m.fromC, null);
        }

        boolean safe = !inCheck(side);

        set(m.fromR, m.fromC, src);
        set(m.toR, m.toC, dst);
        whiteKingMoved=wkm; blackKingMoved=bkm; whiteARookMoved=war; whiteHRookMoved=whr; blackARookMoved=bar; blackHRookMoved=bhr;
        return safe;
    }
    /*Low-Level Move Validation Helpers ---
    // These methods perform fundamental checks, like whether a move is valid
    // for a piece type (ignoring check), whether a square is attacked, or
    // finding the location of the king.
    
    // Checks if a move is valid for a piece's movement rules, ignoring check safety. */
    private boolean isPseudoLegal(Piece p, Move m) {
        if (!in(m.toR, m.toC)) return false;
        if (m.fromR==m.toR && m.fromC==m.toC) return false;
        Piece dst = get(m.toR, m.toC);
        if (dst != null && dst.color == p.color) return false;
        return p.canMove(this, m);
    }

    public boolean isSquareAttacked(int r, int c, ColorSide bySide) {
        for (int rr=0; rr<8; rr++) for (int cc=0; cc<8; cc++) {
            Piece attacker = get(rr, cc);
            if (attacker == null || attacker.color != bySide) continue;
            Move m = new Move(rr, cc, r, c);
            if (attacker.canAttack(this, m)) return true;
        }
        return false;
    }

/* A simple loop to find the coordinates of a side's king. */
    int[] findKing(ColorSide side) {
        for (int r=0;r<8;r++) for (int c=0;c<8;c++) {
            Piece p = get(r,c);
            if (p instanceof King && p.color==side) return new int[]{r,c};
        }
        return null;
    }

    public Piece[][] getG() {
        return g;
    }
}
