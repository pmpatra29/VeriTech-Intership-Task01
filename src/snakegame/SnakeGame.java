package snakegame;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        super("Snake Game");
        initializeGame();
    }

    private void initializeGame() {
        add(new Board());
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the window is closed
        setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
