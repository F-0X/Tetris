package TetrisGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Board extends JPanel implements ActionListener {

    final private int blocksize = 25; //size of blocks in pixels
    final private int levelOneSpeed = 600;

    private Tetronimo currentPiece;

    Timer timer = new Timer(levelOneSpeed, this);

    private BufferedImage boardStateImage = new BufferedImage(11*25,27*20,BufferedImage.TYPE_INT_ARGB);

    private static Color[][] boardState = new Color[10][20];

    Board(){
        initBoardState();
        super.setPreferredSize(new Dimension(270, 500));
        currentPiece = new Tetronimo();
        addKeyListener(new keyboardInput());
        setFocusable(true);
        requestFocusInWindow();

        //draw boundary.
        Graphics2D g = boardStateImage.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 10, 20*25+10);
        g.fillRect(10*25+10, 0, 10*25+10, 20*25+10);
        g.fillRect(0, 20*25+10, 10*25+25, 20*25+10);

    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for (int[] pt: currentPiece.coords){
            g.setColor(currentPiece.color);
            g.fill3DRect(pt[0],pt[1],blocksize,blocksize,true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(currentPiece.moveIsPossible("DOWN", boardState)){
            currentPiece.lineDown(boardState);
        } else {
            for (int[] block : currentPiece.coords){
                boardState[(block[0]-10)/blocksize][(block[1]-10)/blocksize] = currentPiece.color;
            }
            Graphics2D g = boardStateImage.createGraphics();
            this.paint(g);
            removeFullLines(g);
            currentPiece = new Tetronimo();

            if (gameOver()){
                timer.stop();
                g.setColor(Color.BLACK);
                Font font = new Font("Courier", Font.BOLD, 45);
                g.setFont(font);
                g.drawString("GAME OVER", 13,200);
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(boardStateImage,0,0,Color.WHITE,this); //adding Colour.WHITE and making image type ARGB above
                                                           //has fixed colour problems!
    }

    private static void initBoardState(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                boardState[i][j] = null;
            }
        }
    }

    private void levelUp(){
        Status.levelUp();
        if (Status.level < 12) {
            int delay = timer.getDelay();
            timer.setDelay((int) Math.floor(0.85 * delay)); //gets 15% faster until level 12.
        }
    }

    private boolean gameOver(){
        for (int[] block : currentPiece.coords){
            if (boardState[(block[0]-10)/blocksize][(block[1]-10)/blocksize] != null){
                Status.gameIsOver = true;
                timer.stop();
                return true;
            }
        }
        return false;
    }

    private void restart(){
        initBoardState(); //reset board

        Graphics2D g = (Graphics2D) boardStateImage.getGraphics();

        g.setColor(Color.WHITE); //reset background
        g.fillRect(10,0,10*25,20*25+10);

        currentPiece = new Tetronimo();

        Status.resetScoreAndLevel();

        timer.setDelay(levelOneSpeed); //reset level
        timer.start(); //start over.

        repaint();
    }

    private void removeFullLines(Graphics2D g){
        ArrayList<Integer> fullLines = new ArrayList<>();
        boolean lineIsFull;

        for (int j = 0; j < 20; j++){
            lineIsFull = true;
            for (int i = 0; i < 10; i++){
                if (boardState[i][j] == null){
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull){
                fullLines.add(j);
            }
        }

        Status.increaseScore(fullLines.size());
        if (Status.score != 0 && Status.score%15 == 0){ //level for each 15 points. not working at all for some reason.
            //this current method means that skips in points via 3 or 4 line clears can cause
            //a levelup to be missed.
            levelUp();
            Status.score++; //total hack, should think of something better.
        }
        for (int n : fullLines){
            for (int j = n; j > 0; j--){
                for (int i = 0; i < 10; i++){
                    if (boardState[i][j-1] == null){
                        boardState[i][j] = null;
                        g.setColor(Color.white);
                        g.fillRect((i*blocksize)+10, (j*blocksize)+10, blocksize, blocksize);
                        continue;
                    }
                    boardState[i][j] = boardState[i][j-1];
                    g.setColor(boardState[i][j-1]);
                    g.fill3DRect((i*blocksize)+10, (j*blocksize)+10, blocksize, blocksize, true);
                }
            }
        }
        repaint();
    }

    class keyboardInput extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_RIGHT:
                    if (Status.paused || Status.gameIsOver){
                        break;
                    }
                    currentPiece.moveRight(boardState);
                    repaint();
                    break;

                case KeyEvent.VK_LEFT:
                    if (Status.paused || Status.gameIsOver){
                        break;
                    }
                    currentPiece.moveLeft(boardState);
                    repaint();
                    break;

                case KeyEvent.VK_DOWN:
                    if (Status.paused || Status.gameIsOver){
                        break;
                    }
                    currentPiece.moveDown(boardState);
                    repaint();
                    break;

                case KeyEvent.VK_SPACE:
                    if (Status.paused || Status.gameIsOver){
                        break;
                    }
                    while(currentPiece.lineDown(boardState)){}
                    repaint();
                    break;

                case KeyEvent.VK_UP:
                    if (Status.paused || Status.gameIsOver){
                        break;
                    }
                    currentPiece.rotate();
                    repaint();
                    break;

                case KeyEvent.VK_P:
                    if (timer.isRunning()) {
                        Status.pause();
                        timer.stop();
                    } else {
                        Status.unpause();
                        timer.start();
                    }
                    break;

                case KeyEvent.VK_R:
                    if (Status.gameIsOver){
                        Status.gameIsOver = false;
                    }
                    restart();
                    break;

                case KeyEvent.VK_M: //this kills the whole application!
                    if (Status.muted){
                        Status.muted = false;
                        Tetris.unmute();
                    }
                    else if (!Status.muted){
                        Status.muted = true;
                        Tetris.mute();
                    }
                case KeyEvent.VK_Q:
                    System.exit(0);

                default:
                    break;
            }
        }
    }
}
