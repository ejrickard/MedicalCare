package com.example.medicalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

public class BmiActivity extends AppCompatActivity {

    TextView mBmiDisplay;
    TextView mBmiCategory;
    TextView mGender;
    Button mGoToMain;
    Intent intent;
    ImageView mimageview;
    String mBmi;
    float intBmi;
    String height;
    String weight;
    float intHeight,intWeight;
    RelativeLayout mBackground;

    @SuppressLint("ResourceAsColor")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

       // Objects.requireNonNull(getSupportActionBar()).setElevation(0);
       // ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#1E1D1D"));
      //  getSupportActionBar().setBackgroundDrawable(colorDrawable);

  //      getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\"></font>"));
  //      getSupportActionBar().setTitle("Result"); */


        intent=getIntent();
        mBmiDisplay=findViewById(R.id.bmidisplay);
        mBmiCategory = findViewById(R.id.bmicategorydispaly);
        mGoToMain=findViewById(R.id.gotomain);

        mimageview=findViewById(R.id.imageview);

        mGender=findViewById(R.id.genderdisplay);
        mBackground=findViewById(R.id.contentlayout);


        height=intent.getStringExtra("height");
        weight=intent.getStringExtra("weight");


        intHeight=Float.parseFloat(height);
        intWeight=Float.parseFloat(weight);

        intHeight/=100;
        intBmi=intWeight/(intHeight*intHeight);


        mBmi=Float.toString(intBmi);
        System.out.println(mBmi);

        if(intBmi<16)
        {
            mBmiCategory.setText(R.string.severe);
            mBackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.crosss);

        }
        else if(intBmi<16.9 && intBmi>16)
        {
            mBmiCategory.setText("");
            mBackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);

        }
        else if(intBmi<18.4 && intBmi>17)
        {
            mBmiCategory.setText(R.string.moderate);
            mBackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        }
        else if(intBmi<24.9 && intBmi>18.5 )
        {
            mBmiCategory.setText(R.string.normal);
            mimageview.setImageResource(R.drawable.ok);
        }
        else if(intBmi <29.9 && intBmi>25)
        {
            mBmiCategory.setText(R.string.overWeight);
            mBackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        }
        else if(intBmi<34.9 && intBmi>30)
        {
            mBmiCategory.setText(R.string.obeseI);
            mBackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        }
        else
        {
            mBmiCategory.setText(R.string.obeseII);
            mBackground.setBackgroundColor(R.color.warn);
            mimageview.setImageResource(R.drawable.crosss);
        }

        mGender.setText(intent.getStringExtra("gender"));
        mBmiDisplay.setText(mBmi);


        mGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BmiActivity.this, BmiCalculatorActivity.class);
        startActivity(intent);
        finish();
    }
}