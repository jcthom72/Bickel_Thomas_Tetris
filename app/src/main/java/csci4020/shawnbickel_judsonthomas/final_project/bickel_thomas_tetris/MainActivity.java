package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TetrisGameView tetrisGameView;
    private TetrisDriver tetrisGameDriver;
    private TetrisGameEngine.Tetromino currentTetromino;
    private ImageView playButton;
    private ImageView pauseButton;
    private TextView gameScore;
    private Button CW;
    private Button CCW;
    private int score;
    private int gridRows = 10;
    private int gridColomns = 10;
    private float startXTouch = 0.0f;
    MainGameThread gameThread;

    private class MainGameThread extends Thread {
        @Override
        public void run() {
            /*game runs while a new tetromino can be spawned; i.e. in this version
            * the game does not necessarily end if a tetromino is placed in the top row, but rather
            * if a tetromino is placed in a position that blocks the spawn location (i.e. in the middle
            * of the top row)*/
            while (tetrisGameDriver.nextTetromino()) {
                //update the score
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateScore();
                    }
                });

                //sleep before moving the piece downward
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                /*while the piece can be moved down, i.e. has not landed, we move it down and then sleep for a given amount of time;
                if we attempt to move the piece down and a collision is detected, i.e. ERROR_COLLISION is returned, we know
                * that the piece has "landed"*/
                while (tetrisGameDriver.move(TetrisGameEngine.Direction.DOWN) != TetrisDriver.MoveStatus.ERROR_COLLISION) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                    }
                }

                //now that the piece has landed, update any potential rows that may need to be deleted
                tetrisGameDriver.updateRows();
            }
            gameThread = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CW = (Button) findViewById(R.id.CW);
        CCW = (Button) findViewById(R.id.CCW);
        gameScore = (TextView) findViewById(R.id.gameScore);
        playButton = (ImageView) findViewById(R.id.play_button);
        pauseButton = (ImageView) findViewById(R.id.pause_button);
        tetrisGameView = (TetrisGameView) findViewById(R.id.tetrisLayout);
        tetrisGameDriver = new TetrisDriver(new TetrisGameEngine(gridRows, gridColomns), tetrisGameView);

        CW.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                tetrisGameDriver.rotate(TetrisGameEngine.Rotation.CW_90);
            }
        });

        CCW.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                tetrisGameDriver.rotate(TetrisGameEngine.Rotation.CCW_90);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameThread == null){
                    gameThread = new MainGameThread();
                    gameThread.start();
                }
            }
        });

        tetrisGameView.initialize(gridRows, gridColomns);

        tetrisGameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    startXTouch = motionEvent.getX();
                }

               else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    float endXTouch = motionEvent.getX();
                    float xDelta = endXTouch - startXTouch;

                    if (xDelta > 0) {
                        tetrisGameDriver.move(TetrisGameEngine.Direction.RIGHT);
                    } else if (xDelta < 0) {
                        tetrisGameDriver.move(TetrisGameEngine.Direction.LEFT);
                    }
                }

                else{ /*motion event not handled*/
                    return false;
                }

                return true;
            }

        });

    }

    public void updateScore(){
        score++;
        String s = String.valueOf(score);
        gameScore.setText(s);
    }

    public void scoreReset(){
        score = 0;
    }

    public void newGame() {
        final View dialogView = (LayoutInflater.from(this)).inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        builder.setTitle("Choose to play a new game or quit");

        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                scoreReset();
                updateScore();
            }
        });

        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(getApplicationContext(), "Press the Play button to start the game",
                Toast.LENGTH_LONG);
        toast.show();
    }
}


