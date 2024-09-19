import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth = 600;
        int boardheight = boardwidth;

        JFrame frame = new JFrame("Snake bite");
        frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Snake snake = new Snake(boardwidth, boardheight);
        frame.add(snake);
        frame.pack();
        snake.requestFocus(); 
    }
}
