package network;

import java.io.*;
import java.net.*;

public class TcpClient extends TcpBase {
    /*
    /**
     * The constructor attempts to establish a connection to a server.
     *  host The server's IP address (e.g., "127.0.0.1").
     *  port The port number the server is listening on.
     *  IOException if a connection cannot be made.
     */ 
    public TcpClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        /*Once connected, set up the input/output streams for communication. */
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        /*Start the background thread to listen for messages from the server. */
        startReader();
    }
}