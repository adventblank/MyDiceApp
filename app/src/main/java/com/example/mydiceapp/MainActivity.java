package com.example.mydiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int numberOfDice;
    Button btnSubractDice;
    Button btnAddDice;
    ImageView diceImage1;
    ImageView diceImage2;
    ImageView diceImage3;
    RelativeLayout dice2_layout;
    RelativeLayout dice3_layout;
    TextView txtView_Dice1;
    TextView txtView_Dice2;
    TextView txtView_Dice3;

    final int [] d6Images = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    MediaPlayer mp;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceImage1 = findViewById(R.id.imgDice1);
        diceImage2 = findViewById(R.id.imgDice2);
        diceImage3 = findViewById(R.id.imgDice3);
        dice2_layout = findViewById(R.id.layout_dice2);
        dice3_layout = findViewById(R.id.layout_dice3);
        txtView_Dice1 = findViewById(R.id.textView1);
        txtView_Dice2 = findViewById(R.id.textView2);
        txtView_Dice3 = findViewById(R.id.textView3);

        Button btnRoll = findViewById(R.id.btnRollTheDice);
        btnSubractDice = findViewById(R.id.btnSubtractDice);
        btnAddDice = findViewById(R.id.btnAddDice);


        // Set the default starting dice position @ 1 d20
        numberOfDice = 1;
        dice2_layout.setVisibility(View.GONE);
        dice3_layout.setVisibility(View.GONE);

        diceImage1.setImageResource(R.drawable.d20_blank);
        diceImage1.setTag("d20");
        txtView_Dice1.setVisibility(View.VISIBLE);

        diceImage2.setImageResource(R.drawable.d20_blank);
        diceImage2.setTag("d20");
        txtView_Dice2.setVisibility(View.GONE);

        diceImage3.setImageResource(R.drawable.d20_blank);
        diceImage3.setTag("d20");
        txtView_Dice3.setVisibility(View.GONE);

         mp = MediaPlayer.create(this, R.raw.dice_sound);


        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rollTheDice();
            }
        });

        btnSubractDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numberOfDice > 1) {

                    removeDice();

                }

            }
        });

        btnAddDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numberOfDice < 3) {

                    addDice();

                }

            }
        });

        diceImage1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return switchDiceType(diceImage1, event);

            }
        });

        diceImage2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                 return switchDiceType(diceImage2, event);

            }
        });

        diceImage3.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return switchDiceType(diceImage3, event);

            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    void rollTheDice() {

        Random rndObject = new Random();
        int myRandomNumber;
        String toastMsg = "";
        Boolean rolledCrit = false;

        // Calculates the total value of d6s rolled
        int totalD6RollValue = 0;

        if (diceImage1.getTag().equals("d6")) {

            myRandomNumber = rndObject.nextInt(6);
            diceImage1.setImageResource(d6Images[myRandomNumber]);

            // Add random number + 1 to the total
            totalD6RollValue += myRandomNumber + 1;

        } else {

            myRandomNumber = rndObject.nextInt(20) + 1;
            txtView_Dice1.setText(Integer.toString(myRandomNumber));

            if (myRandomNumber == 20)
                rolledCrit = true;

            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(txtView_Dice1);
        }

        YoYo.with(Techniques.Shake)
                .duration(500)
                .repeat(0)
                .playOn(diceImage1);

        if (numberOfDice > 1) {

            if (diceImage2.getTag().equals("d6")) {

                myRandomNumber = rndObject.nextInt(6);
                diceImage2.setImageResource(d6Images[myRandomNumber]);

                // Add random number + 1 to the total
                totalD6RollValue += myRandomNumber + 1;

            } else {

                myRandomNumber = rndObject.nextInt(20) + 1;
                txtView_Dice2.setText(Integer.toString(myRandomNumber));

                if (myRandomNumber == 20)
                    rolledCrit = true;

                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(txtView_Dice2);
            }

            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(diceImage2);

            if (numberOfDice == 3) {

                if (diceImage3.getTag().equals("d6")) {

                    myRandomNumber = rndObject.nextInt(6);
                    diceImage3.setImageResource(d6Images[myRandomNumber]);

                    // Add random number + 1 to the total
                    totalD6RollValue += myRandomNumber + 1;

                } else {

                    myRandomNumber = rndObject.nextInt(20) + 1;
                    txtView_Dice3.setText(Integer.toString(myRandomNumber));

                    if (myRandomNumber == 20)
                        rolledCrit = true;

                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .repeat(0)
                            .playOn(txtView_Dice3);
                }

                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .repeat(0)
                        .playOn(diceImage3);

            }

        }

        mp.start();

        if (rolledCrit)
            toastMsg = "You rolled a crit!!";

        if (totalD6RollValue > 0) {

            if (toastMsg != "")
                toastMsg = toastMsg + "\n";

            toastMsg = toastMsg + "You rolled: " + totalD6RollValue + "";

        }

        if (toastMsg != "")
            Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();


    }

    void removeDice () {

        switch (numberOfDice) {

            case 2:

                dice2_layout.setVisibility(View.GONE);
                txtView_Dice2.setVisibility(View.GONE);

                numberOfDice --;

                break;

            case 3:

                dice3_layout.setVisibility(View.GONE);
                txtView_Dice3.setVisibility(View.GONE);

                numberOfDice --;

                break;


            default:

                break;

        }
    }

    void addDice () {

        switch (numberOfDice) {

            case 1:

                dice2_layout.setVisibility(View.VISIBLE);

                if (diceImage2.getTag() == "d20") {

                    txtView_Dice2.setVisibility(View.VISIBLE);

                }

                numberOfDice ++;

                break;

            case 2:

                dice3_layout.setVisibility(View.VISIBLE);

                if (diceImage3.getTag() == "d20") {

                    txtView_Dice3.setVisibility(View.VISIBLE);

                }

                numberOfDice ++;

                break;


                default:

                break;

        }
    }


    Boolean switchDiceType(ImageView v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                //clear the overlay
                ((ImageView) v).getDrawable().clearColorFilter();
                ((ImageView) v).invalidate();

                if (v.getTag().equals("d6")) {

                    if (v == findViewById(R.id.imgDice1)) {

                        txtView_Dice1.setVisibility(View.VISIBLE);

                    } else if (v == findViewById(R.id.imgDice2)) {

                        txtView_Dice2.setVisibility(View.VISIBLE);

                    } else {

                        txtView_Dice3.setVisibility(View.VISIBLE);

                    }

                    v.setImageResource(R.drawable.d20_blank);
                    v.setTag("d20");

                } else {

                    if (v == findViewById(R.id.imgDice1)) {

                        txtView_Dice1.setVisibility(View.GONE);

                    } else if (v == findViewById(R.id.imgDice2)) {

                        txtView_Dice2.setVisibility(View.GONE);

                    } else {

                        txtView_Dice3.setVisibility(View.GONE);

                    }

                    v.setImageResource(d6Images[5]);
                    v.setTag("d6");
                }

                break;
            }
        }

        return true;



    }

    void handleShakeEvent (int count) {

        rollTheDice();

    }

}

