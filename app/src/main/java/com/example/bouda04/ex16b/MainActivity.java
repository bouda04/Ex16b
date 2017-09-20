package com.example.bouda04.ex16b;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    private Object goLock = new Object();
    private Object syncher = new Object();
    private boolean go = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startThem(View v){

        synchronized (goLock) {
            go = true;
            goLock.notifyAll();
        }
    }
}
