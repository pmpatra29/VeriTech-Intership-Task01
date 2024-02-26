package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RANDOM_POSITION = 25;
    
    private int DELAY = 250;
    private int count = 0;

    private int dots;
    private int score;

    private int apple_x;
    private int apple_y;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image apple;
    private Image dot;
    private Image head;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        loadImages();
        initGame();
        addKeyListener(new TAdapter());
    }

    private void loadImages() {
        ImageIcon i1 = new ImageIcon(Board.class.getResource("/snakegame/icons/apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(Board.class.getResource("/snakegame/icons/dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(Board.class.getResource("/snakegame/icons/head.png"));
        head = i3.getImage();
    }

    private void initGame() {
        dots = 3;
        score = 0;
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
            g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 10, 10);
        } else {
            showGameOverScreen(g);
        }
    }

    private void showGameOverScreen(Graphics g) {
        String totalScore = "Total Score: " + score;
        String gameOverMsg = "Game Over!";
        String restartMsg = "Press Enter to Restart";

        Font font = new Font("SAN_SERIF", Font.BOLD, 20);
        FontMetrics metrices = getFontMetrics(font);

        g.setColor(Color.YELLOW);
        g.setFont(font);
        g.drawString(totalScore, (BOARD_WIDTH - metrices.stringWidth(totalScore)) / 2, 100);
        g.drawString(gameOverMsg, (BOARD_WIDTH - metrices.stringWidth(gameOverMsg)) / 2, 150);
        g.drawString(restartMsg, (BOARD_WIDTH - metrices.stringWidth(restartMsg)) / 2, 200);
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            count++;
            score++;
            
            if(count%5 == 0) {
            	score += 5;
            	DELAY -= 20;
            	timer.setDelay(DELAY);
            }
            
            locateApple();
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (y[0] >= BOARD_HEIGHT || y[0] < 0 || x[0] >= BOARD_WIDTH || x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        private boolean isPaused = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            // Pause or resume the game when space bar is pressed
            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    isPaused = !isPaused;
                    if (isPaused) {
                        timer.stop(); // Pause the timer
                    } else {
                        timer.start(); // Resume the timer
                    }
                }
            } else if (inGame && !isPaused) { // Handle other key presses only if the game is not paused
                if (key == KeyEvent.VK_LEFT && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if (key == KeyEvent.VK_UP && !downDirection) {
                    upDirection = true;
                    leftDirection = false;
                    rightDirection = false;
                }
                if (key == KeyEvent.VK_DOWN && !upDirection) {
                    downDirection = true;
                    leftDirection = false;
                    rightDirection = false;
                }
            }
            
            // Restart the game when Enter key is pressed after game over
            if (!inGame && key == KeyEvent.VK_ENTER) {
                inGame = true;
                initGame();
            }
        }
    }
}
