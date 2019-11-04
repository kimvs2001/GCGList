package com.example.a190510_gcglist;

import android.app.Dialog;
import android.support.v4.app.Fragment;
//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements MyAlertDialog.MyAlertDialogListner ,MyListView.MyListViewListner {
    public static Context context;

    final int DIALOG_TYPE_ADD = 1;
    final int DIALOG_TYPE_SHOW = 2;
    WorkManager wm ;//= WorkManager.getInstance();

    /**DIalog ***/
    //Dialog
//    AlertDialog alertDialog =null;

    //Dialog Button
//    private Button modifyDataButton = null;
//    private Button deleteDataButton = null;
    private TextView zemock = null;
    /** DIalog ***/




    /**<< Popup dialog*/
    /**DB>>**/
    DBHelper dbhelper ;
    /**<<DB**/
    /** List View ,Adapter>>**/
//    ListView listview;
    ListViewAdapter adapter;
    /**<< List View**/
    /**memory ListView >> **/
    static ArrayList<String> arrList;
    /**<< memory ListView**/



    MyListView mlv;


//    CONTROLS controls;

    /******************************
        TEST IP ADDRESS
     cfamr.iptime.org
     8001
     gcg
     rala
     ps -ef | grep "GCGManager" | grep -v 'grep' | awk '{print $8}'
    ******************************/
    public void exeBackground(){
        Toast.makeText(this, "백그라운드 토글 버튼 터치", Toast.LENGTH_LONG).show();
//        new MyService();
//        WorkManager wm = WorkManager.getInstance();
//        wm.enqueue(new OneTimeWorkRequest.Builder(MyService.class).build());

        wm = WorkManager.getInstance();
        wm.enqueue(new PeriodicWorkRequest.Builder(
                MyService.class,5, TimeUnit.SECONDS
        )
                .build());

        adapter.notifyDataSetChanged();
        mlv.listViewUpdateForState(adapter,arrList);


    }
    public void onFinishDialogDoModify(int _position,String _tag,String _userName,String _password,String _ip,String _port,String _keyword,String _state,String  _backGroundInterval ,String _backGroundOnOff){
        int arrnum = Integer.parseInt(arrList.get(DATA.COLNUM * _position + DATA.ARRNUM));

        int cnt = dbhelper.update(arrnum, _tag, _userName, _ip, _port, _password, _keyword,_state,"",_backGroundInterval,_backGroundOnOff);
        if (cnt == -2) {
            Toast.makeText(this, "수정실패! : 키워드를 확인하십시요\n(특수문자 포함 불가)", Toast.LENGTH_LONG).show();
        } else if (cnt < 0) {
            Toast.makeText(this, "수정 실패", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "cnt : " + cnt + "수정 성공 , db : " + dbhelper.getDatabaseName(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "완료", Toast.LENGTH_LONG).show();
            arrList.set(DATA.COLNUM * _position + DATA.TAG, _tag);
            arrList.set(DATA.COLNUM * _position + DATA.ID, _userName);
            arrList.set(DATA.COLNUM * _position + DATA.IP, _ip);
            arrList.set(DATA.COLNUM * _position + DATA.PASSWORD, _password);
            arrList.set(DATA.COLNUM * _position + DATA.PORT, _port);
            arrList.set(DATA.COLNUM * _position + DATA.STATE, "NONE");
            arrList.set(DATA.COLNUM * _position + DATA.RETVALUE, "Return Value");
            arrList.set(DATA.COLNUM * _position + DATA.KEYWORD, _keyword);
            arrList.set(DATA.COLNUM * _position + DATA.BACKGROUND_ONFF, _backGroundOnOff);
            adapter.notifyDataSetChanged();
            mlv.listViewUpdate(adapter,arrList);
        }

    }
    public void onFinishDialogDoDelete(int _position){
        dbhelper.delete(Integer.parseInt(arrList.get(DATA.COLNUM * _position + DATA.ARRNUM)));
                for (int i = 0; i < DATA.COLNUM; i++) {
                    arrList.remove(DATA.COLNUM * _position);
                }
                adapter.notifyDataSetChanged();
                mlv.listViewUpdate(adapter,arrList);
//                alertDialog.cancel();
        Toast.makeText(this, " 삭제", Toast.LENGTH_LONG).show();
    }


    public void onTouchListView(int _position,String _tag,String _userName,String _password,String _ip,String _port,String _keyword,String _returnValue,String _backGroundInterval,String _backGroundOnOff){
        showDialogForView( _position , _tag, _userName, _password, _ip, _port, _keyword,_returnValue,_backGroundInterval, _backGroundOnOff);
    }

    public void onFinishDialogDoAdd(String _tag,String _userName,String _password,String _ip,String _port,String _keyword,String _backGroundOnOff){
        long cnt = dbhelper.insert(_tag,_userName,_ip,_port,_password,_keyword,_backGroundOnOff);
        if(cnt < 0){
            Toast.makeText(this, "실패!", Toast.LENGTH_LONG).show();
        }else{
//            Toast.makeText(this, "cnt : "+cnt+"추가 성공 db : "+ dbhelper.getDatabaseName(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "완료", Toast.LENGTH_LONG).show();

            arrList.add(Integer.toString(dbhelper.getArrNumOfLastRow()));
            arrList.add(_tag);
            arrList.add(_userName);
            arrList.add(_ip);
            arrList.add(_password);
            arrList.add(_port);
            arrList.add("NONE");
            arrList.add("Return Value");
            arrList.add(_keyword);
            arrList.add("0"); //_backGroundInterval
            arrList.add(_backGroundOnOff);
            adapter.notifyDataSetChanged();
            mlv.listViewUpdate(adapter,arrList);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
//        onCreateDialog(DATA.MY_DIALOG);
        super.onCreate(savedInstanceState);


        setTitle("제어기 목록");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ///test>>
//            if(savedInstanceState==null){
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.mainLayout, new FragmentSecond())
//                        .commit();
//            }
        ///<<test



        initViewControls();
        /**DBHelper **/
        dbhelper = new DBHelper(MainActivity.this);
        adapter = new ListViewAdapter();

        arrList =dbhelper.getAllList();
        if(arrList.isEmpty()){
            //LIST EMPTY OR DB READ ERROR
        }
        /**List View >>**/
        mlv = new MyListView( (ListView)findViewById(R.id.listview1) ,adapter, dbhelper , getApplicationContext(),arrList,this );






        /**Add Host Fab button**/

        /**<< Add host Dialog, addButton**/
        adapter.notifyDataSetChanged();
        //listViewUpdate(adapter);

        /**Send Button >>**/
        FloatingActionButton fab_send = findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Sender(adapter, mlv).sendProcessTask();

//                Toast.makeText(getApplicationContext(), "전송", Toast.LENGTH_LONG).show();
//                for(int i=0;i<arrList.size()/DATA.COLNUM;i++) {
//                    arrList.set(DATA.COLNUM * i + DATA.STATE,"...");
//                    adapter.notifyDataSetChanged();
//                    mlv.listViewUpdate(adapter,arrList);
//                    try{
//                        final int tmp = i ;
//                        new AsyncJob.AsyncJobBuilder<Boolean>(tmp)
//                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
//                                    String id = (String) arrList.get(DATA.COLNUM * tmp+DATA.ID);
//                                    String ip = (String) arrList.get(DATA.COLNUM * tmp + DATA.IP);
//                                    String pw = (String) arrList.get(DATA.COLNUM * tmp + DATA.PASSWORD);
//                                    String port = (String) arrList.get(DATA.COLNUM * tmp + DATA.PORT);
//                                    String keyword = (String) arrList.get(DATA.COLNUM * tmp + DATA.KEYWORD);
//                                    String retVal;
//
//                                    @Override
//                                    public Boolean doAsync() {
//                                        // Do some background work
//                                        try {
//                                            retVal = send(id, ip, pw, port,keyword);
//                                            arrList.set(DATA.COLNUM * tmp + DATA.RETVALUE, retVal);
//                                         }
//                                        catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        try {
//                                            Thread.sleep(1000);
//                                        }
//                                        catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                        return true;
//                                    }
//                                })
//                                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
//                                    @Override
//                                    public void onResult(Boolean result) {
//                                        String retVal = (String) arrList.get(DATA.COLNUM * tmp + DATA.RETVALUE);
//                                        retVal = retVal.trim();
//                                        if(retVal.equals("ERR_UNVALID_ADDRESS")){
//                                            //arrList.set(COLNUM*tmp+STATE,"UNVALID ADDRESS");
//                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                        }
//                                        else if(retVal.equals("ERR_CHECK PORT")) {
//                                            //arrList.set(COLNUM*tmp+STATE,"CHECK PORT");
//                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                        }
//                                        else if(retVal.equals("ERR_CHECK WIFI OR SERVER")) {
//                                            //arrList.set(COLNUM*tmp+STATE,"CHECK NETWORK");
//                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                        }
//                                        else if(retVal.length() > 0){
//                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"CONNECTED");
//                                            arrList.set(DATA.COLNUM*tmp+DATA.RETVALUE,"SUCCESS");
//                                        }
//                                        else{
//                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                        }
//                                        adapter.notifyDataSetChanged();
//                                        mlv.listViewUpdate(adapter,arrList);
//
//
//                                    }
//                                }).create().start();
//
//                    }
//                    catch(RuntimeException e) {
//                            e.printStackTrace();
//                    }
//                } //for

            }
            });

        /**<<send Button**/

    }






    /**Menu >> **/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

         if (id == R.id.action_add) {
//            Toast.makeText(getApplicationContext(), "adding_mainAct ", Toast.LENGTH_LONG).show();
            showDialogForAdd();
            return true;
        }
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "settings ", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** << Menu**/





    private void initViewControls(){
        zemock = (TextView)findViewById(R.id.zemock);
        zemock.setText("장치 목록");

    }

