package com.example.a190510_gcglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;


public class SettingsDialog  extends DialogFragment
{
    ToggleButton allBgExeButton = null;
    Button cancelButton = null;
    Button okayButton = null;
    RadioGroup allBgExeRadioButtonGroupOnOff = null;
    RadioButton allbgExeRadioButton_On = null;
    RadioButton allbgExeRadioButton_Neutral = null;
    RadioButton allbgExeRadioButton_Off = null;

    public interface settingsDialogListener{
        void onFinishedSettingsDialog(Bundle args);
    }
    public SettingsDialog(){}
    public static SettingsDialog newInstance(int _type,int _isAllBgExeOnOff){

        SettingsDialog fragment = new SettingsDialog();
        Bundle args = new Bundle();
        args.putInt("type",_type);
        args.putInt("isAllBgExeOnOff",_isAllBgExeOnOff);
        fragment.setArguments(args);



            return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {




        return inflater.inflate(R.layout.settings_dialog, container);

    }
    @Nullable
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.setCancelable(true);
        super.onViewCreated(view, savedInstanceState);

        allBgExeButton = view.findViewById(R.id.button_allBackGround_Exe_toggle);
        cancelButton = view.findViewById(R.id.button_cancel_settings);
        okayButton = view.findViewById(R.id.button_ok_settings);
        allBgExeRadioButtonGroupOnOff  = view.findViewById(R.id.radio_bg_group);
        allbgExeRadioButton_On = view.findViewById(R.id.radio_On);
        allbgExeRadioButton_Neutral = view.findViewById(R.id.radio_Neutral);
        allbgExeRadioButton_Off = view.findViewById(R.id.radio_Off);

        int isAllBgExeOnOff = getArguments().getInt("isAllBgExeOnOff");
        switch (getArguments().getInt("type")) {

            case 1:
                onPrepareSettingsDialog(isAllBgExeOnOff);
                break;
        }
    }


    public void onPrepareSettingsDialog(final int isAllBgExeOnOff){
        final boolean allBgExe;
        final boolean allBgExeRadioGroupOnOff;
        final int isAllBgExeOnOff_;




        allBgExe = allBgExeButton.isChecked() ;
        setCheckRadioButtonInGroup(isAllBgExeOnOff);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle argsToMainActivity = new Bundle();
                argsToMainActivity.putBoolean("allBgExe",allBgExe);
                argsToMainActivity.putInt("isAllBgExeOnOff",getCheckedRadioButtonNumber(allBgExeRadioButtonGroupOnOff));
                argsToMainActivity.putBoolean("allBgExe",allBgExeButton.isChecked());
                settingsDialogListener listenerInMainActivity = (settingsDialogListener)getActivity();
                listenerInMainActivity.onFinishedSettingsDialog(argsToMainActivity);
                dismiss();
            }
        });
        allBgExeRadioButtonGroupOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_On) {
                    allbgExeRadioButton_On.setChecked(true);
                } else if (checkedId == R.id.radio_Neutral) {
                    allbgExeRadioButton_Neutral.setChecked(true);
                } else if (checkedId == R.id.radio_Off) {
                    allbgExeRadioButton_Off.setChecked(true);
                } else {/*none*/}
            }

        });

        }
    private void setCheckRadioButtonInGroup(int isAllBgExeOnOff){
        switch(isAllBgExeOnOff) {
            case 1:
                allbgExeRadioButton_On.setChecked(true);
                break;
            case 0:
                allbgExeRadioButton_Neutral.setChecked(true);
                break;
            case -1:
                allbgExeRadioButton_Off.setChecked(true);
                break;
        }
    }
    private int getCheckedRadioButtonNumber(RadioGroup _allBgExeRadioButtonGroupOnOff){
        int number = _allBgExeRadioButtonGroupOnOff.getCheckedRadioButtonId();
        if(number == R.id.radio_On) return 1;
        if(number == R.id.radio_Neutral) return 0;
        if(number == R.id.radio_Off) return -1;
        else return 0;
    }

    //    public Dialog onCreateDialog(Bundle savedInstance){
////        View layout = getActivity().getLayoutInflater().inflate(R.layout.settings_dialog,null,false);
////        assert  layout != null;
////        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
////        b.getContext().getTheme().applyStyle(R.style.settingsDialogTheme,true);
////        b.setView(layout);
////        return b.create();
////    }
    @Override
    public void onResume(){

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);

        super.onResume();
    }
}
