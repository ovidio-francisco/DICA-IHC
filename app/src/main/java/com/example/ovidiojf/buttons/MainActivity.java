package com.example.ovidiojf.buttons;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import command.Command;
import command.Encoder;
import command.Encoder.Stage;
import command.Touch;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> touches = new ArrayList<>();

    private Button bt;
    private ImageButton btCima, btEsq, btDir, btBaixo;
    private EditText etTexto;

    LinearLayout layoutCommands, layoutNexts;

    private void addFeedbackTouch(int t) {


        int drawable = 0;
        switch (t) {
            case Touch.UP   : drawable = R.mipmap.ic_up;     break;
            case Touch.DOWN : drawable = R.mipmap.ic_down;   break;
            case Touch.LEFT : drawable = R.mipmap.ic_left2;  break;
            case Touch.RIGHT: drawable = R.mipmap.ic_right;  break;
        }

        LinearLayout layoutCommands = (LinearLayout)findViewById(R.id.layoutTouches);
        ImageView touch = new ImageView(this);
        touch.setImageResource(drawable);
        layoutCommands.addView(touch);
    }

    private void cleanFeedbackTouches() {
        layoutCommands.removeAllViews();
    }

    private void addNexts(Command com) {

        ArrayList<Command> nextsDown  = Encoder.nextCommands(com, Touch.DOWN);
        ArrayList<Command> nextsUp    = Encoder.nextCommands(com, Touch.UP);
        ArrayList<Command> nextsLeft  = Encoder.nextCommands(com, Touch.LEFT);
        ArrayList<Command> nextsRight = Encoder.nextCommands(com, Touch.RIGHT);

        addNext(nextsRight, Touch.RIGHT);
        addNext(nextsLeft , Touch.LEFT);
        addNext(nextsDown , Touch.DOWN);
        addNext(nextsUp   , Touch.UP);
    }

    private void addNext(ArrayList<Command> nexts, int first) {

        int w = layoutNexts.getWidth();

        if(w==0) {
            Display display = getWindowManager().getDefaultDisplay();
            Point p = new Point();
            display.getSize(p);
            w = p.x;
        }

        LinearLayout imgNext = new LinearLayout(this);
        imgNext.setOrientation(LinearLayout.VERTICAL);
        imgNext.setMinimumWidth(w/4);
        imgNext.setGravity(Gravity.CENTER_HORIZONTAL);

        if(!true) {
            int drawable = 0;
            switch (first) {
                case Touch.UP   : drawable = R.mipmap.ic_up;     break;
                case Touch.DOWN : drawable = R.mipmap.ic_down;   break;
                case Touch.LEFT : drawable = R.mipmap.ic_left2;  break;
                case Touch.RIGHT: drawable = R.mipmap.ic_right;  break;
            }

            ImageView touch = new ImageView(this);
            touch.setImageResource(drawable);
            imgNext.addView(touch);
        }

        for(Command c : nexts) {
            TextView text = new TextView(this);
            text.setText(c.toString(touches.size()));
            text.setTypeface(null, Typeface.BOLD);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            text.setTypeface(Typeface.createFromAsset(getAssets(), "arial.ttf"));
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            imgNext.addView(text);
        }

        layoutNexts.addView(imgNext);
    }

    private void cleanNexts() {
        layoutNexts.removeAllViews();
    }

    private void showAllCommands() {
        ArrayList<Command> nextsDown  = Encoder.getAllCommands(Touch.DOWN);
        ArrayList<Command> nextsUp    = Encoder.getAllCommands(Touch.UP);
        ArrayList<Command> nextsLeft  = Encoder.getAllCommands(Touch.LEFT);
        ArrayList<Command> nextsRight = Encoder.getAllCommands(Touch.RIGHT);

        addNext(nextsRight, Touch.RIGHT);
        addNext(nextsLeft , Touch.LEFT);
        addNext(nextsDown , Touch.DOWN);
        addNext(nextsUp   , Touch.UP);
    }

    private void addTouch(int t) {

        touches.add(t);
        addFeedbackTouch(t);

        Command command = new Command(touches);

        Stage stage = Encoder.getStage(command);

        if (stage == Stage.DONE) {
            String s = Encoder.find(command);

            if(s != null) {
                etTexto.append(s);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cleanFeedbackTouches();

                    }
                },500);

                touches.clear();
                cleanNexts();
                showAllCommands();
            }
        }

        if (stage == Stage.INCOMPLET) {
            cleanNexts();
            addNexts(command);
        }

        if (stage == Stage.WRONG) {
            touches.clear();
            cleanFeedbackTouches();
            cleanNexts();
            showAllCommands();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("DICA - ");

        btCima  = (ImageButton) findViewById(R.id.btCima);
        btEsq   = (ImageButton) findViewById(R.id.btEsquerda);
        btDir   = (ImageButton) findViewById(R.id.btDireita);
        btBaixo = (ImageButton) findViewById(R.id.btBaixo);

        etTexto = (EditText)findViewById(R.id.etTexto);

        layoutCommands = (LinearLayout)findViewById(R.id.layoutTouches);
        layoutNexts    = (LinearLayout)findViewById(R.id.layoutNexts);

        btCima .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTouch(Touch.UP);
            }
        });

        btBaixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTouch(Touch.DOWN);
            }
        });

        btEsq  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTouch(Touch.LEFT);
            }
        });

        btDir  .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTouch(Touch.RIGHT);
            }
        });

        etTexto.setText("");

        showAllCommands();
    }
}
