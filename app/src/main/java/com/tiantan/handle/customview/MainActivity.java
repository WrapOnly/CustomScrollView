package com.tiantan.handle.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tiantan.handle.custom.scroll.MyScrollView01;

public class MainActivity extends AppCompatActivity {

    private int[] imageIds = {
            R.mipmap.a1,
            R.mipmap.a2,
            R.mipmap.a3,
            R.mipmap.a4,
            R.mipmap.a5,
            R.mipmap.a6,
    };
    private MyScrollView01 myScrollView01;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myScrollView01 = (MyScrollView01) findViewById(R.id.my_scroll_view);
        this.radioGroup = (RadioGroup) findViewById(R.id.my_radio_group);

        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            this.myScrollView01.addView(imageView);

            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
            if (i == 0){
                radioButton.setChecked(true);
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myScrollView01.moveToDestination(checkedId);
            }
        });

        this.myScrollView01.setiMyPageChangedListener(new MyScrollView01.IMyPageChangedListener() {
            @Override
            public void moveToDestination(int currentId) {
                RadioButton currentRadioBtn = (RadioButton) radioGroup.getChildAt(currentId);
                currentRadioBtn.setChecked(true);

            }
        });
    }
}
