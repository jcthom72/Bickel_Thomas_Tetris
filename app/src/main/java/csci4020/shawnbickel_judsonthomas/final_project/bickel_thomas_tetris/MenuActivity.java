package csci4020.shawnbickel_judsonthomas.final_project.bickel_thomas_tetris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView red;
    private ImageView L;
    private ImageView T;
    private ImageView Z;
    private Button play;
    private Button round;
    private Button rules;
    private Button attribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        red = (ImageView) findViewById(R.id.redImageview);
        L = (ImageView) findViewById(R.id.yellowImageview);
        T = (ImageView) findViewById(R.id.greenImageview);
        Z = (ImageView) findViewById(R.id.orangeImageview);
        play = (Button) findViewById(R.id.play_button);
        round = (Button) findViewById(R.id.round_button);
        rules = (Button) findViewById(R.id.rule_button);
        attribution = (Button) findViewById(R.id.attribution_button);

        Button[] buttons = {play, round, rules, attribution};

        for (Button button : buttons){
            button.setOnClickListener(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        TranslateAnimation translateAnimation =
                new TranslateAnimation(Animation.RESTART, 0,
                        Animation.RESTART, 1,
                        Animation.RESTART, 0, Animation.RESTART, 15);
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        AnimationSet setAnimation = new AnimationSet(true);
        setAnimation.addAnimation(translateAnimation);


        red.startAnimation(translateAnimation);
        L.startAnimation(translateAnimation);
        T.startAnimation(translateAnimation);
        Z.startAnimation(translateAnimation);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.play_button){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.attribution_button){
            String message = "<html>" +
                    "<h1>ColorPicker Library</h1>" +
                    "<h3>Author: Petrov Kristiyan</h3>" +
                    "<p><a href='https://github.com/kristiyanP/colorpicker'>Source Website</a></p>" +
                    "<p>Apache License 2.0: <a href='https://github.com/kristiyanP/colorpicker" +
                    "/blob/master/LICENSE'>License Page</a></p><br>" +
                    "<p>The MIT License</p>" +
                    "<p>Copyright (c) 2016 Petrov Kristiyan</p>" +
                    "<p>Permission is hereby granted, free of charge, to any person obtaining a " +
                    "copy of this software and associated documentation files (the \"Software\"), " +
                    "to deal in the Software without restriction, including without limitation " +
                    "the rights to use, copy, modify, merge, publish, distribute, sublicense, " +
                    "and/or sell copies of the Software, and to permit persons to whom the " +
                    "Software is furnished to do so, subject to the following conditions:</p>" +
                    "<p>The above copyright notice and this permission notice shall be included " +
                    "in all copies or substantial portions of the Software.</p>" +
                    "<p>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS " +
                    "OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                    "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL " +
                    "THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER " +
                    "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING " +
                    "FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER " +
                    "DEALINGS IN THE SOFTWARE.</p><br>" +
                    "<h1>Open Source Images</h1>" +
                    "<p><a href='https://openclipart.org/detail/26063/brush'>Brush Image<br>" +
                    "<a href='https://openclipart.org/detail/260650/artgallery15'>Gallery Image<br>" +
                    "<a href='https://openclipart.org/detail/199463/primary-eraser'>Eraser Image<br>" +
                    "<a href='https://commons.wikimedia.org/wiki/File:Draw-1-black-line.svg'>Straight Line Image<br>" +
                    "<a href='https://openclipart.org/detail/263195/thin-rectangle'>Rectangle Image<br>" +
                    "<a href='https://openclipart.org/detail/82549/blue-circle'>Circle Image<br>" +
                    "<a href='https://openclipart.org/detail/38899/new-file'>New File Image<br>" +
                    "<a href='https://openclipart.org/detail/211810/colorwheel'>Colorwheel (background) Image<br>" +
                    "<a href='https://openclipart.org/detail/212220/rodentiaicons-editundoltr'>Redo Image<br>" +
                    "<a href='https://openclipart.org/detail/212221/rodentiaicons-editundo'>Undo Image<br>" +
                    "<a href='https://openclipart.org/detail/191516/rainbow-line-art'>Colorwheel (line color) image<br>" +
                    "<a href='https://openclipart.org/detail/17432/decorative-curves'>Line Width Image<br>" +
                    "<a href='https://openclipart.org/detail/227662/colorful-abstract-background-3'>Main Menu Image<br></p> " +
                    "<footer><p>App Developed by Judson Thomas and Shawn Bickel</p></footer>" +
                    "</html>";
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(Html.fromHtml(message));
            builder.setPositiveButton("Ok", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            // must be done after the call to show();
            // allows anchor tags to work
            TextView tv = (TextView) dialog.findViewById(android.R.id.message);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
