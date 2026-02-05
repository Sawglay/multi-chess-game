package network;
/**
* Defines a standard contract for all network connection types (Server, Client).
* This ensures they all have the same core functions for sending and receiving data.
*/
import logic.Move;
public interface NetConnection {
    interface Listener {
        void onPeerMove(Move m); /*Handles opponent's move */
        void onPeerPromotion(String toPiece);/*Handles opponent's pawn promotion */
        void onPeerMessage(String msg); /*Opponent's text message */
        void onPeerQuit();/*Opponent's disconnection */
    }
    void setListener(Listener l);
    void sendMove(Move m) throws Exception;
    void sendPromotion(String toPiece) throws Exception;
    void sendMessage(String msg) throws Exception;
    /* Methods for managing the connection status */
    void close(); /*Terminates the connection */
    boolean isAlive();/*chek whether it is connected or not */
}
