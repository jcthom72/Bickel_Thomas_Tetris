package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Context;

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

    TetrisDriver(TetrisGameEngine game, TetrisGameView view){
        this.game = game;
        this.view = view;

        currentTetrominoGraphics = null;
        currentTetromino = null;
        tetrominoGen = new Random();
        tetrominoTypes = TetrisGameEngine.TetrominoType.values();
    }

    private void updateCurrentGraphicsPosition(){
        if(currentTetrominoGraphics.length != 5){
            return;
        }

        Iterator<TetrisGameEngine.Block> blockItr = currentTetromino.iterator();
        TetrisGameEngine.Block currentBlock;

        for(int i = 0; i < 5; i++){
            currentBlock = blockItr.next();
            currentTetrominoGraphics[i].updatePos(currentBlock.getPosition().getXPixelPos(view.getBlockPixelWidth()),
                currentBlock.getPosition().getYPixelPos(view.getBlockPixelHeight()));
        }

        //update the screen to reflect the updated graphics positions
        view.updateScreen();
    }

    public boolean nextTetromino(){
        if(currentTetromino != null){ //either the current tetromino piece
            //was never spawned or it has not yet landed
            return false;
        }

        TetrisGameEngine.TetrominoType randomType;
        randomType = tetrominoTypes[tetrominoGen.nextInt(tetrominoTypes.length)];
        currentTetromino = game.spawn(randomType);

        if(currentTetromino == null){
            //GAME HAS ENDED; SPAWN LOCATION IS OBSTRUCTED
            return false;
        }

        currentTetrominoGraphics = new TetrisGameView.GraphicBlock[5];
        Iterator<TetrisGameEngine.Block> blockItr = currentTetromino.iterator();
        TetrisGameEngine.Block currentBlock;

        for(int i = 0; i < 5; i++){
            currentBlock = blockItr.next();
            currentTetrominoGraphics[i] = view.createGraphicBlock(currentTetromino.getColor(),
                    currentBlock.getPosition().getXPixelPos(view.getBlockPixelWidth()),
                    currentBlock.getPosition().getYPixelPos(view.getBlockPixelHeight()));
        }

        //update the screen after all blocks have been added
        view.updateScreen();
        return true;
        };


    public boolean move(TetrisGameEngine.Direction direction){
        if(currentTetromino == null){
            return false;
        }

        TetrisGameView.BlockAnimation animation;

        switch(direction){
            case UP: animation = view.upAnimation; break;
            case DOWN: animation = view.upAnimation; break;
            case LEFT: animation = view.upAnimation; break;
            case RIGHT: animation = view.upAnimation; break;
            default: return false; //invalid direction
        }

        if(currentTetromino.nudge(direction) == false){
            //can't move piece in direction; collision with block / boundary detected
            return false;
        }

        //animate the current tetromino's graphic block images
        view.animate(currentTetrominoGraphics, animation);
        return true;
    }

    public boolean rotate(TetrisGameEngine.Rotation rotation){
        if(currentTetromino == null){
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
}
