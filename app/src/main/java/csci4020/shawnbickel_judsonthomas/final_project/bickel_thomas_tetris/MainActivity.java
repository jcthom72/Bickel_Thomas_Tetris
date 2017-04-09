package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private TetrisGameEngine tetrisGameEngine;
    private ImageView playButton;
    private ImageView pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tetrisGameEngine = (TetrisGameEngine) findViewById(R.id.tetrisLayout);
        playButton = (ImageView) findViewById(R.id.play_button);
        pauseButton = (ImageView) findViewById(R.id.pause_button);
    }
}