//    String send(String hostname, String ip, String pw, String port,String keyword)throws IOException {
//        Handler handler = new Handler(Looper.getMainLooper());/**for toast **/
//
//        try{
//            JSch jsch = new JSch();
//            if(ip.length() < 1 || hostname.length()<1 || pw.length()<1){
//                return "ERR_UNVALID_ADDRESS";
//            }
//            Session session = jsch.getSession(hostname, ip, Integer.parseInt(port));
//            session.setPassword(pw);
//            //Session session = jsch.getSession("manager0000", "192.168.10.24", 22);
//            //session.setPassword("0000");
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setTimeout(10000);
//            session.connect();
//            //SSH Channel
//            ChannelExec channel = (ChannelExec)session.openChannel("exec");
//            //StringBuilder sb = new StringBuilder("ps -ef | grep \"GCGManager\" | grep -v 'grep' | awk '{print $8}'");
//            //StringBuilder sb = new StringBuilder("ps -ef");
//            //StringBuilder sb = new StringBuilder("ps -ef | grep \"NodeManager\" | grep -v 'grep' | awk '{print $8}'");
//            StringBuilder sb = new StringBuilder("ps -ef | grep \""+keyword+"\" | grep -v 'grep' | awk '{print $8}'");
//            channel.setCommand(sb.toString());
//
//            //channel.setCommand("pwd");
//            //channel.setCommand("ps -ef"); //ps -ef | grep "GCGManager" | grep -v 'grep' | awk '{print $8}");//your ssh command here
//            //channel.setCommand("ls /home/manager0000/");
//            InputStream output = channel.getInputStream();
//            channel.connect();
//            //byte[] tmp = new byte[1024];
//            //output.read(tmp,0,1024);
//            byte[] tmp = new byte[1024];
//            output.read(tmp,0,1024);
//            String res = new String(tmp);
//            channel.disconnect();
//
//            return res;
//        }
//        catch(final JSchException e){
//            return "ERR_CHECK WIFI OR SERVER";
//        }
//        catch(NumberFormatException e) {
//            return "ERR_CHECK PORT";
//        }
//
//    }

    void showDialogForAdd(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MyAlertDialog newFragment = MyAlertDialog.newInstance(0,DIALOG_TYPE_ADD,null,null,null,null,null,null,null,null,null);
        newFragment.show(ft, "dialog");

    }
    void showDialogForView(int _position,String _tag,String _userName,String _password,String _ip,String _port,String _keyword,String _returnValue,String _backGroundInterval,String _backGroundOnOff){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MyAlertDialog newFragment = MyAlertDialog.newInstance(_position,DIALOG_TYPE_SHOW,_tag, _userName, _password, _ip, _port, _keyword,_returnValue,_backGroundInterval,_backGroundOnOff);
        newFragment.show(ft, "dialog");
    }


}

