package com.example.a190510_gcglist;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CONTROLS  extends AppCompatActivity {
    static View popupInputDialogView = null;
    static EditText tagEditText = null;
    static EditText userNameEditText = null;
    static EditText passwordEditText = null;
    static EditText ipEditText = null;
    static EditText portEditText = null;
    static EditText keywordEditText = null;
    static TextView retValTextView = null;
    static TextView retValView=null;
    static Button saveDataButton = null;
    static Button cancelDataButton = null;
     StringBuilder keyword_gcg = new StringBuilder("ps -ef | grep \"GCGManager\" | grep -v 'grep' | awk '{print $8}'");
    /**DIalog ***/
    //Dialog
    AlertDialog alertDialog =null;

    //Dialog Button
//    static Button modifyDataButton = null;
//    static Button deleteDataButton = null;
    static TextView zemock = null;
    static Toolbar toolbar = null;

    Context m_context;
    /** DIalog ***/
    CONTROLS(Context _context){
        m_context = _context;
        initViewControls();
        initPopupViewControls();
    }
     void initViewControls(){
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        zemock = (TextView)findViewById(R.id.zemock);
//        zemock.setText("장치 목록");

    }
     void initPopupViewControls() {
        LayoutInflater layoutInflater = LayoutInflater.from(m_context);

        popupInputDialogView = layoutInflater.inflate(R.layout.dialog, null);
        // Get user input edittext and button ui controls in the popup dialog.
        tagEditText = (EditText) popupInputDialogView.findViewById(R.id.tag);
        userNameEditText = (EditText) popupInputDialogView.findViewById(R.id.userName);
        passwordEditText = (EditText) popupInputDialogView.findViewById(R.id.password);
        ipEditText = (EditText) popupInputDialogView.findViewById(R.id.ip);
        portEditText = (EditText) popupInputDialogView.findViewById(R.id.port);
        keywordEditText = (EditText) popupInputDialogView.findViewById(R.id.keyword);
        retValTextView=(TextView)  popupInputDialogView.findViewById(R.id.retVal);
        retValView=(TextView)  popupInputDialogView.findViewById(R.id.retValView);
//        saveDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
//        cancelDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
//        modifyDataButton = popupInputDialogView.findViewById(R.id.button_modify_user_data);
//        deleteDataButton = popupInputDialogView.findViewById(R.id.button_delete_user_data);

    }

}