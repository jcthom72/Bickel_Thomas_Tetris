package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final ImageView red = (ImageView) findViewById(R.id.redImageview);
        final ImageView L = (ImageView) findViewById(R.id.yellowImageview);
        final ImageView T = (ImageView) findViewById(R.id.greenImageview);
        final ImageView Z = (ImageView) findViewById(R.id.orangeImageview);


        final TranslateAnimation translateAnimation =
                new TranslateAnimation(Animation.RESTART, 0,
                        Animation.RESTART, 1,
                        Animation.RESTART, 0, Animation.RESTART, 15);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        final AnimationSet setAnimation = new AnimationSet(true);
        setAnimation.addAnimation(translateAnimation);

        red.startAnimation(translateAnimation);
        L.startAnimation(translateAnimation);
        T.startAnimation(translateAnimation);
        Z.startAnimation(translateAnimation);

    }
}
