package com.example.finmerge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
 Button currency, face, text , color;
 boolean cur,f, t,col;
RelativeLayout myLayout;
AnimationDrawable animationDrawable;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
animationDrawable = (AnimationDrawable) myLayout.getBackground();
animationDrawable.setEnterFadeDuration(1500);
animationDrawable.setExitFadeDuration(1500);
animationDrawable.start();

        currency = findViewById(R.id.buttonForCurrency);
        face = findViewById(R.id.buttonForFace);
        text = findViewById(R.id.buttonForText);
        color = findViewById(R.id.buttonForColor);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!f){
                    f=true;
                    textToSpeech.speak("Face Detection",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
                f=false;

                Intent i = getPackageManager().getLaunchIntentForPackage("com.example.facedetectapp");
                textToSpeech.speak("Face Detection",TextToSpeech.QUEUE_FLUSH,null);
              startActivity(i);
            }
        }
        );

        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!t){
                    t=true;
                    textToSpeech.speak("Text Recognition",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
                t=false;

                Intent i = getPackageManager().getLaunchIntentForPackage("com.example.myapplicationtr2");
                textToSpeech.speak("Text Recognition",TextToSpeech.QUEUE_FLUSH,null);
                startActivity(i);
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!col){
                    col=true;
                    textToSpeech.speak("Color Recognition",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
                col=false;

                Intent i = getPackageManager().getLaunchIntentForPackage("limited.it.planet.pickcameraandgalleryimage");
                textToSpeech.speak("Color Recognition",TextToSpeech.QUEUE_FLUSH,null);
                startActivity(i);
            }
        });

        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cur){
                    cur=true;
                    textToSpeech.speak("Currency Recognition",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
                cur=false;

                Intent i = getPackageManager().getLaunchIntentForPackage("com.cr.myapplicationtr2");
                textToSpeech.speak("Currency Recognition",TextToSpeech.QUEUE_FLUSH,null);
                startActivity(i);
            }
        });
    }
}
