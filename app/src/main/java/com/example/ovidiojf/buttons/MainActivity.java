package com.example.ovidiojf.buttons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    ArrayList<Integer> commands = new ArrayList<>();

    private Button bt;
    private ImageButton btCima, btEsq, btDir, btBaixo;
    private TextView tw, twTexto, twNexts;

    private void setText(String text) {
        tw.setText(tw.getText().toString().concat(text+" "));
    }

    private void addFeedbackTouch(int t) {


        String touchDesc = "???";
        switch (t) {
            case Touch.UP   : touchDesc = "^";  break;
            case Touch.DOWN : touchDesc = "V";  break;
            case Touch.LEFT : touchDesc = "<";  break;
            case Touch.RIGHT: touchDesc = ">";  break;
        }
        tw.append(touchDesc + " ");

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
        LinearLayout layoutCommands = (LinearLayout)findViewById(R.id.layoutTouches);

        layoutCommands.removeAllViews();
        tw.setText("");

    }

    private void addTouch(int t) {

        commands.add(t);
        addFeedbackTouch(t);

        Command command = new Command(commands);

        Stage stage = Encoder.getStage(command);

        if (stage == Stage.DONE) {
            String s = Encoder.find(command);

            if(s != null) {
                twTexto.append(s);
                commands.clear();
                cleanFeedbackTouches();
                twNexts.setText("");
            }
        }

        if (stage == Stage.INCOMPLET) {
            ArrayList<Command> nexts = Encoder.nextCommands(command);

            twNexts.setText("");

            if (nexts != null) {

                for(Command n : nexts) {
                    twNexts.append(String.format("%s ", n.toString()));
                }
//                System.out.println();
            }

        }

        if (stage == Stage.WRONG) {
            commands.clear();
            tw.append("Wrong! :(   ");
            cleanFeedbackTouches();
            twNexts.setText("");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCima  = (ImageButton) findViewById(R.id.btCima);
        btEsq   = (ImageButton) findViewById(R.id.btEsquerda);
        btDir   = (ImageButton) findViewById(R.id.btDireita);
        btBaixo = (ImageButton) findViewById(R.id.btBaixo);

        tw = (TextView)findViewById(R.id.textView);
        twTexto = (TextView)findViewById(R.id.twTexto);
        twNexts = (TextView)findViewById(R.id.twNexts);

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

        tw.setText("");
        twTexto.setText("");
        twNexts.setText("");





    }
}
