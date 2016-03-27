package com.andrewparkerdev.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ClassicSnake extends AppCompatActivity {

    /**
     * Add relative layout
     * Track if initialised  or not
     */
    private RelativeLayout classicSnakeLayout;
    private Boolean isInitialised;

    /**
     * The code that is run when the window is first launched.
     * Sets layout to classic snake layout which has up, down, left,
     * and right arrows, which are game controllers.
     * Sets the Layout to not touchable and full screen.
     * Hides the action bar.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Decide if want music straight away.
        musicOnOff();
        // Create relative layout
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        // set background image
        classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
        // set padding relative (warns: minSdk 17)
        classicSnakeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING);
        isInitialised = false;

        buttonsDirectionsInit();
    }

    /**
     * The ability to play MP3 sound and
     * the ability to be able to switch the sound off.
     */
    private boolean playMusic;
    private MediaPlayer musicPlayer;


    private void musicOnOff() {
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("SnakePreferences", Context.MODE_PRIVATE);
        /** First time the code is run, music will be on by default.
         * Whenever the code is run additionally, music on or off will be
         * defined by user preference stored in SharedPreferences.
         */
        playMusic = preferences.getBoolean("PlayMusic", true);
        musicPlayer = MediaPlayer.create(ClassicSnake.this, R.raw.music);
        if (playMusic) {
            // loops music
            musicPlayer.setLooping(true);
            musicPlayer.start();
        } else {
            musicPlayer.stop();
        }
    }


    private GestureDetector mGestureDetector;

    /**
     * Override onTouchEvent so it uses my mGestureDetector
     *
     * @param event A touch event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isPaused;

    @Override
    protected void onPause() {
        super.onPause();
        // boolean for use elsewhere in code
        isPaused = true;
        // releases any resources used by the Media Player.
        musicPlayer.release();
    }

    // Check the direction being moved in.
    private boolean isGoingLeft = false;
    private boolean isGoingRight = false;
    private boolean isGoingUp = false;
    private boolean isGoingDown = false;

    // Game logic.
    private void onSwipeRight() {
        if (isGoingRight == false && isGoingLeft == false) {
            isGoingRight = true;
            isGoingLeft = false;
            isGoingDown = false;
            isGoingUp = false;
        }
    }

    private void onSwipeLeft() {
        if (isGoingRight == false && isGoingLeft == false) {
            isGoingRight = false;
            isGoingLeft = true;
            isGoingDown = false;
            isGoingUp = false;
        }
    }

    private void onSwipeDown() {
        if (isGoingUp == false && isGoingDown == false) {
            isGoingRight = false;
            isGoingLeft = false;
            isGoingDown = true;
            isGoingUp = false;
        }
    }

    private void onSwipeUp() {
        if (isGoingUp == false && isGoingDown == false) {
            isGoingRight = false;
            isGoingLeft = false;
            isGoingDown = false;
            isGoingUp = true;
        }
    }

    // Check which direction has been clicked on screen.
    private boolean clickRight;
    private boolean clickLeft;
    private boolean clickDown;
    private boolean clickUp;

    private void clickRight() {
        if (clickRight == false && clickLeft == false) {
            clickRight = true;
            clickLeft = false;
            clickDown = false;
            clickUp = false;
        }
    }

    private void clickLeft() {
        if (clickRight == false && clickLeft == false) {
            clickRight = false;
            clickLeft = true;
            clickDown = false;
            clickUp = false;
        }
    }

    private void clickUp() {
        if (clickDown == false && clickUp == false) {
            clickRight = false;
            clickLeft = false;
            clickDown = false;
            clickUp = true;
        }
    }

    private void clickDown() {
        if (clickDown == false && clickUp == false) {
            clickRight = false;
            clickLeft = false;
            clickDown = true;
            clickUp = false;
        }
    }


    private ImageView buttonRight, buttonLeft, buttonDown, buttonUp;

    private void buttonsDirectionsInit() {
        buttonRight = (ImageView) findViewById(R.id.button_right);
        buttonLeft = (ImageView) findViewById(R.id.button_left);
        buttonUp = (ImageView) findViewById(R.id.button_up);
        buttonDown = (ImageView) findViewById(R.id.button_down);

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRight();
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeft();
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickUp();
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDown();
            }
        });

        /** check which controller options a user picked.
         *  The user define an option to play by swipe or the
         *  control arrows.
         *
         *  Sets useButtons to true first time the code is run. Additional runs will
         *  use the value stored in SharedPreferences under UseButtonControls which can
         *  be changed by the user in the settings.
         *
         *  If buttons are not being used they are invisible.
         */
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("SnakePreferences", Context.MODE_PRIVATE);
        useButtons = preferences.getBoolean("UseButtonControls", true);
        // if using buttons make them visible.
        if (useButtons) {
            buttonRight.setVisibility(View.VISIBLE);
            buttonLeft.setVisibility(View.VISIBLE);
            buttonDown.setVisibility(View.VISIBLE);
            buttonUp.setVisibility(View.VISIBLE);
        } else {
            buttonRight.setVisibility(View.INVISIBLE);
            buttonLeft.setVisibility(View.INVISIBLE);
            buttonDown.setVisibility(View.INVISIBLE);
            buttonUp.setVisibility(View.INVISIBLE);
        }
    }

    private boolean useButtons;

    /**
     * Shake the layout using animation.
     */
    private void Shake() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setDuration(GameSettings.SHAKE_DURATION);
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
        classicSnakeLayout.startAnimation(shake);
    }

    private int playerScore;

    /**
     * Fade the layout in and out.
     */
    private void FadeAnim() {
        if (playerScore % GameSettings.POINTS_ANIMATION == 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
            classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake_change);
            classicSnakeLayout.startAnimation(fadeIn);

            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils
                            .loadAnimation(ClassicSnake.this, R.anim.fade_out);
                    classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
                    classicSnakeLayout.startAnimation(fadeOut);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    private boolean gameOver = false;
    /** When to game ends save user preferences and their score.
     *
     * Then pass the score to the ClassicScore activity and show
     * it.
     */
    private void collide() {
        gameOver = true;
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("SnakePreferences", Context.MODE_PRIVATE);

        // To save preferences, use Editor.
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Score", playerScore);
        editor.commit();

        // Move to the classic score activity
        Intent intentScore = new Intent(this, ClassicScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);

    }



}















