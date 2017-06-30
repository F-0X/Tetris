package TetrisGame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by F-0X on 15/06/17.
 */

public class Tetris {

    private static MusicThread music = new MusicThread();

    public static void main(String[] args){
        JFrame frame = new JFrame();

        frame.setSize(10*25+25, 20*25+80);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

        Board board = new Board();
        Status stats = new Status();
        stats.setPreferredSize(new Dimension(11*25,5));

        container.add(board);
        container.add(stats);
        frame.add(container);

        board.timer.start();

        frame.setVisible(true);
        music.run();
        }

    static void mute(){
        music.muteMusic();
    }

    static void unmute(){
        music.unmuteMusic();
    }
}





