package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import java.util.HashSet;
import java.util.Iterator;

/*backend game engine class for tetris game - judson thomas*/

public class TetrisGameEngine {
    public enum TetrominoType{ISHAPE, OSHAPE, LSHAPE, JSHAPE, SSHAPE, ZSHAPE, TSHAPE}
    public enum BlockColor{GREEN, YELLOW, RED, BLUE, PURPLE, GOLD, CYAN}
    public enum Direction{UP, DOWN, LEFT, RIGHT}
    public enum Rotation{CCW_90, CW_90}

    private interface Nudgeable{
        boolean nudge(Direction direction);
    }

    private interface Rotatable{
        boolean rotate(Rotation rotation);
    }

    /*class used to represent positions in the tetris grid;
    most often these positions represent absolute (x,y) coordinates into the grid; however,
    in some cases the positions are treated as vectors <x,y>. Which type of position is
    represented will be clear from the context of its usage.*/
    public class Position{
        private int x;
        private int y;

        private Position(int x, int y){
            this.x = x;
            this.y = y;
        }

        private Position left(){return new Position(x-1, y);}
        private Position right(){return new Position(x+1, y);}
        private Position above(){return new Position(x, y-1);}
        private Position below(){return new Position(x, y+1);}

        private Position inDirection(Direction direction){
            switch(direction){
                case UP:
                    return above();
                case DOWN:
                    return below();
                case LEFT:
                    return left();
                case RIGHT:
                    return right();
                default: //invalid direction;
                    return null;
            }
        }

        /*calculates a vector from "this" to "target" which represents the displacement (relative position)
        from this position to the target position; where relative_position = target_position - this_position;
        i.e. this_position + relative_position = target_position;
        */
        private Position getRelPosVector(Position target){
            return new Position(target.x - x, - (target.y - y)); /*TRICKY: must negate the y difference
            calculation because of the way our grid works (lower y values indicate higher up block positions)*/
        }

        private Position rotateVector(Rotation rotation){
			/*hidden linear algebra: treats the current position as a vector <this.x, this.y>,
			applying a linear transformation to it to perform a 90 degree rotation.
			The function returns the product of the transformation.

			The linear transformation is of the form:
			|0 -1|
			|1  0|*<this.x, this.y>   if rotation == CCW_90

			|0  1|
			|-1 0|*<this.x, this.y>   if rotation == CW_90;
			*/

            if(rotation == Rotation.CW_90){
                return new Position(this.y, -(this.x));
            }

            else if(rotation == Rotation.CCW_90){
                return new Position(-(this.y), this.x);
            }

            else{ //invalid rotation
                return null;
            }
        }

        /*adds a vector "vector" to the current position; calculating a new position vector.x units right
        and vector.y units up from the current position*/
        private Position addVector(Position vector){
            return new Position(x+vector.x, y-vector.y); /*TRICKY: here we have to subtract vector.y from y
            instead of adding it because our y values grow downward; i.e. lower values indicate higher block positions*/
        }

        /*Given a pixel scaling factor of "xPixelScalingFactor", returns the location of the leftmost
        * pixel that would correspond to this position's x value. Typically the "xPixelScalingFactor"
        * will be the block image width for the tetris game's view.*/
        public int getXPixelPos(int xPixelScalingFactor){
            return x * xPixelScalingFactor;
        }

        public int getYPixelPos(int yPixelScalingFactor){
            return y * yPixelScalingFactor;
        }
    };

    public class Block implements Nudgeable, Rotatable{
        private BlockColor color;
        private Position position;

        private Block(BlockColor color, Position position){
            this.color = color;
            this.position = position;
        }

        public boolean nudge(Direction direction){
            Position newPosition;

            newPosition = position.inDirection(direction);

            if(!blockMap.isFreeSpace(newPosition)){
                //new position is out of grid range or a collision is detected
                return false;
            }

            //remove the block from the blockmap
            blockMap.removeBlock_unsafe(this);

            //update the block's position
            position = newPosition;

            //add the block to the blockmap (now at the new position)
            blockMap.addBlock_unsafe(this);
            return true;
        }

        public boolean rotate(Rotation rotation){
            return true;
        }

        public final Position getPosition(){
            return position;
        }

        public BlockColor getColor(){return color;}
    }

    /*
        tetromino terminology
        anchor position: the position of the topmost, leftmost block in the
            tetromino. used as an anchor position when generating a tetromino piece
            to determine the location of all other blocks in the piece relative to it.
            Also, tetrominos are spawned into their gameboard specified by their anchor position.

        pivot: the block in the tetromino piece which serves as the axis point that all
            other blocks in the tetromino piece rotate around when performing a clockwise /
            counterclockwise rotation. The pivot point is different for each tetromino piece; will create
            a diagram to show which point is the pivot point for each tetromino. (For L/J pieces it's the
            block at the joint/elbow; for S/Z pieces it's the block at the top joint/elbow; for T pieces it's
            the block in the bottom middle; for O pieces there is no pivot (rotation of an O piece does nothing);
            for I pieces it's the second block from the left (when laid horizontal).

        nudge: a translation of 1 unit in an upward, downward, left, or right direction.
    */
    public abstract class Tetromino implements Nudgeable, Rotatable, Iterable<Block>{
        protected HashSet<Block> blocks;
        protected BlockColor color;
        protected Block pivot; //blocks rotate around pivot

