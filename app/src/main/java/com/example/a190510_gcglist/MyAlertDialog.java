package com.example.a190510_gcglist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAlertDialog extends DialogFragment  {//implements View.OnClickListener{//Button.OnClickListener{


    static AlertDialog m_alertDialog;
    static DBHelper m_dbHelper;
    static ListViewAdapter m_adapter;
    static ArrayList<String> m_arrList;
    static MyListView m_mlv;
//    static CONTROLS m_controls;
    AlertDialog.Builder m_builder;

    View popupInputDialogView = null;


    EditText tagEditText = null;
    EditText userNameEditText = null;
    EditText passwordEditText = null;
    EditText ipEditText = null;
    EditText portEditText = null;
    EditText keywordEditText = null;
    TextView retValTextView = null;
    TextView retValView=null;
    Button saveDataButton = null;
    Button cancelDataButton = null;
    Button modifyDataButton = null;
    Button deleteDataButton = null;







    String tmp_tag;// = m_arrList.get(DATA.COLNUM * position + DATA.TAG);
    String tmp_id;//= m_arrList.get(DATA.COLNUM * position + DATA.ID);
    String tmp_ip;//= m_arrList.get(DATA.COLNUM * position + DATA.IP);
    String tmp_pw;//= m_arrList.get(DATA.COLNUM * position + DATA.PASSWORD);
    String tmp_port;//= m_arrList.get(DATA.COLNUM * position + DATA.PORT);
    String tmp_keyword;//= m_arrList.get(DATA.COLNUM * position + DATA.KEYWORD);
    String tmp_retVal;//= m_arrList.get(DATA.COLNUM * position + DATA.RETVALUE);

    public interface MyAlertDialogListner{
        void onFinishDialogDoAdd(String _tag,String _userName,String _password,String _ip,String _port,String _keyword);
        void onFinishDialogDoDelete(int _position);
        void onFinishDialogDoModify(int _position,String _tag,String _userName,String _password,String _ip,String _port,String _keyword);
    }

    public MyAlertDialog(){

        //Empty constructor
    }

    public static MyAlertDialog newInstance(int _position,DBHelper _dbHelper, ListViewAdapter _adapter, MyListView _mlv,ArrayList<String> _arrList,int _type,String _tag,String _userName,String _password,String _ip,String _port,String _keyword,String _returnValue){

        m_adapter = _adapter;
        m_dbHelper = _dbHelper;
        m_mlv = _mlv;
        m_arrList = _arrList;
//        m_controls = _controls;

        MyAlertDialog fragment = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putInt("type",_type);
        args.putInt("position",_position);
        args.putString("tag",_tag);
        args.putString("userName",_userName);
        args.putString("password",_password);
        args.putString("ip",_ip);
        args.putString("port",_port);
        args.putString("keyword",_keyword);
        args.putString("returnValue",_returnValue);




        fragment.setArguments(args);





        return fragment;
    }





    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog, container);

    }
    @Nullable
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        tagEditText = (EditText) view.findViewById(R.id.tag);
        userNameEditText = (EditText) view.findViewById(R.id.userName);
        passwordEditText = (EditText) view.findViewById(R.id.password);
        ipEditText = (EditText) view.findViewById(R.id.ip);
        portEditText = (EditText) view.findViewById(R.id.port);
        keywordEditText = (EditText) view.findViewById(R.id.keyword);
        retValTextView=(TextView)  view.findViewById(R.id.retVal);
        retValView=(TextView)  view.findViewById(R.id.retValView);
        saveDataButton = view.findViewById(R.id.button_save_user_data);
        cancelDataButton = view.findViewById(R.id.button_cancel_user_data);
        modifyDataButton = view.findViewById(R.id.button_modify_user_data);
        deleteDataButton = view.findViewById(R.id.button_delete_user_data);



        switch (getArguments().getInt("type")){
            case 1:  //DIALOG_TYPE_ADD
                onPrepareAddDialog();
                break;

            case 2:
                onPrepareShowDialog(
                        getArguments().getInt("position"),
                        getArguments().getString("tag"),
                        getArguments().getString("userName"),
                        getArguments().getString("password"),
                        getArguments().getString("ip"),
                        getArguments().getString("port"),
                        getArguments().getString("keyword"),
                        getArguments().getString("returnValue")
                );
                break;
        }

    }






