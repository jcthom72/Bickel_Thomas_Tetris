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
    private boolean hasSpawned = true;

    private class MainGameThread extends AsyncTask<Void, Void, Void>{

/*
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
        */

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {
            hasSpawned = tetrisGameDriver.nextTetromino();
            updateScore();
        }

        @Override
        protected Void doInBackground(Void... voids) {
                while(tetrisGameDriver.move(TetrisGameEngine.Direction.DOWN)){
                    try{
                        Thread.sleep(1000);
                    }catch(NullPointerException e){
                        newGame();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if ((hasSpawned = tetrisGameDriver.nextTetromino())){
                                newTetromino();
                            }

                        }
                    });

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
                MainGameThread gameThread = new MainGameThread();
                gameThread.execute();
            }
        });

        //FOR TESTING: using pause button as an up button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainGameThread gameThread = new MainGameThread();
                gameThread.execute();
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

    public void newGame(){
        final View dialogView = (LayoutInflater.from(this)).inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        builder.setTitle("Choose to play a new game or quit");

        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                scoreReset();
                hasSpawned = tetrisGameDriver.nextTetromino();
                updateScore();
            }
        });

        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        builder.show();
    }
    public void newTetromino(){
        MainGameThread gameThread = new MainGameThread();
        gameThread.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(getApplicationContext(), "Press the Play button to start the game",
                Toast.LENGTH_LONG);
        toast.show();
    }
}


