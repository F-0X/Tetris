
package TetrisGame;

import javax.swing.*;

class Status extends JPanel {

    static int score = 0;
    static int level = 1;

    private static JLabel scoreLabel = new JLabel();
    private static JLabel pauseLabel = new JLabel();
    private static JLabel levelLabel = new JLabel();

    static boolean paused = false;
    static boolean gameIsOver = false;
    static boolean muted = false;

    Status(){
        labelInit();
        this.add(levelLabel);
        this.add(scoreLabel);
        this.add(pauseLabel);
    }

    private static void labelInit(){
        levelLabel.setText("Level: " + level);
        scoreLabel.setText("      Score: " + 0);
    }

    static void increaseScore(int n){
        score += Math.pow(2,n-1);
        scoreLabel.setText("      Score: " + score);
    }

    static void levelUp(){
        level++;
        levelLabel.setText("Level: " + level);
    }

    static void pause(){
        paused = true;
        pauseLabel.setText(" PAUSED ");
    }

    static void unpause(){
        paused = false;
        pauseLabel.setText("");
    }

    static void resetScoreAndLevel() {
        score = 0;
        scoreLabel.setText("      Score: " + score);
        level = 1;
        levelLabel.setText("Level: " + level);
    }
}
