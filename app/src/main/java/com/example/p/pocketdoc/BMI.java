package com.example.p.pocketdoc;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.math.BigDecimal;

public class BMI extends AppCompatActivity {

    CollapsingToolbarLayout layout;
    Toolbar toolbar;
    TextInputLayout weight;
    TextInputLayout height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        weight = (TextInputLayout) findViewById(R.id.weight);
        height = (TextInputLayout) findViewById(R.id.height);

        layout = (CollapsingToolbarLayout) findViewById(R.id.collapseBmi);
        layout.setTitle("Body Mass Index");

        toolbar = (Toolbar) findViewById(R.id.toolbarBmi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weight.setErrorEnabled(true);
        height.setErrorEnabled(true);
    }

    public void calculate(View view)
    {
        int weightValue=0,heightValue=0,flag = 0;
        String weightTemp = weight.getEditText().getText().toString().trim();
        String heightTemp = height.getEditText().getText().toString().trim();

        if(weightTemp.isEmpty() || weightTemp.contains(" "))
        {
            weight.setError("Please Enter a value");
        }
        else
        {
            if(weightTemp.length() > 7)
            {
                weight.setError("Please Enter a Valid Weight");
            }
            else
            {
                weight.setErrorEnabled(false);
                weight.setError(null);
                flag = flag+1;
                weightValue = Integer.parseInt(weightTemp);
            }
        }

        if(heightTemp.isEmpty() || heightTemp.contains(" "))
        {
            height.setError("Please Enter a value");
        }
        else
        {
            if(heightTemp.length() > 7)
            {
                height.setError("Please Enter a Valid Weight");
            }
            else
            {
                height.setErrorEnabled(false);
                height.setError(null);
                flag = flag+1;
                heightValue = Integer.parseInt(heightTemp);
            }
        }

        if(flag == 2)
        {
            int temp = heightValue * heightValue;
            double bmi = (weightValue * 10000.0) / temp;
            MaterialDialog dialog;
            BigDecimal value = new BigDecimal(bmi);
            value = value.setScale(2, BigDecimal.ROUND_DOWN);
            String dis = value.toString();
            String displayBMI = "Your BMI value is : "+dis;
            String displayText = "";

            if(bmi<18.5)
            {
                displayText = "You come under the 'Under Weight Category'";
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("BMI").content(displayBMI +"\n" +displayText).positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            else if(bmi >= 18.5 && bmi <= 24.9)
            {
                displayText = "You come under the 'Normal Weight Category'";
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("BMI").content(displayBMI +"\n" +displayText).positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            else if (bmi >= 25 && bmi <= 29.9)
            {
                displayText = "You come under the 'Over Weight Category'";
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("BMI").content(displayBMI +"\n" +displayText).positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            else
            {
                displayText = "You are suffering from 'Obesity'";
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("BMI").content(displayBMI +"\n" +displayText).positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }
}
