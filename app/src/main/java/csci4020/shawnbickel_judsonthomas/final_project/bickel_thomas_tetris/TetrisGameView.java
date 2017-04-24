package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by sbickel20 on 3/30/17.
 */

public class TetrisGameView extends View{
    public TetrisGameView(Context context) {
        super(context);
    }

    private class GraphicBlock{
        int x; //x pixel location of block's left side
        int y; //y pixel location of block's top side
        Bitmap graphic;
        TetrisGameEngine.Block block;

        private GraphicBlock(TetrisGameEngine.Block block){
            this.block = block;
            updatePos();
            graphic = mapColorToBitmap(block.getColor());
        }

        private void updatePos(){
            x = block.getPosition().getXPixelPos(blockImgWidth);
            y = block.getPosition().getYPixelPos(blockImgHeight);
        }


    }
    private int blockImgWidth;
    private int blockImgHeight;
    private Bitmap greenBlockImg, redBlockImg, yellowBlockImg,
    blueBlockImg, purpleBlockImg, goldBlockImg, cyanBlockImg;
    private LinkedList<GraphicBlock> blocksToDraw;

    private TetrisGameEngine game;
    private TetrisGameEngine.Tetromino currentTetromino;
    private Random tetrominoGen;
    private TetrisGameEngine.TetrominoType[] tetrominoTypes;


    private void initialize(){
        game = new TetrisGameEngine(10, 10);
        blockImgWidth = getWidth() / game.getNumCols();
        blockImgHeight = getHeight() / game.getNumRows();
        //initialize scaled block images
        updateScaledBitmaps();
        blocksToDraw = new LinkedList<GraphicBlock>();
        currentTetromino = null;
        tetrominoGen = new Random();
        tetrominoTypes = TetrisGameEngine.TetrominoType.values();
    }

    private Bitmap mapColorToBitmap(TetrisGameEngine.BlockColor blockColor){
        switch(blockColor){
            case RED: return redBlockImg;
            case GREEN: return greenBlockImg;
            case BLUE: return blueBlockImg;
            case YELLOW: return yellowBlockImg;
            case GOLD: return goldBlockImg;
            case CYAN: return cyanBlockImg;
            case PURPLE: return purpleBlockImg;
            default: return null; //invalid blockColor
        }
    }

    public void updateCurrentTetrominoGraphic(){
        if(currentTetromino == null){ //do not do anything if there is no current tetromino
            return;
        }

        Iterator<GraphicBlock> tetrominoGraphicItr = blocksToDraw.descendingIterator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateScaledBitmaps();
        updateScreen();
    }

    public boolean nextTetromino(){
        if(currentTetromino != null){ /*either the current tetromino piece
         was never spawned or it has not yet landed*/
            return false;
        }

        TetrisGameEngine.TetrominoType randomType;
        randomType = tetrominoTypes[tetrominoGen.nextInt(tetrominoTypes.length)];
        currentTetromino = game.spawn(randomType);

        if(currentTetromino == null){
            //GAME HAS ENDED; SPAWN LOCATION IS OBSTRUCTED
            return false;
        }

        for(TetrisGameEngine.Block block : currentTetromino){
            blocksToDraw.add(new GraphicBlock(block));
        }

        updateScreen();
        return true;
    };

    public boolean moveDown(){
        if(currentTetromino == null){
            return false;
        }

        if(currentTetromino.nudge(TetrisGameEngine.Direction.DOWN) == false){
            //can't move piece down; collision with block / boundary detected
            return false;
        }

        //update the block graphic corresponding to the piece

    }
    moveLeft;
    moveRight;
    rotateCW;
    rotateCCW;

}
//public class BlockSurface extends SurfaceView implements SurfaceHolder.Callback{
//    private int width;
//
//
//    private int height;
//    private int blockImgWidth;
//    private int blockImgHeight;
//    private Bitmap greenBlockImg, redBlockImg, yellowBlockImg,
//    blueBlockImg, purpleBlockImg, goldBlockImg, cyanBlockImg;
//    private SurfaceHolder surfaceHolder;
//    private boolean readyToDraw;
//    private LinkedList<GraphicBlock> blocksToDraw;
//    /*
//    public TetrisGameView(Context context) {
//        super(context);
//    }
//
//    public TetrisGameView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public TetrisGameView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//    */
//
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        readyToDraw = true;
//    }
//
//    /*there is no official documentation anywhere that I have found in android's API reference
//    * to explain what the hell i, i1, and i2 mean; the official documentation shows an entirely
//    * different list of argument names for this method. Booooooooooooo!!!!!!!!!!!!!!!!!!!!!!!!!
//    * */
//    @Override
//        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//    }
//
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//
//    }
//
//    private class GraphicBlock{
//        Bitmap blockBitmap;
//        int originPixelX, originPixelY;
//    }
//
//    private void updateScaledBitmaps(){
//
//    }
//
//    private void initialize(){
//        blocksToDraw = new LinkedList<GraphicBlock>();
//        width = getWidth();
//        height = getHeight();
//
//        surfaceHolder = getHolder();
//        surfaceHolder.addCallback(this);
//
//        updateScaledBitmaps();
//
//        readyToDraw = false;
//    }
//}