        public boolean nudge(Direction direction){
            Position nudgePosition;

            //check to ensure the nudge can be performed
            for(Block block : blocks){
                nudgePosition = block.position.inDirection(direction);

                if(!blockMap.isFreeSpace(nudgePosition) && !blocks.contains(blockMap.blockAt(nudgePosition))){
					/*if the position to move to is occupied and the occupying block does not belong to our tetris piece
					either a collision has been detected or the current block is out of range (meaning the tetris piece
					is at the border); ***the latter works as long as we ensure our tetris piece's block set
					never contains the null element****/
                    return false;
                }
            }

            //path is unobstructed; perform the nudge:

            //remove the tetromino from the block map
            blockMap.removeTetromino_unsafe(this);

            //apply the translation by updating the position
            for(Block block : blocks){
                nudgePosition = block.position.inDirection(direction);
                block.position = nudgePosition;
            }

            //add the tetromino to the block map
            blockMap.addTetromino_unsafe(this);
            return true;
        }

        public boolean rotate(Rotation rotation){
            Position rotationPosition, vRelToPivot, vRotated;

            //COLLISION DETECTION: check to ensure the rotation can be performed
            for(Block block : blocks){
				/*get the relative position vector representing the displacement
				from pivot to block*/
                vRelToPivot = pivot.position.getRelPosVector(block.position);

                //rotate the displacement vector
                vRotated = vRelToPivot.rotateVector(rotation);

				/*calculate the block's new rotated position by adding the rotated displacement vector
				to the pivot block's position*/
                rotationPosition = pivot.position.addVector(vRotated);

                if(!blockMap.isInRange(rotationPosition) ||
                        (blockMap.isOccupied(rotationPosition) && !blocks.contains(blockMap.blockAt(rotationPosition)))){
					/*if the position to rotate to is out of range, or it is occupied by a block that is NOT
					* part of our tetromino piece*/
                    return false;
                }
            }

            //ROTATION: location to rotate to is unobstructed; perform rotation:

            //remove the tetromino piece from the map
            blockMap.removeTetromino_unsafe(this);

            //apply the transformation to all blocks
            for(Block block : blocks){
                vRelToPivot = pivot.position.getRelPosVector(block.position);
                vRotated = vRelToPivot.rotateVector(rotation);
                rotationPosition = pivot.position.addVector(vRotated);
                block.position = rotationPosition;
            }

            //add the tetromino piece back to the map
            blockMap.addTetromino_unsafe(this);
            return true;
        }

        public BlockColor getColor(){return color;}

        @Override
        public Iterator<Block> iterator() {
            return blocks.iterator();
        }

        protected Tetromino(BlockColor color){
            this.color = color;
            blocks = new HashSet<Block>(4); //create with an initial capacity of 4
        }

        public void clearBlocks(){
            blocks.clear();
        }
    }

