package com.example.a190510_gcglist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAlertDialog extends DialogFragment {
    static AlertDialog m_alertDialog;
    static DBHelper m_dbHelper;
    static ListViewAdapter m_adapter;
    static ArrayList<String> m_arrList;
    static MyListView m_mlv;

    AlertDialog.Builder m_builder;
    BtnOnClickListener m_onClickListener;

    View popupInputDialogView = null;
    Button saveDataButton = null;
    Button cancelDataButton = null;
    Button modifyDataButton = null;
    Button deleteDataButton = null;
//    private MainActivity  m_callBackActivity;





    String tmp_tag;// = m_arrList.get(DATA.COLNUM * position + DATA.TAG);
    String tmp_id;//= m_arrList.get(DATA.COLNUM * position + DATA.ID);
    String tmp_ip;//= m_arrList.get(DATA.COLNUM * position + DATA.IP);
    String tmp_pw;//= m_arrList.get(DATA.COLNUM * position + DATA.PASSWORD);
    String tmp_port;//= m_arrList.get(DATA.COLNUM * position + DATA.PORT);
    String tmp_keyword;//= m_arrList.get(DATA.COLNUM * position + DATA.KEYWORD);
    String tmp_retVal;//= m_arrList.get(DATA.COLNUM * position + DATA.RETVALUE);

    public static MyAlertDialog newInstance(DBHelper _dbHelper, ListViewAdapter _adapter, MyListView _mlv,ArrayList<String> _arrList) {


        m_adapter = _adapter;
        m_dbHelper = _dbHelper;
        m_mlv = _mlv;
        m_arrList = _arrList;

        Bundle args = new Bundle();
        MyAlertDialog fragment = new MyAlertDialog();
        fragment.setArguments(args);
        return fragment;
    }
//    public MyAlertDialog(DBHelper _dbHelper, ListViewAdapter _adapter, MyListView _mlv) {
////        m_alertDialog = (AlertDialog) onCreateDialog(DATA.MY_DIALOG);
//
//    }

//    protected Dialog onCreateDialog(int id) {
//        return new AlertDialog.Builder(this)
//                .setCancelable(true)
//                .setView(CONTROLS.popupInputDialogView)
//                .create();
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(getArguments()!=null){

        }
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
//        popupInputDialogView = inflater.inflate(R.layout.dialog,container,false);
        saveDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
        cancelDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
        modifyDataButton = popupInputDialogView.findViewById(R.id.button_modify_user_data);
        deleteDataButton = popupInputDialogView.findViewById(R.id.button_delete_user_data);
        CONTROLS.tagEditText = (EditText) popupInputDialogView.findViewById(R.id.tag);
        CONTROLS.userNameEditText = (EditText) popupInputDialogView.findViewById(R.id.userName);
        CONTROLS.passwordEditText = (EditText) popupInputDialogView.findViewById(R.id.password);
        CONTROLS.ipEditText = (EditText) popupInputDialogView.findViewById(R.id.ip);
        CONTROLS.portEditText = (EditText) popupInputDialogView.findViewById(R.id.port);
        CONTROLS.keywordEditText = (EditText) popupInputDialogView.findViewById(R.id.keyword);





        saveDataButton.setVisibility(View.VISIBLE);
        modifyDataButton.setVisibility(View.GONE);
        deleteDataButton.setVisibility(View.GONE);

        m_onClickListener = new BtnOnClickListener() ;
        saveDataButton.setOnClickListener(m_onClickListener);
        cancelDataButton.setOnClickListener(m_onClickListener);
        modifyDataButton.setOnClickListener(m_onClickListener);
        deleteDataButton.setOnClickListener(m_onClickListener);


        //m_arrList =m_dbHelper.getAllList(); //add 19.10.02 ,delete 19.10.19







        return super.onCreateView(inflater, container, savedInstanceState);
    }





    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
//        m_callBackActivity = (MainActivity)getActivity();
        m_builder = new AlertDialog.Builder(getActivity());
        popupInputDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog,null);
