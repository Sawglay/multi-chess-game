package gui.screen;

import javax.swing.SwingUtilities;
/*Its only job is to start up the Graphical User Interface */

/**
 * Main class to run the application.
 * 
 * @author G Lay, Min Khant, Sonali
 * @since 2025-08-01
 * @version UAT
 */

public class Main {
    public static void main(String[] args) {/* This line ensures that our user interface is created on the correct thread, */
        SwingUtilities.invokeLater(() -> new ModePicker().setVisible(true));
    }
}
