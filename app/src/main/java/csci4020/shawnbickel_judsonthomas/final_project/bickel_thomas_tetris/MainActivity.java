package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TetrisGameView tetrisGameView;
    private TetrisDriver tetrisGameDriver;
    private ImageView playButton;
    private ImageView pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button CW = (Button) findViewById(R.id.CW);
        Button CCW = (Button) findViewById(R.id.CCW);
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

        //FOR TESTING: using score label text as a left button
        TextView leftButton = (TextView) findViewById(R.id.textView9);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tetrisGameDriver.move(TetrisGameEngine.Direction.LEFT);
            }
        });
        //FOR TESTING: using score number text as a down button
        TextView downButton = (TextView) findViewById(R.id.gameScore);
        downButton.setOnClickListener(new View.OnClickListener() {
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
                //tetrisGameDriver.move(TetrisGameEngine.Direction.UP);
            }
        });

        //somehow next tetromino gets called before onsizechanged for our view????????????
        //tetrisGameDriver.nextTetromino();
    }
}
