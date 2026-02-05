package network;

import java.io.*;
import java.net.*;

public class TcpServer extends TcpBase {
    private final ServerSocket server; /* The main socket that listens for incoming connections. */
/*/**
     * The constructor creates a server that listens on a specific port. It then
     * immediately starts a new thread to handle the waiting process.
     * port The port number to listen on.
     * IOException if the server cannot be created (e.g., port is in use).
     */ 
    public TcpServer(int port) throws IOException {
        server = new ServerSocket(port); /* A new thread is created to wait for a client. This is crucial so that the main game window can remain responsive while the server is waiting. */
        new Thread(() -> {
            try {
                Socket s = server.accept();
                socket = s; /*Once connected, set up the input/output streams and start the reader thread. */
                in = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
                out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"), true);
                startReader();
            } catch (IOException e) { /*If an error occurs (e.g., the server is closed while waiting), clean up. */
                close();
            }
        }, "TcpAccept").start();
    }
    /*
     * Overrides the default close method to ensure that both the client connection
     *  and the main server socket are properly closed.
      */
    @Override public void close() {
        super.close();
        try { server.close(); } catch (IOException ignore) {}
    } /*/ Closes the main server listener. */
}
