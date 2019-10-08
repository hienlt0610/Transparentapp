package dev.hienlt.transparentapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Test2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        findViewById(R.id.tv_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneInputDialogFragment.show(getSupportFragmentManager());
            }
        });
    }
}
