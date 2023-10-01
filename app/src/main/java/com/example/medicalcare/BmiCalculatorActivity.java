package com.example.medicalcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class BmiCalculatorActivity extends AppCompatActivity {

    TextView mCurrentHeight;
    TextView mCurrentWeight,mCurrentAge;
    ImageView minIncrementAge,mDecrementAge,minIncrementWeight,mDecrementWeight;
    SeekBar mSeekbarForHeight;
    Button mCalculateBmi;
    RelativeLayout mMale,mFemale;
    int intWeight=55;
    int intAge=22;
    int currentProgress;
    String mintProgress="170";
    String typeOfUser="0";
    String weight2="55";
    String age2="22";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        mCurrentAge=findViewById(R.id.currentage);
        mCurrentWeight=findViewById(R.id.currentweight);
        mCurrentHeight=findViewById(R.id.currentheight);
        minIncrementAge=findViewById(R.id.incrementage);
        mDecrementAge=findViewById(R.id.decrementage);
        minIncrementWeight=findViewById(R.id.incremetweight);
        mDecrementWeight=findViewById(R.id.decrementweight);
        mCalculateBmi=findViewById(R.id.calculatebmi);
        mSeekbarForHeight=findViewById(R.id.seekbarforheight);
        mMale=findViewById(R.id.male);
        mFemale=findViewById(R.id.female);



        mMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalefocus));
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalenotfocus));
                typeOfUser="Male";

            }
        });


        mFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalefocus));
                mMale.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalenotfocus));
                typeOfUser="Female";
            }
        });

        mSeekbarForHeight.setMax(300);
        mSeekbarForHeight.setProgress(170);
        mSeekbarForHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currentProgress=progress;
                mintProgress=String.valueOf(currentProgress);
                mCurrentHeight.setText(mintProgress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        minIncrementWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intWeight+=1;
                weight2=String.valueOf(intWeight);
                mCurrentWeight.setText(weight2);
            }
        });

        minIncrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intAge+=1;
                age2=String.valueOf(intAge);
                mCurrentAge.setText(age2);
            }
        });


        mDecrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intAge-=1;
                age2=String.valueOf(intAge);
                mCurrentAge.setText(age2);
            }
        });


        mDecrementWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intWeight-=1;
                weight2=String.valueOf(intWeight);
                mCurrentWeight.setText(weight2);
            }
        });



        mCalculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(typeOfUser.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Select Your Gender First",Toast.LENGTH_SHORT).show();
                }
                else if(mintProgress.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Select Your Height First",Toast.LENGTH_SHORT).show();
                }
                else if(intAge==0 || intAge<0)
                {
                    Toast.makeText(getApplicationContext(),"Age is Incorrect",Toast.LENGTH_SHORT).show();
                }

                else if(intWeight==0|| intWeight<0)
                {
                    Toast.makeText(getApplicationContext(),"Weight Is Incorrect",Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(BmiCalculatorActivity.this, BmiActivity.class);
                    intent.putExtra("gender", typeOfUser);
                    intent.putExtra("height", mintProgress);
                    intent.putExtra("weight", weight2);
                    intent.putExtra("age", age2);
                    startActivity(intent);

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BmiCalculatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}