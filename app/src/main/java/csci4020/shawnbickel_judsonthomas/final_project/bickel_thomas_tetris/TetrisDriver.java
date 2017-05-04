package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by sbickel20 on 4/21/17.
 */

public class TetrisDriver {
    private TetrisGameEngine game;
    private TetrisGameView view;
    private TetrisGameEngine.Tetromino currentTetromino;
    private TetrisGameView.GraphicBlock[] currentTetrominoGraphics;
    private Random tetrominoGen;
    private TetrisGameEngine.TetrominoType[] tetrominoTypes;
    public enum MoveStatus{SUCCESS, ERROR_ANIMATING, ERROR_COLLISION, ERROR_NO_PIECE, ERROR_INVALID_DIRECTION};

    TetrisDriver(TetrisGameEngine game, TetrisGameView view){
        this.game = game;
        this.view = view;

        currentTetrominoGraphics = null;
        currentTetromino = null;
        tetrominoGen = new Random();
        tetrominoTypes = TetrisGameEngine.TetrominoType.values();
    }



    private void updateCurrentGraphicsPosition(){
        if(currentTetrominoGraphics.length != 4){
            return;
        }

        Iterator<TetrisGameEngine.Block> blockItr = currentTetromino.iterator();
        TetrisGameEngine.Block currentBlock;

        for(int i = 0; i < 4; i++){
            currentBlock = blockItr.next();
            currentTetrominoGraphics[i].updatePos(currentBlock.getPosition().getXPixelPos(view.getBlockPixelWidth()),
                    currentBlock.getPosition().getYPixelPos(view.getBlockPixelHeight()));
        }

        //update the screen to reflect the updated graphics positions
        view.updateScreen();
    }

    public boolean nextTetromino(){
        //COMMENTED OUT FOR TESTING
        /*if(currentTetromino != null){ //either the current tetromino piece
            //was never spawned or it has not yet landed
            return false;
        }*/

        TetrisGameEngine.TetrominoType randomType;
        randomType = tetrominoTypes[tetrominoGen.nextInt(tetrominoTypes.length)];
        currentTetromino = game.spawn(randomType);
        if(currentTetromino == null){
            //GAME HAS ENDED; SPAWN LOCATION IS OBSTRUCTED
            return false;
        }

        currentTetrominoGraphics = new TetrisGameView.GraphicBlock[4];
        Iterator<TetrisGameEngine.Block> blockItr = currentTetromino.iterator();
        TetrisGameEngine.Block currentBlock;

        for(int i = 0; i < 4; i++){
            currentBlock = blockItr.next();
            currentTetrominoGraphics[i] = view.createGraphicBlock(currentTetromino.getColor(),
                    currentBlock.getPosition().getXPixelPos(view.getBlockPixelWidth()),
                    currentBlock.getPosition().getYPixelPos(view.getBlockPixelHeight()));
        }

        //update the screen after all blocks have been added
        view.updateScreen();
        return true;
    };


    public MoveStatus move(TetrisGameEngine.Direction direction){
        /*if there is no piece to nudge*/
        if(currentTetromino == null){
            return MoveStatus.ERROR_NO_PIECE;
        }

        /*if the screen is currently animating we do not allow a move to occur;
        * we may change this in the future to allow queueing of moves (probably with
        * a max queue size of 2 (up to one move can be queued while another move is occurring)*/
        if(view.isCurrentlyAnimating()){
            return MoveStatus.ERROR_ANIMATING;
        }

        TetrisGameView.BlockAnimation animation;

        switch(direction){
            case UP: animation = new TetrisGameView.UpTranslation(view.getBlockPixelHeight(), 1, 150); break;
            case DOWN: animation = new TetrisGameView.DownTranslation(view.getBlockPixelHeight(), 1, 150); break;
            case LEFT: animation = new TetrisGameView.LeftTranslation(view.getBlockPixelWidth(), 1, 150); break;
            case RIGHT: animation = new TetrisGameView.RightTranslation(view.getBlockPixelWidth(), 1, 150); break;
            default: return MoveStatus.ERROR_INVALID_DIRECTION; //invalid direction
        }

        if(currentTetromino.nudge(direction) == false){
            //can't move piece in direction; collision with block / boundary detected
            return MoveStatus.ERROR_COLLISION;
        }

        //animate the current tetromino's graphic block images
        view.animate(currentTetrominoGraphics, animation);

        //reset the animation
        animation.reset();
        return MoveStatus.SUCCESS;
    }

    public boolean rotate(TetrisGameEngine.Rotation rotation){
        if(currentTetromino == null){
            return false;
        }

        /*prevent a rotation from occurring while the tetromino piece is in the middle
        of an animation*/
        if(view.isCurrentlyAnimating()){
            return false;
        }

        if(currentTetromino.rotate(rotation) == false){
            //can't rotate piece by rotation; collision with block / boundary detected
            return false;
        }

        //update the position directly since there is no animation to perform
        updateCurrentGraphicsPosition();
        return true;
    }

    void updateRows(){
        if(currentTetromino == null){
            return;
        }
        int numRowsToShiftDown = 0;
        int lowestRow = -1;
        int lowestRowToCheck = currentTetromino.getLowestBlockPosition().getY();
        int highestRowToCheck = currentTetromino.getHighestBlockPosition().getY();

        for(int i = lowestRowToCheck; i >= highestRowToCheck; i--){
            if(game.removeRow(i)){
                view.removeRow(i);
                if(numRowsToShiftDown == 0){
                    lowestRow = i-1;
                }
                numRowsToShiftDown++;
            }

            else if(numRowsToShiftDown != 0){
                game.shiftDownRows(lowestRow, numRowsToShiftDown);
                view.shiftDownRows(lowestRow, numRowsToShiftDown);
                numRowsToShiftDown = 0;
            }
        }

        if(numRowsToShiftDown == 0){
            return;
        }

        game.shiftDownRows(lowestRow, numRowsToShiftDown);
        view.shiftDownRows(lowestRow, numRowsToShiftDown);
        view.updateScreen();
        return;
    }

    public int getRows(){
        return game.getNumRows();
    }

    public int getColumns(){
        return game.getNumCols();
    }

    public TetrisGameEngine.BlockColor getCurrentTetrominoColor(){
       return currentTetromino.getColor();
    }

}