import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Snake extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardwidth;
    int boardheight;
    int tilesize = 25;
    // head
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // food
    Tile food;
    Random random;

    // gamelogic
    Timer gameLoop;
    int velocityx;
    int velocityy;
    boolean gameOver = false;
    boolean foodVisible = true; 
    int foodBlinkInterval = 0;  

    Snake(int boardwidth, int boardheight) {
        this.boardwidth = boardwidth;
        this.boardheight = boardheight;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(3, 3);
        random = new Random();
        placeFood();

        velocityx = 0;
        velocityy = 0;

        gameLoop = new Timer(170, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    // Draw function area
    public void draw(Graphics g) {
        // food - make it blink
        if (foodVisible) {
            g.setColor(Color.white);
            g.fillOval(food.x * tilesize, food.y * tilesize, tilesize, tilesize);
        }

        // snake HEAD
        g.setColor(Color.red);
        g.fillOval(snakeHead.x * tilesize, snakeHead.y * tilesize, tilesize, tilesize);

        // snake BODY
        g.setColor(Color.green);
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillOval(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize);
        }

        // Score
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + (snakeBody.size()), tilesize, tilesize);
        } else {
            g.setColor(Color.white);
            g.drawString("Score: " + (snakeBody.size()), tilesize, tilesize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardwidth / tilesize);
        food.y = random.nextInt(boardheight / tilesize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Store the current head position before moving the body
        int previousHeadX = snakeHead.x;
        int previousHeadY = snakeHead.y;

        // Move snake head
        snakeHead.x += velocityx;
        snakeHead.y += velocityy;

        // Move the snake body
        if (snakeBody.size() > 0) {
            // Shift body parts forward
            for (int i = snakeBody.size() - 1; i > 0; i--) {
                snakeBody.get(i).x = snakeBody.get(i - 1).x;
                snakeBody.get(i).y = snakeBody.get(i - 1).y;
            }
            // Move first body part to the previous head location
            snakeBody.get(0).x = previousHeadX;
            snakeBody.get(0).y = previousHeadY;
        }

        // FOOD - check collision and grow snake
        if (collision(snakeHead, food)) {
            // Add new tile at the previous head position (snake grows)
            snakeBody.add(new Tile(previousHeadX, previousHeadY));
            placeFood();
        }

        // Gameover conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (snakeHead.x == snakePart.x && snakeHead.y == snakePart.y) {
                gameOver = true;
            }
        }
        if (snakeHead.x < 0 || snakeHead.x >= boardwidth / tilesize || snakeHead.y < 0
                || snakeHead.y >= boardheight / tilesize) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }

        // Blink food every 300 milliseconds (you can adjust the speed by changing the divisor)
        foodBlinkInterval++;
        if (foodBlinkInterval % 3 == 0) {
            foodVisible = !foodVisible; // Toggle food visibility
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityy != 1) {
            velocityx = 0;
            velocityy = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityy != -1) {
            velocityx = 0;
            velocityy = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityx != 1) {
            velocityx = -1;
            velocityy = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityx != -1) {
            velocityx = 1;
            velocityy = 0;
        }
    }

    // Not used functions
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
