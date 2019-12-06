package com.example.sharecontainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xuexiang.xui.widget.actionbar.TitleBar;

public class Retrievepassword extends AppCompatActivity {
    private TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrievepassword);
        titleBar=findViewById(R.id.titlebar);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