//        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(mMainMsg);
//        view.findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);

        m_builder.setView(popupInputDialogView);
        m_alertDialog = m_builder.create();
        m_alertDialog.setCanceledOnTouchOutside(false);





        return  m_builder.create();
    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case  R.id.button_save_user_data:
                    String tag = CONTROLS.tagEditText.getText().toString();
                             String userName = CONTROLS.userNameEditText.getText().toString();
                             String password = CONTROLS.passwordEditText.getText().toString();
                             String ip = CONTROLS.ipEditText.getText().toString();
                             String port = CONTROLS.portEditText.getText().toString();
                             String keyword = CONTROLS.keywordEditText.getText().toString();

                             long cnt = m_dbHelper.insert(tag,userName,ip,port,password,keyword);
                             if(cnt < 0){
                                 Toast.makeText(getActivity(), "실 패", Toast.LENGTH_LONG).show();
                             }else{
                                 Toast.makeText(getActivity(), "cnt : "+cnt+"추가 성공 db : "+ m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();

                                 m_arrList.add(Integer.toString(m_dbHelper.getArrNumOfLastRow()));
                                 m_arrList.add(tag);
                                 m_arrList.add(userName);
                                 m_arrList.add(ip);
                                 m_arrList.add(password);
                                 m_arrList.add(port);
                                 m_arrList.add("NONE");
                                 m_arrList.add("Return Value");
                                 m_arrList.add(keyword);
                                 m_adapter.notifyDataSetChanged();
                                 m_mlv.listViewUpdate(m_adapter,m_arrList);
                             }
                            //m_callBackActivity.setArrList(m_arrList);
                             dismiss();
                    break ;
                case R.id.button_cancel_user_data :

                    dismiss();

//                    Toast.makeText(getActivity(), "취소버튼", Toast.LENGTH_LONG).show();
                    break ;


            }
        }
    }






    protected void onPrepareAddDialog(){//int id, ArrayList<String> _arrList) {
//        m_arrList = _arrList;
//        super.onPrepareDialog(id, dialog);
      //  m_builder= new AlertDialog.Builder(getActivity());
        CONTROLS.userNameEditText.setText("");///
        CONTROLS.passwordEditText.setText("");
        CONTROLS.ipEditText.setText("");
        CONTROLS.portEditText.setText("8001");
        CONTROLS.tagEditText.setText("");
        CONTROLS.keywordEditText.setText("GCGManager");
        CONTROLS.retValTextView.setVisibility(View.GONE);
        CONTROLS.retValView.setVisibility(View.GONE);
        CONTROLS.saveDataButton.setVisibility(View.VISIBLE);
        modifyDataButton.setVisibility(View.GONE);
        deleteDataButton.setVisibility(View.GONE);
        //m_alertDialog = m_builder.create();
    }

    protected void onPrepareShowDialog(int id, Dialog dialog, String host_tag, String host_id, String host_ip, String host_pw, String host_port, String keyword, String tmp_retVal, ArrayList<String> _arrList) {
        m_arrList = _arrList;
//        super.onPrepareDialog(id, dialog);
        //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        CONTROLS.userNameEditText.setText(host_id);///
        CONTROLS.passwordEditText.setText(host_pw);
        CONTROLS.ipEditText.setText(host_ip);
        CONTROLS.portEditText.setText(host_port);
        CONTROLS.tagEditText.setText(host_tag);
        CONTROLS.keywordEditText.setText(keyword);
        CONTROLS.retValTextView.setText(tmp_retVal);
        CONTROLS.retValTextView.setMovementMethod(new ScrollingMovementMethod());
        CONTROLS.retValTextView.scrollTo(0, 0);
        CONTROLS.retValTextView.setVisibility(View.VISIBLE);
        CONTROLS.retValView.setVisibility(View.VISIBLE);
        modifyDataButton.setVisibility(View.VISIBLE);
        deleteDataButton.setVisibility(View.VISIBLE);
        CONTROLS.saveDataButton.setVisibility(View.GONE);
       // m_alertDialog = alertDialogBuilder.create();
    }

//    public void show() {
//        m_alertDialog.show();
//    }

    public Dialog getDialog() {
        return m_alertDialog;
    }

