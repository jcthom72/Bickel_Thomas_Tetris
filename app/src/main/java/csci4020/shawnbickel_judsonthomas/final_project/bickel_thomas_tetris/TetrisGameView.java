package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by sbickel20 on 3/30/17.
 */

public class TetrisGameView extends View{
    protected class GraphicBlock{
        private float x; //x pixel location of block's left side
        private float y; //y pixel location of block's top side
        private Bitmap blockImg;

        private GraphicBlock(TetrisGameEngine.BlockColor color, float x, float y){
            switch(color){
                case RED: blockImg = redBlockImg; break;
                case GREEN: blockImg = greenBlockImg; break;
                case CYAN: blockImg = cyanBlockImg; break;
                case PURPLE: blockImg = purpleBlockImg; break;
                case BLUE: blockImg = blueBlockImg; break;
                case GOLD: blockImg = goldBlockImg; break;
                case YELLOW: blockImg = yellowBlockImg; break;
            }
            updatePos(x, y);
        }

        public void updatePos(float x, float y){
            this.x = x;
            this.y = y;
        }
    }

    /*class responsible for performing animations to tetrominos' graphic blocks;
     animations are performed asynchronously to the main UI thread*/
    private class TetrominoAnimator extends Thread{
        private BlockAnimation animationToRun;
        private GraphicBlock[] blocksToAnimate;
        private boolean isAnimating;


        TetrominoAnimator(BlockAnimation animationToRun, GraphicBlock[] blocksToAnimate){
            this.animationToRun = animationToRun;
            this.blocksToAnimate = blocksToAnimate;
            isAnimating = false;
        }

        @Override
        public void run() {
            isAnimating = true;
            int durationPerFrame = animationToRun.duration / animationToRun.numFrames;

            for(int frame = 1; frame <= animationToRun.numFrames; frame++) {
                //animate each block passed in by a single frame
                for (GraphicBlock graphicBlock : blocksToAnimate) {
                    animationToRun.animateFrame(graphicBlock);
                    animationToRun.reset();
                }

                postInvalidate();

                //sleep for a durationPerFrame amount of time
                try {
                    Thread.sleep(durationPerFrame);
                } catch (InterruptedException e) {
                }
            }

            isAnimating = false;
        }
    }

    public static abstract class BlockAnimation{
        private int numFrames;
        protected int movementStep;
        private int currentFrame;
        private int duration; //number of milliseconds the animation should last

        protected BlockAnimation(int numFrames, int movementStep, int duration){
            this.numFrames = numFrames;
            this.movementStep = movementStep;
            this.currentFrame = 1;
            this.duration = duration > 0 ? duration : 0; /*validate our duration*/
        }

        public void reset(){
            currentFrame = 1;
        }

        protected abstract void frameAction(GraphicBlock blockToAnimate);

        //applies a single frame worth of animation to "blockToAnimate", given the
        //specified frame action
        private void animateFrame(GraphicBlock blockToAnimate){
            if(currentFrame <= numFrames) {
                frameAction(blockToAnimate);
            }
            currentFrame++;
        }
    }

    public static class UpTranslation extends BlockAnimation{
        public UpTranslation(int numFrames, int movementStep, int duration){
            super(numFrames, movementStep, duration);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y -= movementStep;

        }
    }

    public static class DownTranslation extends BlockAnimation{
        public DownTranslation(int numFrames, int movementStep, int duration){
            super(numFrames, movementStep, duration);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y += movementStep;
        }
    }

    public static class LeftTranslation extends BlockAnimation{
        public LeftTranslation(int numFrames, int movementStep, int duration){
            super(numFrames, movementStep, duration);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x -= movementStep;
        }
    }

    public static class RightTranslation extends BlockAnimation{
        public RightTranslation(int numFrames, int movementStep, int duration){
            super(numFrames, movementStep, duration);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x += movementStep;
        }
    }

    public static class Rotation extends BlockAnimation{
        private Rotation(){
            super(1, 0, 0);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate) {
            /*currently rotations do not have an animation; they are
            * performed instantaneously whenever the game engine block locations
            * are updated. However, that may change one day!*/
        }
    }

    private int numRows, numCols;
    private int blockImgWidth;
    private int blockImgHeight;
    private Bitmap greenBlockImg, redBlockImg, yellowBlockImg,
            blueBlockImg, purpleBlockImg, goldBlockImg, cyanBlockImg;
    private TetrominoAnimator tetrominoAnimator;
    private final LinkedList<GraphicBlock> blocksToDraw = new LinkedList<GraphicBlock>();

    private boolean isInitialized;

