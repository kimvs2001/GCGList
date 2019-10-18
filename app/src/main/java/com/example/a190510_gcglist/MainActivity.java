package com.example.a190510_gcglist;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
//    public static int MY_DIALOG = 0;
//    public static final int COLNUM = 9;
//    public static final int _ARRNUM = 0;
//    public static final int TAG = 1;
//    public static final int ID = 2;
//    public static final int IP = 3;
//    public static final int PASSWORD = 4;
//    public static final int PORT = 5;
//    public static final int STATE = 6;
//    public static final int RETVALUE = 7;
//    public static final int KEYWORD = 8;


    /**Popup dialog >> */
//    private View popupInputDialogView = null;
//    private EditText tagEditText = null;
//    private EditText userNameEditText = null;
//    private EditText passwordEditText = null;
//    private EditText ipEditText = null;
//    private EditText portEditText = null;
//    private EditText keywordEditText = null;
//    private TextView retValTextView = null;
//    private TextView retValView=null;
//    private Button saveDataButton = null;
//    private Button cancelDataButton = null;
//    private StringBuilder keyword_gcg = new StringBuilder("ps -ef | grep \"GCGManager\" | grep -v 'grep' | awk '{print $8}'");
    /**DIalog ***/
    //Dialog
    AlertDialog alertDialog =null;

    //Dialog Button
    private Button modifyDataButton = null;
    private Button deleteDataButton = null;
    private TextView zemock = null;
    /** DIalog ***/




    /**<< Popup dialog*/
    /**DB>>**/
    DBHelper dbhelper ;
    /**<<DB**/
    /** List View ,Adapter>>**/
    ListView listview;
    ListViewAdapter adapter;
    /**<< List View**/
    /**memory ListView >> **/
    static ArrayList<String> arrList;
    /**<< memory ListView**/



    MyAlertDialog mad;
    MyListView mlv;
    CONTROLS controls;

    /******************************
        TEST IP ADDRESS
     cfamr.iptime.org
     8001
     gcg
     rala
     ps -ef | grep "GCGManager" | grep -v 'grep' | awk '{print $8}'
    ******************************/

    /*************************************
    Back Ground
    /**************************************/
    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreateDialog(DATA.MY_DIALOG);

        super.onCreate(savedInstanceState);

        //mBackgroundService = new BackgroundService(getApplicationContext());

        setTitle("제어기 목록");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initViewControls();
        /**DBHelper **/
        dbhelper = new DBHelper(MainActivity.this);
        adapter = new ListViewAdapter();

        arrList =dbhelper.getAllList();
        if(arrList.isEmpty()){
            //LIST EMPTY OR DB READ ERROR
        }
        /**List View >>**/
        controls = new CONTROLS(getApplicationContext());
        mlv = new MyListView( (ListView)findViewById(R.id.listview1) ,adapter, dbhelper ,mad , getApplicationContext(),arrList);
        mad = MyAlertDialog.newInstance(dbhelper,adapter,mlv,arrList);


//        adapter= new ListViewAdapter();
//        listview = (ListView)findViewById(R.id.listview1);
//        listview.setAdapter(adapter);

