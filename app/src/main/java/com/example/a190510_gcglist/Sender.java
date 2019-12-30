package com.example.a190510_gcglist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

import static com.example.a190510_gcglist.MainActivity.arrList;

public class Sender {
    ListViewAdapter adapter;
    MyListView mlv;
    Context m_mainActivity;
    View.OnClickListener m_listnerInMainActivity;

    DBHelper m_dbHelper;
    int cnt =0;
    int m_cnt = 0;
    int m_cntForFailedLists = 0;
    public Sender(ListViewAdapter adapter,MyListView mlv,Context _mainActivity){
        m_mainActivity = _mainActivity;
        this.adapter = adapter;
        this.mlv = mlv;
    }

    public Sender(ListViewAdapter adapter, MyListView mlv, View.OnClickListener _listnerInMainActivity) {
        m_listnerInMainActivity = _listnerInMainActivity;
        this.adapter = adapter;
        this.mlv = mlv;
    }


    public interface SenderListener{
        void updateListView();
    }

    void sendProcessTask(){
        final int numOfLists = arrList.size()/DATA.COLNUM;
        for(int i=0;i<numOfLists;i++) {
            arrList.set(DATA.COLNUM * i + DATA.STATE,"...");
            adapter.notifyDataSetChanged(); // "..." 표시를 위해서 필요함
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
                                setReturnValueInArr(retVal,arrList,tmp);

//                                if(retVal.equals("ERR_UNVALID_ADDRESS")){
//                                    //arrList.set(COLNUM*tmp+STATE,"UNVALID ADDRESS");
//                                    arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                }
//                                else if(retVal.equals("ERR_CHECK PORT")) {
//                                    //arrList.set(COLNUM*tmp+STATE,"CHECK PORT");
//                                    arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                }
//                                else if(retVal.equals("ERR_CHECK WIFI OR SERVER")) {
//                                    //arrList.set(COLNUM*tmp+STATE,"CHECK NETWORK");
//                                    arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                }
//                                else if(retVal.length() > 0){
//                                    arrList.set(DATA.COLNUM*tmp+DATA.STATE,"CONNECTED");
//                                    arrList.set(DATA.COLNUM*tmp+DATA.RETVALUE,"SUCCESS");
//                                }
//                                else{
//                                    arrList.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
//                                }

//                                cnt++;
//                                if(cnt==numOfLists){ //모두 다 끝나면 업데이트 하라
                                    adapter.notifyDataSetChanged();
//                                    mlv.listViewUpdate(adapter,arrList);
                                    mlv.listViewUpdateForState(adapter,arrList);
//                                }

                            }
                        }).create().start();

            }
            catch(RuntimeException e) {
                e.printStackTrace();
            }
        } //for
    }


    public void sendProcessForBackGroundTask(){
        int numOfLists = arrList.size()/ DATA.COLNUM;
        for (int i = 0; i < numOfLists ; i++) {
            final int tmp = i;
            String id = (String) arrList.get(DATA.COLNUM * tmp + DATA.ID);
            String ip = (String) arrList.get(DATA.COLNUM * tmp + DATA.IP);
            String pw = (String) arrList.get(DATA.COLNUM * tmp + DATA.PASSWORD);
            String port = (String) arrList.get(DATA.COLNUM * tmp + DATA.PORT);
            String keyword = (String) arrList.get(DATA.COLNUM * tmp + DATA.KEYWORD);
            String retVal;

            arrList.set(DATA.COLNUM * i + DATA.STATE, "...");
//            adapter.notifyDataSetChanged();
//            mlv.listViewUpdate(adapter, arrList);
            try {
                retVal = send(id, ip, pw, port, keyword);
                arrList.set(DATA.COLNUM * tmp + DATA.RETVALUE, retVal);
            }catch (IOException e) {
                e.printStackTrace();
            }


            retVal = (String) arrList.get(DATA.COLNUM * tmp + DATA.RETVALUE);
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


            cnt++;
            if(cnt==numOfLists){ //모두 다 끝나면 업데이트 하라
                adapter.notifyDataSetChanged();
                mlv.listViewUpdateOnBackGrond(adapter,arrList);
                cnt=0;
            }



        }
    }






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
//            session.setTimeout(10000);
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

    private void setReturnValueInArr(String retVal, ArrayList<String> _arrList_,int tmp){
        if(retVal.equals("ERR_UNVALID_ADDRESS")){
            //arrList.set(COLNUM*tmp+STATE,"UNVALID ADDRESS");
            _arrList_.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
        }
        else if(retVal.equals("ERR_CHECK PORT")) {
            //arrList.set(COLNUM*tmp+STATE,"CHECK PORT");
            _arrList_.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
        }
        else if(retVal.equals("ERR_CHECK WIFI OR SERVER")) {
            //arrList.set(COLNUM*tmp+STATE,"CHECK NETWORK");
            _arrList_.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
        }
        else if(retVal.length() > 0){
            _arrList_.set(DATA.COLNUM*tmp+DATA.STATE,"CONNECTED");
            _arrList_.set(DATA.COLNUM*tmp+DATA.RETVALUE,"SUCCESS");
        }
        else{
            _arrList_.set(DATA.COLNUM*tmp+DATA.STATE,"FAIL");
        }
    }





    void sendProcessTaskForBackGround(){

        Log.i("Noti"," Called sendProcessTaskForBackGround ");
        final int numOfLists = arrList.size()/DATA.COLNUM;
        for(int i=0;i<numOfLists;i++) {
            arrList.set(DATA.COLNUM * i + DATA.STATE,"...");
//            adapter.notifyDataSetChanged(); //필요 없지 않나? 일단 백그라운드에서는 null
//            mlv.listViewUpdate(adapter,arrList); //이것도 필요없을 것 같
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
                                m_cnt ++;
                                String retVal = (String) arrList.get(DATA.COLNUM * tmp + DATA.RETVALUE);
                                retVal = retVal.trim();
                                setReturnValueInArr(retVal,arrList,tmp);

                                if(m_cnt == numOfLists){
                                    for(int i=0;i<numOfLists;i++) {
                                        m_dbHelper = new DBHelper(m_mainActivity);
                                        String arrNumStr = arrList.get(DATA.COLNUM*i + DATA.ARRNUM);
                                        int arrNum = Integer.parseInt(arrNumStr);
                                        String s = arrList.get(DATA.COLNUM * i + DATA.STATE);
                                        m_dbHelper.updateState(arrNum ,s);

                                        if(s.equals("FAIL"))
                                        {
                                            if(arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_ONFF).equals("t")) //알림 ON
                                            {
                                                m_cntForFailedLists++;
                                            }
                                        }
                                    }
//                                    SenderListener listnerInMainActivity = (SenderListener)m_listnerInMainActivity;
//                                    listnerInMainActivity.updateListView();
                                    if(m_cntForFailedLists > 0)
                                        sendNotificationIfFailed("제어기 실행 알림",m_cntForFailedLists+"개 전송 실패" );
                                    else{
                                        sendNotificationIfFailed("제어기 실행 알림", "모든 제어기가 실행 중입니다(알림 OFF 제외)");
                                    }
                                }

                            }
                        }).create().start();

            }
            catch(RuntimeException e) {
                e.printStackTrace();
            }
        } //for
    }


    public void sendNotificationIfFailed(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) m_mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(m_mainActivity, "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }












}
