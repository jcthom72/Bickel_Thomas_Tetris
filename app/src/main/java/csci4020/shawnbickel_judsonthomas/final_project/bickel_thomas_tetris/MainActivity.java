package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private TetrisGameView tetrisGameView;
    private TetrisDriver tetrisGameDriver;
    private ImageView playButton;
    private ImageView pauseButton;
    private TextView gameScore;
    private int score;
    protected String HighScore = "HighScore.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button CW = (Button) findViewById(R.id.CW);
        Button CCW = (Button) findViewById(R.id.CCW);
        Button UP = (Button) findViewById(R.id.UP);
        gameScore = (TextView) findViewById(R.id.gameScore);
        playButton = (ImageView) findViewById(R.id.play_button);
        pauseButton = (ImageView) findViewById(R.id.pause_button);
        tetrisGameView = (TetrisGameView) findViewById(R.id.tetrisLayout);
        tetrisGameDriver = new TetrisDriver(new TetrisGameEngine(10, 10), tetrisGameView);


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

        UP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.UP);
            }
        });

        //FOR TESTING: using score label text as a left button
        TextView leftButton = (TextView) findViewById(R.id.textView9);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.LEFT);
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
                tetrisGameDriver.nextTetromino();
                updateScore();
                //tetrisGameDriver.move(TetrisGameEngine.Direction.UP);
            }
        });


        tetrisGameView.initialize(10, 10);
        //tetrisGameView.setOnTouchListener(new onTouchListener());
    }

    public void updateScore(){
        score++;
        String s = String.valueOf(score);
        gameScore.setText(s);
        saveHighScore(s);
    }

    // saves the player's score to a file
    public void saveHighScore(String score)  {
        try {
            FileOutputStream fos = openFileOutput(HighScore, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter (bw);
            pw.print(score);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't write to file", Toast.LENGTH_LONG).show();
        }
    }

    // returns a player's score from a file
    public String returnHighScore(){
        String score = "";

        try {
            FileInputStream fis = openFileInput(HighScore);
            Scanner s = new Scanner(fis);
            while(s.hasNext()){
                score = s.next();
            }
            s.close();
        } catch (FileNotFoundException e) {
            Log.i("ReadData", "no input file found");
        }
        return score;
    }

    // retrieves the player's score from a file
    private void retrieveScore(){
        // sets the player's score retrieved from file
        String sc = "";
        try {
           sc = returnHighScore();
        }catch (NullPointerException e){
            score = 0;
            gameScore.setText(Integer.toString(score));
        }


        try{
            int s = Integer.parseInt(sc);
            score = s;
            gameScore.setText(Integer.toString(s));
        }catch (NumberFormatException e){

        }
    }

    @Override
    protected void onResume() {
        super.onPause();
        retrieveScore();
    }
}
