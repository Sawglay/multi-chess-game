package logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Logger to use in this application.
 * @author Zaid & Vuthy
 * @since 2025-08-18
 * @version UAT
 */
public class LoggerOfThisApplication {
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

/**
 * Print the message to the relative logging level.
 * 
 * @param level logging level
 * @param message message to print
 */
    private static String ts() { return "[" + LocalDateTime.now().format(TS) + "] "; }

    /** One line per move: e2e4  next: Black */
    public static void move(String algebraic, boolean whiteToMoveAfter) {
        System.out.println(ts() + "[MOVE] " + algebraic + "  next: " + (whiteToMoveAfter ? "White" : "Black"));
        System.out.flush();
    }

    /** Shown right after a pawn promotes. Example: e8 → Queen */
    public static void promotion(String square, String toPiece) {
        System.out.println(ts() + "[PROMO] " + square + " \u2192 " + toPiece);
        System.out.flush();
    }
}

