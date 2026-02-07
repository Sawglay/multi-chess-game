package gui.screen;

import network.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;

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
        /* Define what happens when the "Local" button is clicked. */

        // 20 is the radius for rounded corners
        // buttons.setFocusPainted(false);
        // buttons.setBackground(Color.orange);

        // JFrame frame = this;
        // frame.getContentPane().setBackground(Color.white);

        local.addActionListener(_ -> {
            new ChessUI(null, true).setVisible(true);
            /*Create and show the main chess board for a local game.
            // 'net: null' means it's not an online game. */
            dispose();
        });
/*Define what happens when the "Host (Server)" button is clicked.
 */
        host.addActionListener(_ -> {
            String portStr = JOptionPane.showInputDialog(this, "Port to host on:", "5000");/*Ask the user which port number to use for the server. */
            if (portStr == null) return;
            int port;/*Convert the user's text input into a number. */
            try { port = Integer.parseInt(portStr.trim()); } catch (NumberFormatException ex) { /*removes any accidental spaces */
                JOptionPane.showMessageDialog(this, "Invalid port."); return;/* If the input is not a valid number, show an error and stop. */
            }
            try {
                TcpServer server = new TcpServer(port);
                new ChessUI(server, true).setVisible(true); // Host is White
                dispose();
            } catch (IOException ex) {/*If starting the server fails (e.g., port is already in use), show an error. */
                JOptionPane.showMessageDialog(this, "Failed to host: " + ex.getMessage());
            }
        });

        join.addActionListener(_ -> {
            JPanel p = new JPanel(new GridLayout(0,2,6,6));
            JTextField ipField = new JTextField("127.0.0.1");
            JTextField portField = new JTextField("5000");/*  This first block creates a small panel with two text fields, one for the "Server IP" address and one for the "Port" number */
            p.add(new JLabel("Server IP:")); p.add(ipField);
            p.add(new JLabel("Port:")); p.add(portField);
        /*After that 
     * Below, we show the pop-up dialog containing the IP and Port fields. If the user
     * clicks "OK", the code proceeds. Inside the 'try' block, it attempts to parse
     * the port number and create a new TCP Client to connect to the host.
     */
            int ok = JOptionPane.showConfirmDialog(this, p, "Join Server", JOptionPane.OK_CANCEL_OPTION);
            if (ok != JOptionPane.OK_OPTION) return;
            try {
                int port = Integer.parseInt(portField.getText().trim());
                TcpClient client = new TcpClient(ipField.getText().trim(), port);
                new ChessUI(client, false).setVisible(true); // Client is Black
                dispose();
            } catch (IOException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Failed to connect: " + ex.getMessage());
            }
        });
        /*
 * This final section assembles the window. It adds the "Local", "Host", and "Join"
 * buttons to their panel and places that panel in the center of the window.
 * It also creates a non-editable text area with helpful tips for the user and
 * places it at the bottom of the window.
 */
        buttons.add(local); buttons.add(host); buttons.add(join);
        add(buttons, BorderLayout.CENTER);

        JTextArea tips = new JTextArea("""
    Host: Open a port and wait for one client. You play White.
    Join: Connect to a host. You play Black.
""");
        tips.setEditable(false);
        tips.setOpaque(false);
        add(tips, BorderLayout.SOUTH);
    }
}