//
//    public void onClick(View view) {
//
//
//
//
//        int position = 1; // TEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! DLEETE !! position
//        // 리스트뷰에서 포지션을 받아오던지 하는 방법을 찾아야 하겠다.
//        // 일단 1번으로 고정...
//
//        switch (view.getId()){
//            case R.id.button_cancel_user_data:
//                m_alertDialog.cancel();
//            case R.id.button_delete_user_data:
//                m_dbHelper.delete(Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM)));
//                for (int i = 0; i < DATA.COLNUM; i++) {
//                    m_arrList.remove(DATA.COLNUM * position);
//                }
//                m_adapter.notifyDataSetChanged();
//                m_mlv.listViewUpdate(m_adapter);
//                m_alertDialog.cancel();
//            case R.id.button_modify_user_data:
//                String tag = CONTROLS.tagEditText.getText().toString();
//                String userName = CONTROLS.userNameEditText.getText().toString();
//                String password = CONTROLS.passwordEditText.getText().toString();
//                String ip = CONTROLS.ipEditText.getText().toString();
//                String port = CONTROLS.portEditText.getText().toString();
//                String keyword = CONTROLS.keywordEditText.getText().toString();
//                int arrnum = Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM));
//                //dbhelper.delete(Integer.parseInt(arrList.get(COLNUM * position+_ARRNUM)));
//
//
//                int cnt = m_dbHelper.update(arrnum, tag, userName, ip, port, password, keyword);
//                if (cnt == -2) {
//                    Toast.makeText(getActivity(), "수정실패! : 키워드를 확인하십시요\n(특수문자 포함 불가)", Toast.LENGTH_LONG).show();
//                } else if (cnt < 0) {
//                    Toast.makeText(getActivity(), "수정 실패", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(), "cnt : " + cnt + "수정 성공 , db : " + m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//                    m_arrList.set(DATA.COLNUM * position + DATA.TAG, tag);
//                    m_arrList.set(DATA.COLNUM * position + DATA.ID, userName);
//                    m_arrList.set(DATA.COLNUM * position + DATA.IP, ip);
//                    m_arrList.set(DATA.COLNUM * position + DATA.PASSWORD, password);
//                    m_arrList.set(DATA.COLNUM * position + DATA.PORT, port);
//                    m_arrList.set(DATA.COLNUM * position + DATA.STATE, "NONE");
//                    m_arrList.set(DATA.COLNUM * position + DATA.RETVALUE, "Return Value");
//                    m_arrList.set(DATA.COLNUM * position + DATA.KEYWORD, keyword);
//
//                    m_adapter.notifyDataSetChanged();
//                    m_mlv.listViewUpdate(m_adapter);
//                }
//                m_alertDialog.cancel();
//
//        }
//    }


    private class initOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            cancelDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m_alertDialog.cancel();
                }
            });
            deleteDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m_dbHelper.delete(Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM)));
                    for (int i = 0; i < DATA.COLNUM; i++) {
                        m_arrList.remove(DATA.COLNUM * position);
                    }
                    m_adapter.notifyDataSetChanged();
                    m_mlv.listViewUpdate(m_adapter,m_arrList);
                    m_alertDialog.cancel();
                }

            });
            modifyDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = CONTROLS.tagEditText.getText().toString();
                    String userName = CONTROLS.userNameEditText.getText().toString();
                    String password = CONTROLS.passwordEditText.getText().toString();
                    String ip = CONTROLS.ipEditText.getText().toString();
                    String port = CONTROLS.portEditText.getText().toString();
                    String keyword = CONTROLS.keywordEditText.getText().toString();
                    int arrnum = Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM));
                    //dbhelper.delete(Integer.parseInt(arrList.get(COLNUM * position+_ARRNUM)));


                    int cnt = m_dbHelper.update(arrnum, tag, userName, ip, port, password, keyword);
                    if (cnt == -2) {
                        Toast.makeText(getActivity(), "수정실패! : 키워드를 확인하십시요\n(특수문자 포함 불가)", Toast.LENGTH_LONG).show();
                    } else if (cnt < 0) {
                        Toast.makeText(getActivity(), "수정 실패", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "cnt : " + cnt + "수정 성공 , db : " + m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
                        m_arrList.set(DATA.COLNUM * position + DATA.TAG, tag);
                        m_arrList.set(DATA.COLNUM * position + DATA.ID, userName);
                        m_arrList.set(DATA.COLNUM * position + DATA.IP, ip);
                        m_arrList.set(DATA.COLNUM * position + DATA.PASSWORD, password);
                        m_arrList.set(DATA.COLNUM * position + DATA.PORT, port);
                        m_arrList.set(DATA.COLNUM * position + DATA.STATE, "NONE");
                        m_arrList.set(DATA.COLNUM * position + DATA.RETVALUE, "Return Value");
                        m_arrList.set(DATA.COLNUM * position + DATA.KEYWORD, keyword);

                        m_adapter.notifyDataSetChanged();
                        m_mlv.listViewUpdate(m_adapter,m_arrList);
                    }
                    m_alertDialog.cancel();

                }
            });


        }


    }




}


