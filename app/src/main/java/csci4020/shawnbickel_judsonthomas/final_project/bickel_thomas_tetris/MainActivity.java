package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private class MainGameThread extends AsyncTask<Void, Void, Void>{
        @Override
        public void run() {
            while(tetrisGameDriver.nextTetromino()) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateScore();
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                while (tetrisGameDriver.move(TetrisGameEngine.Direction.DOWN)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }


            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            hasSpawned = tetrisGameDriver.nextTetromino();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(hasSpawned){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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

        //FOR TESTING: using score label text as a left button
        TextView leftButton = (TextView) findViewById(R.id.textView9);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.LEFT);
            }
        });

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
        //FOR TESTING: using score number text as a down button
        gameScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.DOWN);
            }
        });

        //FOR TESTING: using play button as a right button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.RIGHT);
            }
        });

        //FOR TESTING: using pause button as an up button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainGameThread gameThread = new MainGameThread();
                gameThread.start();
            }
        });


        tetrisGameView.initialize(gridRows, gridColomns);


/*
        tetrisGameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int  y = (int) motionEvent.getY();
                int x = (int) motionEvent.getX();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (x >= (tetrisGameView.getWidth() / 2)) {
                        tetrisGameDriver.rotate(TetrisGameEngine.Rotation.CW_90);
                    } else if (x < (tetrisGameView.getWidth() / 2)) {
                        tetrisGameDriver.rotate(TetrisGameEngine.Rotation.CCW_90);
                    }
                }
                return true;
            }

        });
*/

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


}
