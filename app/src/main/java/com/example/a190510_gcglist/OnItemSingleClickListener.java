package com.example.a190510_gcglist;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

public abstract class OnItemSingleClickListener implements AdapterView.OnItemClickListener{

    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;
    public abstract void onItemSingleClick(AdapterView<?> adapterView, View view, int i, long l);

    @Override
    public final void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        long currentClickTime= SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        // 중복 클릭인 경우
        if(elapsedTime<=MIN_CLICK_INTERVAL){
            return;
        }

        // 중복 클릭아 아니라면 추상함수 호출
        onItemSingleClick(adapterView,view,i,l);
    }

    //new AdapterView.OnItemClickListener() {
}