//        @Override
//        public void onClick(View view) {
//
//            switch (view.getId()) {
//
//                case  R.id.button_save_user_data:
//                    String tag = tagEditText.getText().toString();
//                             String userName = userNameEditText.getText().toString();
//                             String password = passwordEditText.getText().toString();
//                             String ip = ipEditText.getText().toString();
//                             String port = portEditText.getText().toString();
//                             String keyword = keywordEditText.getText().toString();
//
//                             long cnt = m_dbHelper.insert(tag,userName,ip,port,password,keyword);
//                             if(cnt < 0){
//                                 Toast.makeText(getActivity(), "실 패", Toast.LENGTH_LONG).show();
//                             }else{
//                                 Toast.makeText(getActivity(), "cnt : "+cnt+"추가 성공 db : "+ m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//
//                                 m_arrList.add(Integer.toString(m_dbHelper.getArrNumOfLastRow()));
//                                 m_arrList.add(tag);
//                                 m_arrList.add(userName);
//                                 m_arrList.add(ip);
//                                 m_arrList.add(password);
//                                 m_arrList.add(port);
//                                 m_arrList.add("NONE");
//                                 m_arrList.add("Return Value");
//                                 m_arrList.add(keyword);
//                                 m_adapter.notifyDataSetChanged();
//                                 m_mlv.listViewUpdate(m_adapter,m_arrList);
//                             }
//                            //m_callBackActivity.setArrList(m_arrList);
//                             dismiss();
//                    break ;
//                case R.id.button_cancel_user_data :
//
//                    dismiss();
//
////                    Toast.makeText(getActivity(), "취소버튼", Toast.LENGTH_LONG).show();
//                    break ;
//
//
//            }
//
//    }




    protected void onPrepareAddDialog(){//MainActivity _mainActivity){//int id, ArrayList<String> _arrList) {
//        m_arrList = _arrList;
//        super.onPrepareDialog(id, dialog);
      //  m_builder= new AlertDialog.Builder(getActivity());



        userNameEditText.setText("");///
        passwordEditText.setText("");
        ipEditText.setText("");
        portEditText.setText("8001");
        tagEditText.setText("");
        keywordEditText.setText("GCGManager");
        retValTextView.setVisibility(View.GONE);
        retValView.setVisibility(View.GONE);
        saveDataButton.setVisibility(View.VISIBLE);
        modifyDataButton.setVisibility(View.GONE);
        deleteDataButton.setVisibility(View.GONE);


        cancelDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = tagEditText.getText().toString();
                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String ip = ipEditText.getText().toString();
                String port = portEditText.getText().toString();
                String keyword = keywordEditText.getText().toString();

//                long cnt = m_dbHelper.insert(tag,userName,ip,port,password,keyword);
//                if(cnt < 0){
//                    Toast.makeText(getActivity(), "실 패", Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(getActivity(), "cnt : "+cnt+"추가 성공 db : "+ m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//
//                    m_arrList.add(Integer.toString(m_dbHelper.getArrNumOfLastRow()));
//                    m_arrList.add(tag);
//                    m_arrList.add(userName);
//                    m_arrList.add(ip);
//                    m_arrList.add(password);
//                    m_arrList.add(port);
//                    m_arrList.add("NONE");
//                    m_arrList.add("Return Value");
//                    m_arrList.add(keyword);
//                    m_adapter.notifyDataSetChanged();
//                    m_mlv.listViewUpdate(m_adapter,m_arrList);
//                }
//                dismiss();


                MyAlertDialogListner listnerInMainActivity = (MyAlertDialogListner)getActivity();
                listnerInMainActivity.onFinishDialogDoAdd(tag,userName,password,ip,port,keyword);
                dismiss();





            }
        });



    }