    /*
    public final UpTranslation upAnimation = new UpTranslation();
    public final DownTranslation downAnimation = new DownTranslation();
    public final LeftTranslation leftAnimation = new LeftTranslation();
    public final RightTranslation rightAnimation = new RightTranslation();
    */
    //public final Rotation ccwRotateAnimation = new Rotation();
    //public final Rotation cwRotateAnimation = new Rotation();

    /*initializes the tetris game view, giving it the specified number of rows
    * and columns; returns true if the tetris game view was initialized; false otherwise
    * (indicating that the tetris game view object has already been initialized)*/
    public boolean initialize(int numRows, int numCols){
        if(!isInitialized) {
            this.numRows = numRows;
            this.numCols = numCols;
            isInitialized = true;
            updateScaledBitmaps();
            return true;
        }
        return false;
    }

    public TetrisGameView(Context context) {
        super(context);
        isInitialized = false;
    }

    public TetrisGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInitialized = false;
    }

    public TetrisGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isInitialized = false;
    }

    private void updateScaledBitmaps(){
        if(!isInitialized){
            return;
        }

        if(getWidth() == 0 || getHeight() == 0){
            return;
        }
        blockImgWidth = getWidth() / numCols;
        blockImgHeight = getHeight() / numRows;

        greenBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.green_block_img), blockImgWidth, blockImgHeight, false);
        redBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.red_block_img), blockImgWidth, blockImgHeight, false);
        blueBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.blue_block_img), blockImgWidth, blockImgHeight, false);
        yellowBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.yellow_block_img), blockImgWidth, blockImgHeight, false);
        goldBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.gold_block_img), blockImgWidth, blockImgHeight, false);
        purpleBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.purple_block_img), blockImgWidth, blockImgHeight, false);
        cyanBlockImg = Bitmap.createScaledBitmap(decodeResource(getResources(), R.drawable.cyan_block_img), blockImgWidth, blockImgHeight, false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!isInitialized){
            return;
        }
        updateScaledBitmaps();
        updateScreen();
    }

    /*currently updates the screen in its entirety; in the future maybe we can do
    * some optimizations to make only portions of the screen get invalidated instead
    * of the entire view*/
    public void updateScreen(){
        postInvalidate();
    }

    public boolean isCurrentlyAnimating(){
        return tetrominoAnimator != null && tetrominoAnimator.isAnimating;
    }

    public void animate(final GraphicBlock[] blocksToAnimate, final BlockAnimation animation){
        if(!isInitialized){
            return;
        }

        tetrominoAnimator = new TetrominoAnimator(animation, blocksToAnimate);
        tetrominoAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.appMainColor)); /*for testing, just draw a boring white background
            //later we can make our own background, or draw a grid or something*/

        for(GraphicBlock block : blocksToDraw){
            canvas.drawBitmap(block.blockImg, block.x, block.y, null);
        }
    }

    void removeRow(int rowToRemove){
        int yPixelLocation = rowToRemove * blockImgHeight;
        Iterator<GraphicBlock> it = blocksToDraw.iterator();
        while(it.hasNext()){
            if(it.next().y == yPixelLocation){
                it.remove();
            }
        }
    }

    void shiftDownRows(int lowestRow, int numRowsToShiftDown){
        int yPixelLocation = lowestRow * blockImgHeight;
        Iterator<GraphicBlock> it = blocksToDraw.iterator();
        GraphicBlock blockToUpdate;

        while(it.hasNext()){
            blockToUpdate = it.next();
            if(blockToUpdate.y <= yPixelLocation){
                blockToUpdate.updatePos(blockToUpdate.x, blockToUpdate.y + numRowsToShiftDown * blockImgHeight);
            }
        }
    }

    /*creates a new graphic block, inserts it into the container of blocks to draw, and returns it
    * to the caller; IMPORTANT: calling this method does not automatically call updateScreen.*/
    public GraphicBlock createGraphicBlock(TetrisGameEngine.BlockColor color, int x, int y){
        if(!isInitialized){
            return null;
        }

        GraphicBlock blockToCreate = new GraphicBlock(color, x, y);
        blocksToDraw.add(blockToCreate);
        return blockToCreate;
    }
    public LinkedList<GraphicBlock> getGraphicBlocks(){
        return blocksToDraw; /*directly exposes the underlying graphic block container. This is safe to do because
        the TetrisGameView itself isn't responsible for preserving the integrity of its data; the driver is. This is because the driver is the class
        that knows what is a valid state of the game's view since it is the class that attaches the engine's game logic to the view*/
    }

    public int getBlockPixelWidth(){
        return blockImgWidth;
    }

    public int getBlockPixelHeight(){
        return blockImgHeight;
    }

}