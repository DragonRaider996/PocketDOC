package com.example.p.pocketdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image = (ImageView) findViewById(R.id.imageLogoView);

        YoYo.with(Techniques.FlipInX).duration(1700).playOn(image);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    SharedPreferences wizardPreferences = getApplicationContext().getSharedPreferences("Wizard",MODE_PRIVATE);
                    SharedPreferences.Editor wizardEditor = wizardPreferences.edit();

                    String wizard = wizardPreferences.getString("Wizard",null);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String login = sharedPreferences.getString("token",null);

                    if(wizard == null)
                    {
                        wizardEditor.putString("Wizard","Done");
                        wizardEditor.commit();
                        Intent intent = new Intent(getApplicationContext(),Wizard.class);
                        startActivity(intent);
                    }

                    else
                    {
                        if(login == null)
                        {
                            Intent intent = new Intent(getApplicationContext(),Login.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(),Home.class);
                            startActivity(intent);
                        }

                    }


                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }


}