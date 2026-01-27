package com.globalacademy.chessgame.gui.screen;

// import com.globalacademy.finalChess.network.TcpClient;
// import com.globalacademy.finalChess.network.TcpServer;
import java.awt.*;
// import java.io.IOException;
import javax.swing.*;
/**
 * Main class to run the application.
 * 
 * @author Vuthy & Joseph
 * @since 2025-07-22
 * @version UAT
 */

 /**
 * This class represents the first window the user sees.
 * It allows them to pick a game mode: Local, Host (Server), or Join (Client).
 */
public class ModePicker extends JFrame {

    public ModePicker() {
        super("SimpleChess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);/* Make the program close when this window is closed. */
        setSize(450, 450);/*Set the window's width and height */
        setLocationRelativeTo(null);/*Set the window's width and height */
        setLayout(new BorderLayout(8,8));/* for arranging components. */
        setResizable(false);

        JLabel title = new JLabel("Choose to play", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 25f));
        add(title, BorderLayout.NORTH);/* Add the title to the top of the window. */

        JPanel buttons = new JPanel(new GridLayout(3, 0, 12, 25));/*Create a panel to hold the buttons. */
        buttons.setBorder(BorderFactory.createEmptyBorder(30, 90, 40, 90));
        JButton local = new JButton("Local (Two Players)");
        JButton host  = new JButton("White Host (Server)");
        JButton join  = new JButton("Black Join (Client)");

    }}