//        initView();
        /******Dialog >>********/
        //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        //alertDialogBuilder.setTitle("User Data Collection Dialog.");
        //alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        //alertDialogBuilder.setCancelable(false);
        //initPopupViewControls();
        //alertDialogBuilder.setView(popupInputDialogView);
        //final AlertDialog alertDialog = alertDialogBuilder.create();

        //final AlertDialog alertDialog = (AlertDialog)onCreateDialog(MY_DIALOG);
        // //alertDialog = (AlertDialog)onCreateDialog(DATA.MY_DIALOG);
        /*******<< Dialog******/

        /**listview touch listner >>**/

        /**<< listview touch listener**/




        /**Add Host Fab button**/

        /**<< Add host Dialog, addButton**/
        adapter.notifyDataSetChanged();
        //listViewUpdate(adapter);

        /**Send Button >>**/
        FloatingActionButton fab_send = findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(getApplicationContext(), "Send Fab Button ", Toast.LENGTH_LONG).show();
                for(int i=0;i<arrList.size()/DATA.COLNUM;i++) {
                    arrList.set(DATA.COLNUM * i + DATA.STATE,"...");
                    adapter.notifyDataSetChanged();
                    mlv.listViewUpdate(adapter,arrList);
                    try{
                        final int tmp = i ;
                        new AsyncJob.AsyncJobBuilder<Boolean>(tmp)
                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                    String id = (String) arrList.get(DATA.COLNUM * tmp+DATA.ID);
                                    String ip = (String) arrList.get(DATA.COLNUM * tmp + DATA.IP);
                                    String pw = (String) arrList.get(DATA.COLNUM * tmp + DATA.PASSWORD);
                                    String port = (String) arrList.get(DATA.COLNUM * tmp + DATA.PORT);
                                    String keyword = (String) arrList.get(DATA.COLNUM * tmp + DATA.KEYWORD);
                                    String retVal;

                                    @Override
                                    public Boolean doAsync() {
                                        // Do some background work
                                        try {
                                            retVal = send(id, ip, pw, port,keyword);
                                            arrList.set(DATA.COLNUM * tmp + DATA.RETVALUE, retVal);
                                         }
                                        catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Thread.sleep(1000);
                                        }
                                        catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    }
                                })
                                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                    @Override
                                    public void onResult(Boolean result) {
                                        String retVal = (String) arrList.get(DATA.COLNUM * tmp + DATA.RETVALUE);
                                        retVal = retVal.trim();
                                        if(retVal.equals("ERR_UNVALID_ADDRESS")){
                                            //arrList.set(COLNUM*tmp+STATE,"UNVALID ADDRESS");
                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
                                        }
                                        else if(retVal.equals("ERR_CHECK PORT")) {
                                            //arrList.set(COLNUM*tmp+STATE,"CHECK PORT");
                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
                                        }
                                        else if(retVal.equals("ERR_CHECK WIFI OR SERVER")) {
                                            //arrList.set(COLNUM*tmp+STATE,"CHECK NETWORK");
                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
                                        }
                                        else if(retVal.length() > 0){
                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"CONNECTED");
                                            arrList.set(DATA.COLNUM*tmp+DATA.RETVALUE,"SUCCESS");
                                        }
                                        else{
                                            arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
                                        }
                                        adapter.notifyDataSetChanged();
                                        mlv.listViewUpdate(adapter,arrList);


                                    }
                                }).create().start();

                    }
                    catch(RuntimeException e) {
                            e.printStackTrace();
                    }
                } //for
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
            Toast.makeText(getApplicationContext(), "adding ", Toast.LENGTH_LONG).show();
//            mad.onPrepareAddDialog();
            mad.show(getSupportFragmentManager(), "dialog");




             /**Add Host Fab button**/
             //FloatingActionButton fab = findViewById(R.id.fab);
             //fab.setOnClickListener(new View.OnClickListener() {
               //  @Override
               //  public void onClick(View view) {

//                     mad.onPrepareAddDialog(DATA.MY_DIALOG,alertDialog);
//                     alertDialog.show();
//
//                     /**SaveData Button >> **/
//                     saveDataButton.setOnClickListener(new View.OnClickListener() {
//                         @Override
//                         public void onClick(View view) {
//                             String tag = tagEditText.getText().toString();
//                             String userName = userNameEditText.getText().toString();
//                             String password = passwordEditText.getText().toString();
//                             String ip = ipEditText.getText().toString();
//                             String port = portEditText.getText().toString();
//                             String keyword = keywordEditText.getText().toString();
//
//                             long cnt = dbhelper.insert(tag,userName,ip,port,password,keyword);
//                             if(cnt < 0){
//                                 Toast.makeText(getApplicationContext(), "실 패", Toast.LENGTH_LONG).show();
//                             }else{
//                                 Toast.makeText(getApplicationContext(), "cnt : "+cnt+"추가 성공 db : "+dbhelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//
//                                 arrList.add(Integer.toString(dbhelper.getArrNumOfLastRow()));
//                                 arrList.add(tag);
//                                 arrList.add(userName);
//                                 arrList.add(ip);
//                                 arrList.add(password);
//                                 arrList.add(port);
//                                 arrList.add("NONE");
//                                 arrList.add("Return Value");
//                                 arrList.add(keyword);
//                                 adapter.notifyDataSetChanged();
//                                 listViewUpdate(adapter);
//                             }
//                             alertDialog.cancel();
//
//                         }
//
//                     });
//                     /**Cancel Button>>***/
//                     cancelDataButton.setOnClickListener(new View.OnClickListener() {
//                         @Override
//                         public void onClick(View view) {
//                             alertDialog.cancel();
//                         }
//                     });
//                // }
//
//             //});
//             /**<< Add host Dialog, addButton**/
//             adapter.notifyDataSetChanged();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** << Menu**/




