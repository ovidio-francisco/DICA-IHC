package com.example.ovidiojf.buttons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import command.Command;
import command.Encoder;
import command.Encoder.Stage;
import command.Touch;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> touches = new ArrayList<>();

    private ImageButton btCima, btEsq, btDir, btBaixo;
    private EditText etTexto;

    private MediaPlayer soundAddLetter, soudWrong, soundTouch, soundBacspace;

    private TextToSpeech tts;

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
            imgNext.addView(getImageViewByTouch(first));
        }

        for(Command c : nexts) {
//            imgNext.addView(getTextViewByCommand(c));
            imgNext.addView(getCommandView2(c));
        }

        layoutNexts.addView(imgNext);
    }

    private LinearLayout getCommandView2(Command c) {
        LinearLayout result = new LinearLayout(this);
        result.setOrientation(LinearLayout.HORIZONTAL);
        result.setGravity(Gravity.BOTTOM);

        result.addView(getTextViewByCommand(c));

        switch (c.getControl()) {
            case BACKSPACE:
            case SPACE    :
            case RETURN   :
            case SPEACH   :
            case EXIT     :     result.addView(getImageByControl(c.getControl())); break;
        }

        return result;
    }

    private LinearLayout getCommandView(Command c) {
        LinearLayout result = new LinearLayout(this);
        result.setOrientation(LinearLayout.HORIZONTAL);
//        result.addView(getTextViewByCommand(c));

        for(int t: c) {
            result.addView(getImageViewByTouch2(t));
        }

        TextView label = new TextView(this);
        label.setText(c.getTarget());

        result.addView(label);

        return result;
    }



    private void cleanNexts() {
        layoutNexts.removeAllViews();
    }

    private TextView getTextViewByCommand (Command c) {
        TextView result = new TextView(this);
        result.setText(c.toString(touches.size()));
        result.setTypeface(null, Typeface.BOLD);
        result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        result.setTypeface(Typeface.MONOSPACE);
        result.setGravity(Gravity.CENTER_HORIZONTAL);

        return result;
    }

    private int getDrawableByTouch(int t) {
        int drawable = 0;
        switch (t) {
            case Touch.UP   : drawable = R.mipmap.ic_up;     break;
            case Touch.DOWN : drawable = R.mipmap.ic_down;   break;
            case Touch.LEFT : drawable = R.mipmap.ic_left2;  break;
            case Touch.RIGHT: drawable = R.mipmap.ic_right;  break;
        }

        return drawable;
    }

    private ImageView getImageViewByTouch(int t) {
        ImageView result = new ImageView(this);
        int drawable = getDrawableByTouch(t);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), drawable);
        Bitmap scal = Bitmap.createScaledBitmap(bMap, 40, 40, true);
        result.setImageBitmap(scal);

//        result.setRotation((float) 90);

        return result;
    }

    private ImageView getImageViewByTouch2(int t) {
        ImageView result = new ImageView(this);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_left_arrow);
        Bitmap scal = Bitmap.createScaledBitmap(bMap, 40, 40, true);
        result.setImageBitmap(scal);

        result.setPadding(2,2,2,2);

        float rotate = 0;
        switch (t) {
            case Touch.DOWN : rotate = 180f; break;
            case Touch.LEFT : rotate = 270f; break;
            case Touch.UP   : rotate = 000f; break;
            case Touch.RIGHT: rotate = 090f; break;
        }

        result.setRotation(rotate);

        return result;
    }

    private ImageView getImageByControl(Encoder.Control control) {
        ImageView result = new ImageView(this);

        int drawable = 0;
        switch (control) {
            case BACKSPACE: drawable = R.drawable.ic_backspace; break;
            case RETURN   : drawable = R.drawable.ic_enter2;    break;
            case SPACE    : drawable = R.drawable.ic_space2;    break;
            case EXIT     : drawable = R.drawable.ic_exit;      break;
            case SPEACH   : drawable = R.drawable.ic_speach1;   break;
        }

        if ((control == Encoder.Control.EXIT || control == Encoder.Control.SPEACH) && true) {
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), drawable);
            Bitmap scal = Bitmap.createScaledBitmap(bMap, 39, 37, true);
            result.setImageBitmap(scal);
        }
        else {
            result.setImageResource(drawable);
        }

        if (control == Encoder.Control.BACKSPACE || control == Encoder.Control.RETURN) result.setPadding(2,2,2,5);

        return result;
    }

    private void vibre() {
//        Vibrator v = (Vibrator)this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//        v.vibrate(500);
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
            Command c = Encoder.findCommand(command);



            if(c != null) {

                switch (c.getControl()) {
                    case BACKSPACE: soundBacspace.start(); break;
                    case EXIT: break;
                    default: soundAddLetter.start(); break;
                }

                switch (c.getControl()) {
                    case BACKSPACE: etTexto.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)); break;
                    case EXIT     : finish(); break;
                    case SPEACH   : speak(etTexto.getText().toString());
                    default       : etTexto.append(c.getTarget());
                }

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
            soundTouch.start();

            cleanNexts();
            addNexts(command);
        }

        if (stage == Stage.WRONG) {
            soudWrong.start();
            vibre();
            touches.clear();
            cleanFeedbackTouches();
            cleanNexts();
            showAllCommands();
        }

    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("DICA - Dispositivo para Comunicação Alternativa");

//        TextView custonTitle = new TextView(this);
//        RelativeLayout.LayoutParams custonTitleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        custonTitle.setLayoutParams(custonTitleParams);
//        custonTitle.setText("DICA - Dispositivo para Comunicação Alternativa");
//        custonTitle.setTextSize(30);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(custonTitle);

        btCima  = (ImageButton) findViewById(R.id.btCima);
        btEsq   = (ImageButton) findViewById(R.id.btEsquerda);
        btDir   = (ImageButton) findViewById(R.id.btDireita);
        btBaixo = (ImageButton) findViewById(R.id.btBaixo);

        btCima .setSoundEffectsEnabled(false);
        btEsq  .setSoundEffectsEnabled(false);
        btDir  .setSoundEffectsEnabled(false);
        btBaixo.setSoundEffectsEnabled(false);

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

        soundAddLetter = MediaPlayer.create(this, R.raw.click8);
        soudWrong      = MediaPlayer.create(this, R.raw.incorrectfunction);
        soundTouch     = MediaPlayer.create(this, R.raw.click7);
        soundBacspace  = MediaPlayer.create(this, R.raw.drop1);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("pt_BR"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    }
           //         speak("Hello girl");

                } else {
                }
            }
        });
//, "com.googlecode.eyesfree.espeak"

    }
}