    public class IShape extends Tetromino{
        private IShape(Position anchorPosition){
            super(BlockColor.GREEN);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.right());
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);
        }
    }

    public class OShape extends Tetromino{
        private OShape(Position anchorPosition){
            super(BlockColor.RED);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);

            block = new Block(color, block.position.below());
            blocks.add(block);

            block = new Block(color, block.position.left());
            blocks.add(block);

            pivot = null; //we just set pivot to null, OShape does not actually use a pivot
        }

        @Override
        public boolean rotate(Rotation rotation){
            return true;
        }
    }

    public class TShape extends Tetromino{
        private TShape(Position anchorPosition){
            super(BlockColor.YELLOW);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.right());
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.below());
            blocks.add(block);

            block = new Block(color, block.position.above().right());
            blocks.add(block);
        }
    }

    public class JShape extends Tetromino{
        private JShape(Position anchorPosition){
            super(BlockColor.BLUE);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.right());
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);

            block = new Block(color, block.position.below());
            blocks.add(block);
        }
    }

    public class LShape extends Tetromino{
        private LShape(Position anchorPosition){
            super(BlockColor.PURPLE);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.below());
            blocks.add(block);

            block = new Block(color, block.position.above().right());
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);
        }
    }

    public class SShape extends Tetromino{
        private SShape(Position anchorPosition){
            super(BlockColor.GOLD);
            Block block = new Block(color, anchorPosition);
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);

            block = new Block(color, block.position.left().below());
            blocks.add(block);

            block = new Block(color, block.position.left());
            blocks.add(block);
        }
    }

    public class ZShape extends Tetromino{
        private ZShape(Position anchorPosition){
            super(BlockColor.CYAN);
            Block block = new Block(color, anchorPosition);
            blocks.add(block);

            block = new Block(color, block.position.right());
            pivot = block;
            blocks.add(block);

            block = new Block(color, block.position.below());
            blocks.add(block);

            block = new Block(color, block.position.right());
            blocks.add(block);
        }
    }

    private class BlockMap{
        private Block[][] grid;
        private int numRows, numCols;

        private BlockMap(int numRows, int numCols){
            this.numRows = numRows;
            this.numCols = numCols;
            grid = new Block[numRows][numCols];
        }

		/*unsafe methods: provides the rest of the game engine
		an interface to the blockmap for DIRECTLY updating blocks and tetrominos
		within the blockMap. This interface is unsafe because it provides
		no additional error checking to ensure the integrity of the block map; e.g.
		a block could be added into a position where a block already exists, overwriting
		the previous block. However, this is necessary in some cases. Special care must
		be taken by the caller to ensure that the integrity of the blockMap will be maintained.*/

        private void removeBlock_unsafe(Position position){
            grid[position.y][position.x] = null;
        }

        private void removeBlock_unsafe(Block block){
            grid[block.position.y][block.position.x] = null;
        }

        private void removeTetromino_unsafe(Tetromino tetromino){
            for(Block piece : tetromino.blocks){
                removeBlock_unsafe(piece);
            }
        }

        private void addBlock_unsafe(Block block){
            grid[block.position.y][block.position.x] = block;
        }

        private void addTetromino_unsafe(Tetromino tetromino){
            for(Block piece : tetromino.blocks){
                addBlock_unsafe(piece);
            }
        }

        private Block blockAt(Position position){
            if(!isInRange(position)){
                return null;
            }

            return grid[position.y][position.x];
        }

        private boolean isInRange(Position position){
            if(position.x < 0 || position.x >= numCols ||
                    position.y < 0 || position.y >= numRows){
                return false;
            }
            return true;
        }

        private boolean isOccupied(Position position){
            return blockAt(position) != null;
        }

        /*returns true if the position is a legal position in our board
        (in range), and it is not occupied by a block; false otherwise.
        Note: this function's behavior is not equivalent to calling !isOccupied;
        !isOccupied returns true if a block is NOT at the given position; however,
        this could mean one of 2 things: either the position is a valid, empty space
        or the position is unreachable (out of range). !isOccupied does not guarantee
        that a block can be put in the specified position. isFreeSpace on the other hand does.*/
        private boolean isFreeSpace(Position position){
            return isInRange(position) && !isOccupied(position);
        }
    }

    //spawns a tetromino of type "type" at the top middle-most point of the grid (if possible)
    public Tetromino spawn(TetrominoType type){
        Tetromino tetromino;
        int midCol = blockMap.numCols / 2;
        Position anchorPosition = new Position(midCol, 0);

        switch(type){
            case TSHAPE:
                anchorPosition.x -= 1;
                tetromino = new TShape(anchorPosition);
                break;

            case JSHAPE:
                anchorPosition.x -= 1;
                tetromino = new JShape(anchorPosition);
                break;

            case ISHAPE:
                anchorPosition.x -= 1;
                tetromino = new IShape(anchorPosition);
                break;

            case LSHAPE:
                anchorPosition.x -= 1;
                tetromino = new LShape(anchorPosition);
                break;

            case ZSHAPE:
                anchorPosition.x -= 1;
                tetromino = new ZShape(anchorPosition);
                break;

            case OSHAPE:
                tetromino = new OShape(anchorPosition);
                break;

            case SSHAPE:
                anchorPosition.x -= 1;
                tetromino = new SShape(anchorPosition);
                break;

            default:
                return null; //invalid tetromino type
        }

        for(Block block : tetromino.blocks){
            if(!blockMap.isFreeSpace(block.position)){
                return null;
            }
        }

        blockMap.addTetromino_unsafe(tetromino);

        return tetromino;
    }

    private boolean removeRow(int rowToRemove){
        if(rowToRemove >= blockMap.numRows || rowToRemove < 0){
            return false;
        }

        for(int i = 0; i < blockMap.numCols; i++){
            if(blockMap.grid[rowToRemove][i] == null){
                return false;
            }
        }

        //row is filled
        for(int i = 0; i < blockMap.numCols; i++){
            blockMap.removeBlock_unsafe(blockMap.grid[rowToRemove][i]);
        }

        /*move down every piece above the removed row*/
        if(rowToRemove == 0){
            return true;
        }

        for(int i = rowToRemove - 1; i < blockMap.numRows; i++){
            for(int j = 0; j < blockMap.numCols; j++){
                blockMap.removeBlock_unsafe(blockMap.grid[i][j]);
                blockMap.grid[i][j].position = blockMap.grid[i][j].position.below();
            }
        }

        for(int i = rowToRemove - 1; i < blockMap.numRows; i++){
            for(int j = 0; j < blockMap.numCols; j++){
                blockMap.addBlock_unsafe(blockMap.grid[i][j]);
            }
        }

        return true;
    }

    private BlockMap blockMap;

    public TetrisGameEngine(int numRows, int numCols){
        blockMap = new BlockMap(numRows, numCols);
    }

    public int getNumRows(){return blockMap.numRows;}

    public int getNumCols(){return blockMap.numCols;}
}

