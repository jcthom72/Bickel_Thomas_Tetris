package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by sbickel20 on 3/30/17.
 */

public class TetrisGameView extends View{
    public class GraphicBlock{
        private int x; //x pixel location of block's left side
        private int y; //y pixel location of block's top side
        private Bitmap blockImg;

        private GraphicBlock(TetrisGameEngine.BlockColor color, int x, int y){
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

        public void updatePos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static abstract class BlockAnimation{
        int numFrames;
        int movementStep;
        int currentFrame;

        protected BlockAnimation(int numFrames, int movementStep){
            updateNumFrames(numFrames);
            this.movementStep = movementStep;
            this.currentFrame = 1;
        }

        public void reset(){
            currentFrame = 1;
        }

        //NEED TO CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public void updateNumFrames(int numFrames){
            this.numFrames = numFrames;
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
        public UpTranslation(int numFrames, int movementStep){
            super(numFrames, movementStep);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y -= movementStep;

        }
    }

    public static class DownTranslation extends BlockAnimation{
        public DownTranslation(int numFrames, int movementStep){
            super(numFrames, movementStep);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y += movementStep;
        }
    }

    public static class LeftTranslation extends BlockAnimation{
        public LeftTranslation(int numFrames, int movementStep){
            super(numFrames, movementStep);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x -= movementStep;
        }
    }

    public static class RightTranslation extends BlockAnimation{
        public RightTranslation(int numFrames, int movementStep){
            super(numFrames, movementStep);
        }

        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x += movementStep;
        }
    }

    public static class Rotation extends BlockAnimation{
        private Rotation(){
            super(1, 0);
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

    private final LinkedList<GraphicBlock> blocksToDraw = new LinkedList<GraphicBlock>();

    /*
    public final UpTranslation upAnimation = new UpTranslation();
    public final DownTranslation downAnimation = new DownTranslation();
    public final LeftTranslation leftAnimation = new LeftTranslation();
    public final RightTranslation rightAnimation = new RightTranslation();
    */
    //public final Rotation ccwRotateAnimation = new Rotation();
    //public final Rotation cwRotateAnimation = new Rotation();

    private void initialize(){
        this.numRows = 10;
        this.numCols = 10;
        updateScaledBitmaps();
    }

    public TetrisGameView(Context context) {
        super(context);
        initialize();
    }

    public TetrisGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TetrisGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void updateScaledBitmaps(){
        if(getWidth() == 0 || getHeight() == 0){
            return;
        }
        blockImgWidth = getWidth() / numCols;
        blockImgHeight = getHeight() / numRows;
        /*int srcHeight = 256;
        int srcWidth = 256;
        int scaledHeight = blockImgHeight;
        int scaledWidth = blockImgWidth;
        int inSampleSize = 1;
        final int halfHeight = srcHeight / 2;
        final int halfWidth = srcWidth / 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //calculate new sample size, from official android docs
        https://developer.android.com/topic/performance/graphics/load-bitmap.html
        //

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= scaledHeight
                && (halfWidth / inSampleSize) >= scaledWidth) {
            inSampleSize *= 2;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        options.outHeight = scaledHeight;
        options.outWidth = scaledWidth;

        //create the scaled bitmaps from our block image resources
        greenBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.green_block_img, options);
        redBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.red_block_img, options);
        blueBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.blue_block_img, options);
        yellowBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_block_img, options);
        goldBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.gold_block_img, options);
        purpleBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.purple_block_img, options);
        cyanBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.cyan_block_img, options);
*/
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
        updateScaledBitmaps();
        /*upAnimation.updateNumFrames(blockImgHeight);
        downAnimation.updateNumFrames(blockImgHeight);
        leftAnimation.updateNumFrames(blockImgWidth);
        rightAnimation.updateNumFrames(blockImgWidth);*/
        updateScreen();
    }

    /*currently updates the screen in its entirety; in the future maybe we can do
    * some optimizations to make only portions of the screen get invalidated instead
    * of the entire view*/
    public void updateScreen(){
        invalidate();
    }

    public void animate(GraphicBlock[] blocksToAnimate, BlockAnimation animation){
        for(int frame = 1; frame <= animation.numFrames; frame++) {
            //animate each block passed in by a single frame
            for (GraphicBlock graphicBlock : blocksToAnimate) {
                animation.animateFrame(graphicBlock);
                animation.reset();
            }
            //update the screen
            updateScreen();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.appMainColor)); /*for testing, just draw a boring white background
            later we can make our own background, or draw a grid or something*/
        for(GraphicBlock block : blocksToDraw){
            canvas.drawBitmap(block.blockImg, block.x, block.y, null);
        }
    }

    /*creates a new graphic block, inserts it into the container of blocks to draw, and returns it
    * to the caller; IMPORTANT: calling this method does not automatically call updateScreen.*/
    public GraphicBlock createGraphicBlock(TetrisGameEngine.BlockColor color, int x, int y){
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