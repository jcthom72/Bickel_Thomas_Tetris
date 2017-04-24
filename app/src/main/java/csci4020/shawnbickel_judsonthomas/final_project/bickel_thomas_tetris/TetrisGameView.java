package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.RotateAnimation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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

        private BlockAnimation(int numFrames, int movementStep){
            this.numFrames = numFrames;
            this.movementStep = movementStep;
            this.currentFrame = 1;
        }

        private void reset(){
            currentFrame = 1;
        }

        protected abstract void frameAction(GraphicBlock blockToAnimate);

        //applies a single frame worth of animation to "blockToAnimate", given the
        //specified frame action
        private void animateFrame(GraphicBlock blockToAnimate){
            if(currentFrame < numFrames) {
                frameAction(blockToAnimate);
            }
            currentFrame++;
        }
    }

    public abstract class VerticalTranslation extends BlockAnimation{
        private VerticalTranslation(){
            super(blockImgHeight, 1);
        }
    }

    public abstract class HorizontalTranslation extends BlockAnimation{
        private HorizontalTranslation(){
            super(blockImgWidth, 1);
        }
    }

    public class UpTranslation extends VerticalTranslation{
        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y -= movementStep;

        }
    }

    public class DownTranslation extends VerticalTranslation{
        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.y += movementStep;
        }
    }

    public class LeftTranslation extends HorizontalTranslation{
        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x -= movementStep;
        }
    }

    public class RightTranslation extends HorizontalTranslation{
        @Override
        protected void frameAction(GraphicBlock blockToAnimate){
            blockToAnimate.x += movementStep;
        }
    }

    public class Rotation extends BlockAnimation{
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
    private final LinkedList<GraphicBlock> blocksToDraw;

    protected final UpTranslation upAnimation;
    protected final DownTranslation downAnimation;
    protected final LeftTranslation leftAnimation;
    protected final RightTranslation rightAnimation;
    //protected final Rotation ccwRotateAnimation;
    //protected final Rotation cwRotateAnimation;

    public TetrisGameView(Context context) {
        super(context);

        this.numRows = 10;
        this.numCols = 10;
        blockImgWidth = getWidth() / numCols;
        blockImgHeight = getHeight() / numRows;
        //initialize scaled block images
        updateScaledBitmaps();
        blocksToDraw = new LinkedList<GraphicBlock>();

        upAnimation = new UpTranslation();
        downAnimation = new DownTranslation();
        rightAnimation = new RightTranslation();
        leftAnimation = new LeftTranslation();
        //ccwRotateAnimation = new Rotation();
        //cwRotateAnimation = new Rotation();

    }

    private void updateScaledBitmaps(){
        int srcHeight = 500;
        int srcWidth = 500;
        int scaledHeight = blockImgHeight;
        int scaledWidth = blockImgWidth;
        int inSampleSize = 1;
        final int halfHeight = srcHeight / 2;
        final int halfWidth = srcWidth / 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        /*calculate new sample size, from official android docs
        https://developer.android.com/topic/performance/graphics/load-bitmap.html
         */

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= scaledHeight
                && (halfWidth / inSampleSize) >= scaledWidth) {
            inSampleSize *= 2;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        //create the scaled bitmaps from our block image resources
        greenBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.greenBlockImg, options);
        redBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.redBlockImg, options);
        blueBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.blueBlockImg, options);
        yellowBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.yellowBlockImg, options);
        goldBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.goldBlockImg, options);
        purpleBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.purpleBlockImg, options);
        cyanBlockImg = BitmapFactory.decodeResource(getResources(), R.drawable.cyanBlockImg, options);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateScaledBitmaps();
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
            }
            //update the screen
            updateScreen();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE); /*for testing, just draw a boring white background
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