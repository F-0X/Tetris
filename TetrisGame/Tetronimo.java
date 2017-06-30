package TetrisGame;

import java.awt.*;
import java.util.Random;

class Tetronimo {

    public enum Shape {LINE, S, Z, L, REVERSE_L, T, SQUARE, NONE}

    private Shape[] values = Shape.values();
    private Shape shape;

    private int blocksize = 25;
    private int boardWidth = 10*blocksize;
    private int boardHeight = 20*blocksize;
    private int center = boardWidth/2;

    Color color = Color.white;

    private Random random = new Random();

    int[][] coords;

    private int[][] lineShape = new int[][]{
            {center+10,10},
            {center+10,10+blocksize},
            {center+10,10+2*blocksize},
            {center+10,10+3*blocksize} };

    private int[][] tShape = new int[][]{
            {center+10-blocksize, blocksize+10},
            {center+10, blocksize+10},
            {center+10,10},
            {center+10+blocksize,blocksize+10}};

    private int[][] lShape = new int[][]{
            {center+10, 10},
            {center+10,10+blocksize},
            {center+10, 10+2*blocksize},
            {center+10+blocksize,2*blocksize+10}};

    private int[][] sShape = new int[][]{
            {center+10-blocksize, blocksize+10},
            {center+10,10},
            {center+10+blocksize,10},
            {center+10, blocksize+10}};

    private int[][] zShape = new int[][]{
            {center+10-blocksize, 10},
            {center+10,10},
            {center+10+blocksize,blocksize+10},
            {center+10, blocksize+10}};

    private int[][] SqShape = new int[][]{
            {center+10-blocksize, 10},
            {center+10,10},
            {center+10-blocksize,blocksize+10},
            {center+10, blocksize+10}};

    private int[][] revLShape = new int[][]{
            {center+10, 10},
            {center+10,10+blocksize},
            {center+10-blocksize,10+2*blocksize},
            {center+10,10+2*blocksize}};


    Tetronimo(){
        this.shape = setShape();
        switch (shape){
            case LINE:
                this.coords = lineShape;
                this.color = Color.cyan;
                break;

            case T:
                this.coords = tShape;
                this.color = Color.magenta;
                break;

            case L:
                this.coords = lShape;
                this.color = Color.orange;
                break;

            case S:
                this.coords = sShape;
                this.color = Color.red;
                break;

            case Z:
                this.coords = zShape;
                this.color = Color.green;
                break;

            case SQUARE:
                this.coords = SqShape;
                this.color = Color.yellow;
                break;

            case REVERSE_L:
                this.coords = revLShape;
                this.color = Color.blue;
                break;
        }
    }

    private Shape setShape(){ //chooses shape of new tetronimo.
        int r = random.nextInt(7);
        return values[r];
    }

    void moveRight(Color[][] boardState){
        if (moveIsPossible("RIGHT", boardState)) {
            for (int[] pt : this.coords) {
                pt[0] += blocksize;
            }
        }
    }

    void moveDown(Color[][] boardState){
        if (moveIsPossible("DOWN", boardState)) {
            for (int[] pt : this.coords) {
                pt[1] += blocksize;
            }
        }
    }

    void moveLeft(Color[][] boardState){
        if (moveIsPossible("LEFT", boardState)) {
            for (int[] pt : this.coords) {
                pt[0] -= blocksize;
            }
        }
    }

    boolean lineDown(Color[][] boardState){ //the line down enforced by time. Is boolean, not void, hence
        if (moveIsPossible("DOWN", boardState)) {  // separate from moveDown.
            for (int[] pt : this.coords) {
                     pt[1] += blocksize;
            }
            return true;
        }
        return false;
    }

    boolean moveIsPossible(String motion, Color[][] boardState){
        switch (motion) {
            case "RIGHT":
                if (this.coords[0][0] + blocksize < boardWidth &&
                        this.coords[1][0] + blocksize < boardWidth &&
                        this.coords[2][0] + blocksize < boardWidth &&
                        this.coords[3][0] + blocksize < boardWidth) {
                    for (int[] block : this.coords) {
                        if (null != boardState[(block[0] - 10) / blocksize + 1][(block[1] - 10) / blocksize]) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;

            case "LEFT":
                if (this.coords[0][0] - blocksize > 0 &&
                        this.coords[1][0] - blocksize > 0 &&
                        this.coords[2][0] - blocksize > 0 &&
                        this.coords[3][0] - blocksize > 0) {
                    for (int[] block : this.coords) {
                        if (null != boardState[(block[0] - 10) / blocksize - 1][(block[1] - 10) / blocksize]) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;

            case "DOWN":
                if (this.coords[0][1] + blocksize < boardHeight &&
                        this.coords[1][1] + blocksize < boardHeight &&
                        this.coords[2][1] + blocksize < boardHeight &&
                        this.coords[3][1] + blocksize < boardHeight) {
                    for (int[] block : this.coords) {
                        if (null != boardState[(block[0] - 10) / blocksize][(block[1] - 10) / blocksize + 1]) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;

            default:
                System.out.println("WEIRD ISSUE");
                return false;
        }
    }

    void rotate(){//like how it does L and Reverse-L. S and Z might want a change.
        if (this.shape == Shape.SQUARE){
            return;
        }
        int centerx = this.coords[1][0];
        int centery = this.coords[1][1];
        boolean shoveRight = false;
        boolean shoveLeft = false;
        boolean shoveRightTwice = false;
        boolean shoveLeftTwice = false;

        int temp;
        for (int[] point : this.coords){
            temp = point[0]-centerx;
            point[0] = -(point[1] - centery) + centerx;
            point[1] = temp + centery;

            if (point[0] < 0){
                shoveRight = true;
            } else if (point [0] > 10*blocksize){
                shoveLeft = true;
            }

            if (point[0] <= -2*blocksize+10){
                shoveRightTwice = true;
            } else if (point[0] >= 11*blocksize+10){
                shoveLeftTwice = true;
            }
        }

        if (shoveRightTwice){ //to handle line piece
            for (int[] point : this.coords){
                point[0] += 2*blocksize;
            }
            return;
        } else if (shoveLeftTwice){
            for (int[] point : this.coords){
                point[0] -= 2*blocksize;
            }
            return;
        }

        if (shoveRight){ //for everything else.
            for (int[] point : this.coords){
                point[0] += blocksize;
            }
        } else if (shoveLeft){
            for (int[] point : this.coords){
                point[0] -= blocksize;
            }
        }
    }
}