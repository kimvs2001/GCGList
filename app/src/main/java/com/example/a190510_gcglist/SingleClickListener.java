package com.example.a190510_gcglist;

import android.os.SystemClock;
import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener{

    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;
    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime=SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        // 중복 클릭인 경우
        if(elapsedTime<=MIN_CLICK_INTERVAL){
            return;
        }

        // 중복 클릭아 아니라면 추상함수 호출
        onSingleClick(v);
    }

    //new AdapterView.OnItemClickListener() {
}

