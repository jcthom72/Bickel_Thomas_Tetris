package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Thread creates the splash screen
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(6000); // splash screen displays for 6 seconds; then the menu is displayed to the user
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
