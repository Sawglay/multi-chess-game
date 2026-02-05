package network;


import logic.Move;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that implements the base.
 *
 * @author G Lay
 * @since 2025-07-28
 * @version UAT
 */

/*
 * An abstract base class containing the common networking logic for both the
 * TCP Server and Client. It handles the low-level details of reading and writing
 * data streams over a network socket.
 */
abstract class TcpBase implements NetConnection, Runnable {

    /*the connection socket, input/output text streams,
    // a background thread for listening, a flag to check if the connection is active,
    // and a listener to notify the main game of events. */
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected Thread thread;
    protected final AtomicBoolean alive = new AtomicBoolean(false);
    protected Listener listener;

    @Override
    public void setListener(Listener l) {
        this.listener = l;
    }

    @Override
    public boolean isAlive() {
        return alive.get();
    }

    /*
     * Creates and starts a new background thread that continuously listens for
     * incoming data from the opponent, preventing the main game from freezing.
     */
    protected void startReader() {
        alive.set(true);
        thread = new Thread(this, "NetReader");
        thread.setDaemon(true);
        thread.start();
    }/*/**
     * These methods format game actions into a specific text protocol and send them
     * to the opponent. For example, a move is sent as "M fromR fromC toR toC".
     * 'P' is for Promotion, and 'T' is for Text Message.
     */


    @Override
    public void sendMove(Move m) throws Exception {
        out.println("M " + m.fromR + " " + m.fromC + " " + m.toR + " " + m.toC);
        out.flush();/*Ensures the data is sent immediately. */
    }

    @Override
    public void sendPromotion(String toPiece) throws Exception {
        out.println("P " + toPiece);
        out.flush();
    }

    @Override
    public void sendMessage(String msg) throws Exception {
        out.println("T " + msg.replace("\n", " "));
        out.flush();
    }

    /* /**
     * Safely closes the network socket and stops the listening thread.
     */

    @Override
    public void close() {
        alive.set(false);
        try {
            if (socket != null) {
                socket.close();
        
            }} catch (IOException ignore) {
        }
    }

    /*/**
     * This is the main loop for the background listening thread. It continuously reads
     * figure out what action the opponent took, and then calls the appropriate listener method to notify the main game. */
    @Override
    public void run() {
        try {
            String line;
            while (alive.get() && (line = in.readLine()) != null) {
                final String ln = line;
                if (listener == null) {
                    continue;
                }
                if (ln.startsWith("M ")) {
                    String[] t = ln.split(" ");
                    Move m = new Move(
                            Integer.parseInt(t[1]), Integer.parseInt(t[2]),
                            Integer.parseInt(t[3]), Integer.parseInt(t[4])
                    );
                    listener.onPeerMove(m);
                } else if (ln.startsWith("P ")) {
                    listener.onPeerPromotion(ln.substring(2).trim());
                } else if (ln.startsWith("T ")) {
                    listener.onPeerMessage(ln.substring(2));
                } else if (ln.startsWith("Q")) {
                    listener.onPeerQuit();
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        } finally {
            alive.set(false);
        }
    }
}

