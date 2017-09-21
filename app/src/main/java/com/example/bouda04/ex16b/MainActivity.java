package com.example.bouda04.ex16b;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    private final int UP =1;
    private final int DOWN=-1;
    private final int DURATION=10000;
    private final int MAX_WORKERS =10;

    private Object goLock = new Object();
    private Object syncher = new Object();
    private boolean go = false;
    private int sharedCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textView1)).setText(
                getResources().getString(R.string.intro, MAX_WORKERS)
        );
    }


    public void startThem(View v){

        synchronized (goLock) {
            this.sharedCounter =0;
            go=false;
            boolean synched = (v.getId()== R.id.btSynch);

            for (int i=0; i<MAX_WORKERS;i++){
                new Worker(DURATION,(i%2==0)?UP:DOWN,synched).start();
            }
            go = true;
            goLock.notifyAll();
//            findViewById(R.id.btSynch).setEnabled(false);
//            findViewById(R.id.btNotSynch).setEnabled(false);
        }
    }

    //first try without 'synchronized' to show that the sharedCounter
    //doesn't return to zero
    private  void updateSharedCounter(final int direction){
        int temp = sharedCounter;
        temp = temp + direction;
        sharedCounter = temp;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.tvCountShared)).setText((Integer.toString(sharedCounter)));
            }
        });
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private class Worker extends Thread{
        int untilVal;
        int direction;
        boolean synched;

        public Worker(int untilVal, int direction, boolean synched){

            this.untilVal= untilVal;
            this.direction = direction;
            this.synched = synched;
        }

        @Override
        public void run() {
            try {
                synchronized (goLock) {
                    while (!go) goLock.wait();
                }
                for (int i=1;i<this.untilVal;i++){
                    Thread.sleep(1);
                    if (synched)
                        synchronized (syncher) {
                            updateSharedCounter(direction);
                        }
                        else
                        updateSharedCounter(direction);
                }

            } catch (Exception e) {
                return;
            }
        }
    }


}