//    void initView(){
////        for (int i = 0; i < arrList.size() / COLNUM; i++) {
////            String _arrnum = (String) arrList.get(COLNUM * i+_ARRNUM);
////            String tag = (String) arrList.get(COLNUM * i+TAG);
////            String id = (String) arrList.get(COLNUM * i+ID);
////            String ip = (String) arrList.get(COLNUM * i + IP);
////            String pw = (String) arrList.get(COLNUM * i + PASSWORD);
////            String port = (String) arrList.get(COLNUM * i + PORT);
////            String state = (String) arrList.get(COLNUM * i + STATE);
////            String retVal = (String) arrList.get(COLNUM * i + RETVALUE);
////            String keyword = (String) arrList.get(COLNUM * i + KEYWORD);
////            adapter.addItem(_arrnum,tag,id, ip, pw,port,state,retVal,keyword);
////        }
////    }
    //Add host information to ListView from DB
//    void listViewUpdate(ListViewAdapter adapter) {
//        adapter.cleareView();
//        for (int i = 0; i < arrList.size() / DATA.COLNUM; i++) {
//            String _arrnum = (String) arrList.get(DATA.COLNUM * i+DATA.ARRNUM);
//            String tag = (String) arrList.get(DATA.COLNUM * i+DATA.TAG);
//            String id = (String) arrList.get(DATA.COLNUM * i+DATA.ID);
//            String ip = (String) arrList.get(DATA.COLNUM * i + DATA.IP);
//            String pw = (String) arrList.get(DATA.COLNUM * i + DATA.PASSWORD);
//            String port = (String) arrList.get(DATA.COLNUM * i + DATA.PORT);
//            String state = (String) arrList.get(DATA.COLNUM * i + DATA.STATE);
//            String retVal = (String) arrList.get(DATA.COLNUM * i + DATA.RETVALUE);
//            String keyword = (String) arrList.get(DATA.COLNUM * i + DATA.KEYWORD);
//
//            adapter.addItem(_arrnum,tag,id, ip, pw,port,state,retVal,keyword);
//        }
//    }

    private void initViewControls(){
        zemock = (TextView)findViewById(R.id.zemock);
        zemock.setText("장치 목록");

    }
//    private void initPopupViewControls() {
//        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

//        popupInputDialogView = layoutInflater.inflate(R.layout.dialog, null);
//        // Get user input edittext and button ui controls in the popup dialog.
//        tagEditText = (EditText) popupInputDialogView.findViewById(R.id.tag);
//        userNameEditText = (EditText) popupInputDialogView.findViewById(R.id.userName);
//        passwordEditText = (EditText) popupInputDialogView.findViewById(R.id.password);
//        ipEditText = (EditText) popupInputDialogView.findViewById(R.id.ip);
//        portEditText = (EditText) popupInputDialogView.findViewById(R.id.port);
//        keywordEditText = (EditText) popupInputDialogView.findViewById(R.id.keyword);
//        retValTextView=(TextView)  popupInputDialogView.findViewById(R.id.retVal);
//        retValView=(TextView)  popupInputDialogView.findViewById(R.id.retValView);
//        saveDataButton = popupInputDialogView.findViewById(R.id.button_save_user_data);
//        cancelDataButton = popupInputDialogView.findViewById(R.id.button_cancel_user_data);
//        modifyDataButton = popupInputDialogView.findViewById(R.id.button_modify_user_data);
//        deleteDataButton = popupInputDialogView.findViewById(R.id.button_delete_user_data);



        // Display values from the main activity list view in user input edittext.
        //initEditTextUserDataInPopupDialog();
