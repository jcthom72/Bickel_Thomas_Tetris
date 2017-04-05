package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AttributionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView splashScreen;
    private TextView M_Square;
    private TextView M_T;
    private TextView M_Z;
    private TextView M_L;
    private TextView M_Straight;
    private TextView ShawnBickel;
    private TextView JudsonThomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribution);
        splashScreen = (TextView) findViewById(R.id.SplashScreenTextView);
        M_Square = (TextView) findViewById(R.id.Square_Image);
        M_T = (TextView) findViewById(R.id.T_image);
        M_Z = (TextView) findViewById(R.id.Z_Image);
        M_L = (TextView) findViewById(R.id.L_image);
        M_Straight = (TextView) findViewById(R.id.StraightLine_Image);
        ShawnBickel = (TextView) findViewById(R.id.ShawnBickelRepo);
        JudsonThomas = (TextView) findViewById(R.id.JudsonThomasRepo);

        TextView[] imageLinks = {splashScreen, M_Square, M_T, M_Z, M_L, M_Straight, ShawnBickel, JudsonThomas};

        for (TextView properties : imageLinks){
            setLinkProperties(properties, properties.getText().toString());
        }
        for (TextView images : imageLinks){
            images.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.SplashScreenTextView){
            Uri uri = Uri.parse("https://openclipart.org/detail/86695/3d-tetris-blocks");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.Square_Image){
            Uri uri = Uri.parse("https://commons.wikimedia.org/wiki/File:Tetris_O.svg");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.T_image){
            Uri uri = Uri.parse("https://commons.wikimedia.org/wiki/File:Tetris_T.svg");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.Z_Image){
            Uri uri = Uri.parse("https://commons.wikimedia.org/wiki/File:Tetris_Z.svg");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.L_image){
            Uri uri = Uri.parse("https://commons.wikimedia.org/wiki/File:Tetris_J.svg");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.StraightLine_Image){
            Uri uri = Uri.parse("https://commons.wikimedia.org/wiki/File:Tetris_I.svg");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.ShawnBickelRepo){
            Uri uri = Uri.parse("https://github.com/sbickel3");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

        else if (view.getId() == R.id.JudsonThomasRepo){
            Uri uri = Uri.parse("https://github.com/jcthom72");
            Intent a = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(a);
        }

    }

    private void setLinkProperties (TextView textView, String text){
        textView.setPaintFlags(splashScreen.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.Alizarin));
    }
}
