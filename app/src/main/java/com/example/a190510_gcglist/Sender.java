package com.example.a190510_gcglist;

import android.os.Handler;
import android.os.Looper;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

import static com.example.a190510_gcglist.MainActivity.arrList;

public class Sender {
    ListViewAdapter adapter;
    MyListView mlv;
    int cnt =0;
    public Sender(ListViewAdapter adapter,MyListView mlv){
        this.adapter = adapter;
        this.mlv = mlv;

    }

    void sendProcessTask(){
        final int numOfLists = arrList.size()/DATA.COLNUM;
        for(int i=0;i<numOfLists;i++) {
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
//            adapter.notifyDataSetChanged();
//            mlv.listViewUpdateOnBackGrond(adapter,arrList);

            cnt++;
            if(cnt==numOfLists){ //모두 다 끝나면 업데이트 하라
                //db업데이트 필요
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





}
