package gui.screen;

import network.*;
import logic.*;
import properties.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class ChessUI extends JFrame implements NetConnection.Listener {

    /*GUI Components & Game State Variables ---
    * These lines declare all the visual elements (board, buttons, text areas)
    * and the variables needed to track the game's state, such as whose turn it is,
    * the selected piece, and network connection details. */
    private final Board board = new Board();
    private final JButton[][] squares = new JButton[8][8];
    private final JTextArea history = new JTextArea(12, 24);
    private final JLabel status = new JLabel("Connecting...");
    private final JButton saveBtn = new JButton("Save Moves…");
    private final JPanel gridPanel = new JPanel(new GridLayout(8, 8));
    private final JScrollPane historyPane = new JScrollPane(history);

    private int selR = -1, selC = -1;
    private final boolean blackStarts = false;

    // Multiplayer
    private final NetConnection net; // null means local
    private final boolean iAmWhite;
    private String queuedPromotion = null;

    // Orientation: white at bottom in local; online: host white at bottom, client black at bottom
    private boolean whiteAtBottom;

    public ChessUI(NetConnection net, boolean iAmWhite) {

        super("SimpleChess — " + (net == null ? "Local" : (iAmWhite ? "Host(White)" : "Client(Black)")));
        this.net = net;
        this.iAmWhite = iAmWhite;
        if (this.net != null) {
            SwingUtilities.invokeLater(() -> this.net.setListener(this));
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1065, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem(new AbstractAction("New Game (Local)") {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (net != null) {
                    net.close();
                }
                restartLocal();
            }
        });
        // JCheckBoxMenuItem blackFirst = new JCheckBoxMenuItem(new AbstractAction("Black starts first (Local)") {
        //     @Override
        //     public void actionPerformed(java.awt.event.ActionEvent e) {
        //         ChessUI.this.blackStarts = ((JCheckBoxMenuItem) e.getSource()).isSelected();
        //     }
        // });
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (net != null) {
                    net.close();
                }
                System.exit(0);
            }
        });
        game.add(newGame);
        // game.add(blackFirst);
        game.add(exit);
        menuBar.add(game);
        setJMenuBar(menuBar);

        // Left: board
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(gridPanel, BorderLayout.CENTER);

        // Right: sidebar
        history.setEditable(false);
        // JPanel side = new JPanel(new BorderLayout(98,98));
        JPanel side = new JPanel();
        // side.setPreferredSize(new Dimension(150, 50));
        side.setLayout(new BorderLayout(8, 8));
        historyPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        side.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        side.add(status, BorderLayout.NORTH);
        side.add(historyPane, BorderLayout.CENTER);
        /*Create a panel for the "Restart" and "Quit" buttons and define what they
         do when clicked. "Restart" only works for local games. */
        JPanel buttons = new JPanel(new GridLayout(3, 0, 8, 8));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 45, 20, 45));
        JButton restart = new JButton("Restart");
        restart.addActionListener(_ -> {
            if (net == null) {
                restartLocal();
            } else {
                JOptionPane.showMessageDialog(this, "Restart only for Local mode.\nClose and host again to reset.");
            }
        });
        JButton quit = new JButton("Quit");
        quit.addActionListener(_ -> {
            if (net != null) {
                net.close();
            }
            dispose();
            new ModePicker().setVisible(true);
        });
        buttons.add(restart);
        buttons.add(quit);

        // Save button to export move history        
        /*// Set up the "Save Moves" button. When clicked, it opens a file dialog,
        // allowing the user to save the move history from the text area to a file. */
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(_ -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("chess_moves.txt"));
            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                try (FileWriter w = new FileWriter(fc.getSelectedFile())) {
                    w.write(history.getText());
                    JOptionPane.showMessageDialog(this, "Saved: " + fc.getSelectedFile().getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage());
                }
            }
        });
        buttons.add(saveBtn);
        /*Final assembly of the sidebar: add the button panel to the bottom of the
        // sidebar, and then add the completed sidebar to the right side of the main window. */
        side.add(buttons, BorderLayout.SOUTH);
        
        add(side, BorderLayout.EAST);
        /*This double loop creates the 64 clickable JButtons that represent the
        // chessboard squares. Each button is given a color and an action listener
        // that calls the `onSquare` method when clicked. */

        // Build board buttons
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                JButton b = new JButton();
                b.setFocusPainted(false);
                b.setBackground(baseColor(r, c));
                b.setOpaque(true);
                b.setHorizontalAlignment(SwingConstants.CENTER);
                b.setVerticalAlignment(SwingConstants.CENTER);
                final int rr = r, cc = c;
                b.addActionListener(_ -> onSquare(rr, cc));
                squares[r][c] = b;
                gridPanel.add(b);
            }
        }

        // Responsive scaling hook
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoard(); // re-scale text
            }
        });

        newGame();
    }
    /* A helper method to easily restart a local game. It closes the current
    // window and opens a fresh one. */
    private void restartLocal() {
        ChessUI ui = new ChessUI(null, true);
        ui.setVisible(true);
        dispose();
    }  
    /*This method resets the entire game state to start a new match. It clears the
    // board logic, move history, and selections. It also sets up the correct turn order
    // and status text for either a local or an online game. */
    private void newGame() {
        board.reset();
        history.setText("");
        selR = selC = -1;
        queuedPromotion = null;
        if (net == null) {
            whiteAtBottom = true;
            board.setWhiteToMove(!blackStarts);
            status.setText("Mode: Local — Turn: " + (board.isWhiteToMove() ? "White" : "Black"));
        } else {
            whiteAtBottom = iAmWhite;
            board.setWhiteToMove(true); // host/white starts
            status.setText("Mode: " + (iAmWhite ? "Host/White" : "Client/Black") + " — " + (net.isAlive() ? "Connected ✓" : "Connecting…"));
        }
        updateBoard();
        updateStatus();
    }

    private Color baseColor(int r, int c) {
        return ((r + c) % 2 == 0) ? new Color(240, 217, 181) : new Color(181, 136, 99);
    }

    private Color moveHintEmpty() {
        return new Color(170, 220, 170);
    }

    private Color moveHintCapture() {
        return new Color(220, 170, 170);
    }
    /* // Resets the visual appearance of all squares, removing any selection
    // or move hint highlights. */
    private void clearHighlights() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c].setBorder(UIManager.getBorder("Button.border"));
                squares[r][c].setBackground(baseColor(r, c));
            }
        }
    }

    private void highlightSelectionAndMoves(int mr, int mc) {
        clearHighlights();
        int[] v = modelToView(mr, mc);
        squares[v[0]][v[1]].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        List<Move> legalFrom = board.legalMoves(board.isWhiteToMove() ? ColorSide.WHITE : ColorSide.BLACK)
                .stream().filter(m -> m.fromR == mr && m.fromC == mc).collect(Collectors.toList());
        for (Move m : legalFrom) {
            int[] vv = modelToView(m.toR, m.toC);
            if (board.get(m.toR, m.toC) == null) {
                squares[vv[0]][vv[1]].setBackground(moveHintEmpty());
            } else {
                squares[vv[0]][vv[1]].setBackground(moveHintCapture());
            }
        }
    }

    private boolean myTurnInNet() {
        if (net == null) {
            return true;
        }
        return (board.isWhiteToMove() && iAmWhite) || (!board.isWhiteToMove() && !iAmWhite);
    }
    /*  // These two methods are crucial for online play. They convert coordinates
    // between the "view" (what the player sees) and the "model" (the internal
    // board logic). This allows the board to be flipped for the black player. */
    private int[] viewToModel(int vr, int vc) {
        if (whiteAtBottom) {
            return new int[]{vr, vc};
        }
        return new int[]{7 - vr, 7 - vc};
    }

    private int[] modelToView(int mr, int mc) {
        if (whiteAtBottom) {
            return new int[]{mr, mc};
        }
        return new int[]{7 - mr, 7 - mc};
    }/* This is the primary method that handles a player's click on a square.
    // The logic determines if the click is to select a piece (first click) or to
    // move a piece (second click). */

    private void onSquare(int viewR, int viewC) {
        if (!myTurnInNet()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        int[] mc = viewToModel(viewR, viewC);
        int r = mc[0], c = mc[1];
        /*If no piece is selected yet, this click attempts to select one. */
        if (selR == -1) {
            if (board.get(r, c) != null && board.get(r, c).color == (board.isWhiteToMove() ? ColorSide.WHITE : ColorSide.BLACK)) {
                selR = r;
                selC = c;
                highlightSelectionAndMoves(selR, selC);
            }
            return;
        }/*If a piece is already selected, this click is treated as a move attempt.
        // It tries to make the move, updates the history, and sends the move over the network. */

        //Algebraic notation for move
        Move m = new Move(selR, selC, r, c);
        if (board.tryMakeMove(m)) {
            history.append(Board.algebraic(m, board) + "\n");
            updateBoard();
            if (net != null) {
                try {
                    net.sendMove(m);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Network send failed: " + ex.getMessage());
                }
            }/*If the move results in a pawn promotion, a dialog box appears asking the
            // player to choose a new piece. The choice is then sent over the network. */
            // Promotion after sending move (ordering fix)
            if (board.promotionPending != null) {
                String[] opts = {"Queen", "Rook", "Bishop", "Knight"};
                int choice = JOptionPane.showOptionDialog(this, "Promote to:", "Promotion",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
                if (choice < 0) {
                    choice = 0;
                }
                String pick = opts[choice];
                board.promotePawn(board.promotionPending, pick);
                if (net != null) {
                    try {
                        net.sendPromotion(pick);
                    } catch (Exception ignore) {
                    }
                }
            }
            checkEndStates();
        }
        /* After the move attempt, clear all selections and highlights. */
        selR = selC = -1;
        clearHighlights();
        updateStatus();
    }

    private void checkEndStates() {
        if (board.isCheckmate()) {
            JOptionPane.showMessageDialog(this, "Checkmate! " + (board.isWhiteToMove() ? "Black" : "White") + " wins.");
            saveBtn.setEnabled(true);
            int want = JOptionPane.showConfirmDialog(this, "Save move history now?", "Save Moves", JOptionPane.YES_NO_OPTION);
            if (want == JOptionPane.YES_OPTION) {
                saveBtn.doClick();
            }
        } else if (board.isStalemate()) {
            JOptionPane.showMessageDialog(this, "Stalemate! Draw.");
            saveBtn.setEnabled(true);
            int want2 = JOptionPane.showConfirmDialog(this, "Save move history now?", "Save Moves", JOptionPane.YES_NO_OPTION);
            if (want2 == JOptionPane.YES_OPTION) {
                saveBtn.doClick();
            }
        }
    }/*This method redraws all the pieces on the board, and it gets the current state from the board logic object an update the text and icons
     */

    private void updateBoard() {
        int cell = Math.min(gridPanel.getWidth(), gridPanel.getHeight()) / 8;
        if (cell <= 0) {
            cell = 64;
        }
        int fontSize = (int) (cell * 0.7);
        Font f = new Font("SansSerif", Font.PLAIN, fontSize);
        for (int vr = 0; vr < 8; vr++) {
            for (int vc = 0; vc < 8; vc++) {
                int[] mc = viewToModel(vr, vc);
                Piece p = board.get(mc[0], mc[1]);
                JButton btn = squares[vr][vc];
                btn.setFont(f);
                btn.setText(p == null ? "" : p.unicode());
                btn.setForeground(p == null ? Color.BLACK : (p.color == ColorSide.WHITE ? Color.BLACK : Color.DARK_GRAY));
            }
        }/*Updates the status label at the top of the sidebar to show the current player's
    // turn, network connection status, and whether a player is in check */
    }

    private void updateStatus() {
        String turn = board.isWhiteToMove() ? "White" : "Black";
        String extra = "";
        if (board.inCheck(board.isWhiteToMove() ? ColorSide.WHITE : ColorSide.BLACK)) {
            extra = " (in check)";
        }
        if (net == null) {
            status.setText("Mode: Local | Turn: " + turn + extra);
        } else {
            status.setText("Mode: " + (iAmWhite ? "Host/White" : "Client/Black") + " | "
                    + (net.isAlive() ? "Connected ✓" : "Connecting…") + " | Turn: " + turn + extra);
        }
    }

    /*NetConnection.Listener Callbacks ---
    // The following methods are automatically called when data arrives from the
    // opponent over the network. They use `SwingUtilities.invokeLater` to ensure
    // that the GUI is updated safely from the network thread. */

    // --- NetConnection.Listener ---
    @Override
    public void onPeerMove(Move m) {
        SwingUtilities.invokeLater(() -> {
            board.tryMakeMove(m);
            // If promotion queued early, apply now if pending exists
            if (board.promotionPending != null && queuedPromotion != null) {
                board.promotePawn(board.promotionPending, queuedPromotion);
                queuedPromotion = null;
            }
            updateBoard();
            checkEndStates();
            updateStatus();
        });
    }

    @Override
    public void onPeerPromotion(String toPiece) {
        SwingUtilities.invokeLater(() -> {
            if (board.promotionPending != null) {
                board.promotePawn(board.promotionPending, toPiece);
                updateBoard();
                updateStatus();
            } else {
                queuedPromotion = toPiece; // wait until move arrives
            }
        });
    }

    @Override
    public void onPeerMessage(String msg) {
        SwingUtilities.invokeLater(() -> history.append("[Peer] " + msg + "\n"));
    }

    @Override
    public void onPeerQuit() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Peer has left."));
    }
}