//    protected void onPrepareShowDialog(int id, Dialog dialog, String host_tag, String host_id, String host_ip, String host_pw, String host_port, String keyword, String tmp_retVal, ArrayList<String> _arrList) {
    void onPrepareShowDialog(int _position,String host_tag, String host_id, String host_ip, String host_pw, String host_port, String keyword, String tmp_retVal){//, ArrayList<String> _arrList,MainActivity _mainActivity) {

//        m_arrList = _arrList;
//        super.onPrepareDialog(id, dialog);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_mainActivity);

//        m_alertDialog.getButton(deleteDataButton.getId()).setVisibility(View.GONE);
        final int position = _position;
        userNameEditText.setText(host_id);///
        passwordEditText.setText(host_pw);
        ipEditText.setText(host_ip);
        portEditText.setText(host_port);
        tagEditText.setText(host_tag);
        keywordEditText.setText(keyword);
        retValTextView.setText(tmp_retVal);
        retValTextView.setMovementMethod(new ScrollingMovementMethod());
        retValTextView.scrollTo(0, 0);
        retValTextView.setVisibility(View.VISIBLE);
        retValView.setVisibility(View.VISIBLE);
        modifyDataButton.setVisibility(View.VISIBLE);
        deleteDataButton.setVisibility(View.VISIBLE);
        saveDataButton.setVisibility(View.GONE);


        deleteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAlertDialogListner listnerInMainActivity = (MyAlertDialogListner)getActivity();
                listnerInMainActivity.onFinishDialogDoDelete(position);
                dismiss();
            }
        });
        modifyDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = tagEditText.getText().toString();
                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String ip = ipEditText.getText().toString();
                String port = portEditText.getText().toString();
                String keyword = keywordEditText.getText().toString();

                MyAlertDialogListner listnerInMainActivity = (MyAlertDialogListner)getActivity();
                listnerInMainActivity.onFinishDialogDoModify(position,tag,userName,password,ip,port,keyword);
                dismiss();
            }
        });
        cancelDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


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


//    private class initOnItemClickListener implements AdapterView.OnItemClickListener {
//        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//            cancelDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    m_alertDialog.cancel();
//                }
//            });
//            deleteDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    m_dbHelper.delete(Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM)));
//                    for (int i = 0; i < DATA.COLNUM; i++) {
//                        m_arrList.remove(DATA.COLNUM * position);
//                    }
//                    m_adapter.notifyDataSetChanged();
//                    m_mlv.listViewUpdate(m_adapter,m_arrList);
//                    m_alertDialog.cancel();
//                }
//
//            });
//            modifyDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String tag = tagEditText.getText().toString();
//                    String userName = userNameEditText.getText().toString();
//                    String password = passwordEditText.getText().toString();
//                    String ip = ipEditText.getText().toString();
//                    String port = portEditText.getText().toString();
//                    String keyword = keywordEditText.getText().toString();
//                    int arrnum = Integer.parseInt(m_arrList.get(DATA.COLNUM * position + DATA.ARRNUM));
//                    //dbhelper.delete(Integer.parseInt(arrList.get(COLNUM * position+_ARRNUM)));
//
//
//                    int cnt = m_dbHelper.update(arrnum, tag, userName, ip, port, password, keyword);
//                    if (cnt == -2) {
//                        Toast.makeText(getActivity(), "수정실패! : 키워드를 확인하십시요\n(특수문자 포함 불가)", Toast.LENGTH_LONG).show();
//                    } else if (cnt < 0) {
//                        Toast.makeText(getActivity(), "수정 실패", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getActivity(), "cnt : " + cnt + "수정 성공 , db : " + m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//                        m_arrList.set(DATA.COLNUM * position + DATA.TAG, tag);
//                        m_arrList.set(DATA.COLNUM * position + DATA.ID, userName);
//                        m_arrList.set(DATA.COLNUM * position + DATA.IP, ip);
//                        m_arrList.set(DATA.COLNUM * position + DATA.PASSWORD, password);
//                        m_arrList.set(DATA.COLNUM * position + DATA.PORT, port);
//                        m_arrList.set(DATA.COLNUM * position + DATA.STATE, "NONE");
//                        m_arrList.set(DATA.COLNUM * position + DATA.RETVALUE, "Return Value");
//                        m_arrList.set(DATA.COLNUM * position + DATA.KEYWORD, keyword);
//
//                        m_adapter.notifyDataSetChanged();
//                        m_mlv.listViewUpdate(m_adapter,m_arrList);
//                    }
//                    m_alertDialog.cancel();
//
//                }
//            });
//
//
//        }


//    }



}


