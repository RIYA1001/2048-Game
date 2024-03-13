import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game2048 extends JFrame {
    private static final int SIZE = 4;
    private static final int TILE_SIZE = 100;

    private int[][] board;

    public Game2048() {
        setTitle("2048 Game");
        setSize(SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initGame();
        addKeyListener(new KeyHandler());

        setFocusable(true);
        requestFocusInWindow();
        setVisible(true); // Add this line to explicitly request focus
    }

    private void initGame() {
        board = new int[SIZE][SIZE];
        addRandomTile();
        addRandomTile();
    }

    private void addRandomTile() {
        int emptyTiles = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 0) {
                    emptyTiles++;
                }
            }
        }

        if (emptyTiles > 0) {
            int randomPosition = (int) (Math.random() * emptyTiles) + 1;
            int count = 0;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        count++;
                        if (count == randomPosition) {
                            board[i][j] = Math.random() < 0.9 ? 2 : 4; // 90% chance of 2, 10% chance of 4
                            return;
                        }
                    }
                }
            }
        }
    }

    private void moveTiles(Direction direction) {
        boolean moved = false;
        switch (direction) {
            case UP:
                moved = moveUp();
                break;
            case DOWN:
                moved = moveDown();
                break;
            case LEFT:
                moved = moveLeft();
                break;
            case RIGHT:
                moved = moveRight();
                break;
        }
        if (moved) {
            addRandomTile();
            repaint();
        }
    }

    private boolean moveUp() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = 1; i < SIZE; i++) {
                if (board[i][j] != 0) {
                    int k = i;
                    while (k > 0 && board[k - 1][j] == 0) {
                        board[k - 1][j] = board[k][j];
                        board[k][j] = 0;
                        k--;
                        moved = true;
                    }
                    if (k > 0 && board[k - 1][j] == board[k][j]) {
                        board[k - 1][j] *= 2;
                        board[k][j] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveDown() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = SIZE - 2; i >= 0; i--) {
                if (board[i][j] != 0) {
                    int k = i;
                    while (k < SIZE - 1 && board[k + 1][j] == 0) {
                        board[k + 1][j] = board[k][j];
                        board[k][j] = 0;
                        k++;
                        moved = true;
                    }
                    if (k < SIZE - 1 && board[k + 1][j] == board[k][j]) {
                        board[k + 1][j] *= 2;
                        board[k][j] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 1; j < SIZE; j++) {
                if (board[i][j] != 0) {
                    int k = j;
                    while (k > 0 && board[i][k - 1] == 0) {
                        board[i][k - 1] = board[i][k];
                        board[i][k] = 0;
                        k--;
                        moved = true;
                    }
                    if (k > 0 && board[i][k - 1] == board[i][k]) {
                        board[i][k - 1] *= 2;
                        board[i][k] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = SIZE - 2; j >= 0; j--) {
                if (board[i][j] != 0) {
                    int k = j;
                    while (k < SIZE - 1 && board[i][k + 1] == 0) {
                        board[i][k + 1] = board[i][k];
                        board[i][k] = 0;
                        k++;
                        moved = true;
                    }
                    if (k < SIZE - 1 && board[i][k + 1] == board[i][k]) {
                        board[i][k + 1] *= 2;
                        board[i][k] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    // Implement moveDown(), moveLeft(), and moveRight() methods here

    private void paintTile(Graphics g, int value, int x, int y) {
        g.setColor(getTileColor(value));
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        drawCenteredString(g, String.valueOf(value), new Rectangle(x, y, TILE_SIZE, TILE_SIZE),
                new Font("Arial", Font.BOLD, 20));
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private Color getTileColor(int value) {
        // You can customize the tile colors based on the value
        // For simplicity, let's use a gradient for now
        return new Color(Math.min(255, value * 20), 0, 0);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int x = j * TILE_SIZE;
                int y = i * TILE_SIZE;

                paintTile(g, board[i][j], x, y);
            }
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private class KeyHandler implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    moveTiles(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    moveTiles(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    moveTiles(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveTiles(Direction.RIGHT);
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game2048 game = new Game2048();
            game.setVisible(true);
        });
    }
}
