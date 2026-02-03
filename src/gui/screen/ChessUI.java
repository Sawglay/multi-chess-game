package gui.screen;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;


/**
 * ChessUI class to run the application.
 * 
 * @author Sonali sonali.kdk@gmail.com, & Garn
 * @since 2025-07-22
 * @version UAT
 */
// public class ChessUI extends JFrame implements NetConnection.Listener {
public class ChessUI {

    /*GUI Components & Game State Variables ---
    * These lines declare all the visual elements (board, buttons, text areas)
    * and the variables needed to track the game's state, such as whose turn it is,
    * the selected piece, and network connection details. */
    // private final Board board = new Board();
    private final JButton[][] squares = new JButton[8][8];
    private final JTextArea history = new JTextArea(12, 24);
    private final JLabel status = new JLabel("Connecting...");
    private final JButton saveBtn = new JButton("Save Moves…");
    private final JPanel gridPanel = new JPanel(new GridLayout(8, 8));
    private final JScrollPane historyPane = new JScrollPane(history);

    private int selR = -1, selC = -1;
    private final boolean blackStarts = false;

    // Multiplayer
    // private final NetConnection net; // null means local
    // private final boolean iAmWhite;
    private String queuedPromotion = null;

    // Orientation: white at bottom in local; online: host white at bottom, client black at bottom
    // private boolean whiteAtBottom;
    }