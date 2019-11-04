package com.example.a190510_gcglist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.FrameMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentSecond extends Fragment {
    public FragmentSecond(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_main,container,false);
        return rootView;
    }
}
