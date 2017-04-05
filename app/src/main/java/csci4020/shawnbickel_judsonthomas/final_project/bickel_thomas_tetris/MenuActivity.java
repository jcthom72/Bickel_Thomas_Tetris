package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView Square;
    private ImageView L;
    private ImageView T;
    private ImageView Z;
    private ImageView I;
    private Button play;
    private Button round;
    private Button rules;
    private Button attribution;
    private TranslateAnimation translateAnimation;
    private TetrisGameEngine tetrisGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tetrisGameEngine = new TetrisGameEngine(this);
        Square = (ImageView) findViewById(R.id.Square_Tetromino);
        L = (ImageView) findViewById(R.id.L_Tetromino);
        T = (ImageView) findViewById(R.id.T_Tetromino);
        Z = (ImageView) findViewById(R.id.Z_Tetromino);
        I = (ImageView) findViewById(R.id.I_Tetromino);
        play = (Button) findViewById(R.id.play_button);
        round = (Button) findViewById(R.id.round_button);
        rules = (Button) findViewById(R.id.rule_button);
        attribution = (Button) findViewById(R.id.attribution_button);

        Button[] buttons = {play, round, rules, attribution};

        for (Button button : buttons){
            button.setOnClickListener(this);
        }

    }

    // image effects or translations are initialized in onResume()
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("OnResume", "animations started");

        translateAnimation =
                new TranslateAnimation(Animation.RESTART, 0,
                        Animation.RESTART, 1,
                        Animation.RESTART, 0, Animation.RESTART, 15);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        AnimationSet setAnimation = new AnimationSet(true);
        setAnimation.addAnimation(translateAnimation);

        // http://stackoverflow.com/questions/1634252/how-to-make-a-smooth-image-rotation-in-android
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Square.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                L.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                T.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                Z.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                I.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
            }
        };

        Square.startAnimation(setAnimation);
        Square.animate().rotationBy(360).withEndAction(runnable);
        I.startAnimation(setAnimation);
        I.animate().rotationBy(360).withEndAction(runnable);
        L.startAnimation(setAnimation);
        L.animate().rotationBy(360).withEndAction(runnable);
        T.startAnimation(setAnimation);
        T.animate().rotationBy(360).withEndAction(runnable);
        Z.startAnimation(setAnimation);
        Z.animate().rotationBy(360).withEndAction(runnable);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.play_button){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.round_button){
            final View dialogView = (LayoutInflater.from(this)).inflate(R.layout.dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setView(dialogView);

            builder.setTitle("Please enter round from 1 to 10");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id){
                    String gameLevel = ((EditText) dialogView.findViewById(R.id.userText)).getText().toString().trim();
                    int level = Integer.parseInt(gameLevel);
                    /*
                        if (level <= 10 || level >= 1){
                            tetrisGameEngine.setGameLevel(level);
                       }else{
                            tetrisGameEngine.setGameLevel(currentGameLevel);
                       }
                    */
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    //tetrisGameEngine.setGameLevel(currentGameLevel);
                }
            });

            builder.show();

        }

        else if (view.getId() == R.id.rule_button){
            Intent intent = new Intent(getApplicationContext(), Rules.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.attribution_button){
            Intent intent = new Intent(getApplicationContext(), AttributionActivity.class);
            startActivity(intent);
        }
    }

    // onPause clears the animations to reserve user resources
    @Override
    protected void onPause() {
        super.onPause();
        Square.clearAnimation();
        I.clearAnimation();
        L.clearAnimation();
        T.clearAnimation();
        Z.clearAnimation();
        Log.i("OnPause", "animations cleared");
    }
}