//    }

    String send(String hostname, String ip, String pw, String port,String keyword)throws IOException {
        Handler handler = new Handler(Looper.getMainLooper());/**for toast **/

        try{
            JSch jsch = new JSch();
            if(ip.length() < 1 || hostname.length()<1 || pw.length()<1){
                return "ERR_UNVALID_ADDRESS";
            }
            Session session = jsch.getSession(hostname, ip, Integer.parseInt(port));
            session.setPassword(pw);
            //Session session = jsch.getSession("manager0000", "192.168.10.24", 22);
            //session.setPassword("0000");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            //SSH Channel
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            //StringBuilder sb = new StringBuilder("ps -ef | grep \"GCGManager\" | grep -v 'grep' | awk '{print $8}'");
            //StringBuilder sb = new StringBuilder("ps -ef");
            //StringBuilder sb = new StringBuilder("ps -ef | grep \"NodeManager\" | grep -v 'grep' | awk '{print $8}'");
            StringBuilder sb = new StringBuilder("ps -ef | grep \""+keyword+"\" | grep -v 'grep' | awk '{print $8}'");
            channel.setCommand(sb.toString());

            //channel.setCommand("pwd");
            //channel.setCommand("ps -ef"); //ps -ef | grep "GCGManager" | grep -v 'grep' | awk '{print $8}");//your ssh command here
            //channel.setCommand("ls /home/manager0000/");
            InputStream output = channel.getInputStream();
            channel.connect();
            //byte[] tmp = new byte[1024];
            //output.read(tmp,0,1024);
            byte[] tmp = new byte[1024];
            output.read(tmp,0,1024);
            String res = new String(tmp);
            channel.disconnect();

            return res;
        }
        catch(final JSchException e){
            return "ERR_CHECK WIFI OR SERVER";
        }
        catch(NumberFormatException e) {
            return "ERR_CHECK PORT";
        }

    }
    public ArrayList<String> getArrList(){
        return arrList;
    }
//    protected Dialog onCreateDialog(int id){
//        return new AlertDialog.Builder(this)
//                .setCancelable(true)
//                .setView(popupInputDialogView)
//                .create();
//    }
//    protected void onInitPrepareDialog(int id, Dialog dialog) {
//        super.onPrepareDialog(id,dialog);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//    }
//
//    protected void onPrepareAddDialog(int id, Dialog dialog){
//        super.onPrepareDialog(id,dialog);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        userNameEditText.setText("");///
//        passwordEditText.setText("");
//        ipEditText.setText("");
//        portEditText.setText("8001");
//        tagEditText.setText("");
//        keywordEditText.setText("GCGManager");
//        retValTextView.setVisibility(View.GONE);
//        retValView.setVisibility(View.GONE);
//        saveDataButton.setVisibility(View.VISIBLE);
//        modifyDataButton.setVisibility(View.GONE);
//        deleteDataButton.setVisibility(View.GONE);
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//    }
//    protected void onPrepareShowDialog(int id, Dialog dialog,String host_tag,String host_id,String host_ip,String host_pw, String host_port, String keyword,String tmp_retVal){
//        super.onPrepareDialog(id,dialog);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        userNameEditText.setText(host_id);///
//        passwordEditText.setText(host_pw);
//        ipEditText.setText(host_ip);
//        portEditText.setText(host_port);
//        tagEditText.setText(host_tag);
//        keywordEditText.setText(keyword);
//        retValTextView.setText(tmp_retVal);
//        retValTextView.setMovementMethod(new ScrollingMovementMethod());
//        retValTextView.scrollTo(0,0);
//        retValTextView.setVisibility(View.VISIBLE);
//        retValView.setVisibility(View.VISIBLE);
//        modifyDataButton.setVisibility(View.VISIBLE);
//        deleteDataButton.setVisibility(View.VISIBLE);
//        saveDataButton.setVisibility(View.GONE);
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//    }
    /**>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>**/
    //test
//
//    public void setArrList(ArrayList<String> _arrList){
//        arrList = _arrList;
//        adapter.notifyDataSetChanged();
//        mlv.listViewUpdate(adapter);
//
//    }







    /**<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<**/






}

