package com.example.a190510_gcglist;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MyListView {//extends Fragment {

    ListViewAdapter m_adapter;
    ListView m_listview;
    ArrayList<String> m_arrList;
    DBHelper m_dbHelper;
    MyAlertDialog m_mad;
    View m_view;
    Context m_context;
    //initOnItemClickListener m_initOnItemClickListener;
    MainActivity m_mainActivity;


//    CONTROLS m_controls;
     MyListView(ListView _listview, ListViewAdapter _adapter, DBHelper _dbHelper,  Context m_context, ArrayList<String> _arrList, MainActivity _mainActivity)  {
        m_adapter = _adapter;
        m_listview = _listview;
        m_dbHelper = _dbHelper;
//        m_mad = _mad;
        m_arrList=_arrList;
        m_mainActivity = _mainActivity;
//        m_controls = _controls;
        init();

    }
    public interface MyListViewListner{
         void onTouchListView(int _position,String _tag,String _username
                                ,String _password,String _id,String  _port
                                ,String _keyword, String _returnValue
                                ,String _backGroundInterval,String _backGroundOnOff
                                );
    }


    private void init() {

        //m_initOnItemClickListener = new initOnItemClickListener();
        m_listview.setAdapter(m_adapter);
        //m_arrList = m_dbHelper.getAllList(); delete 19.10.19
        if (m_arrList.isEmpty()) {
            //LIST EMPTY OR DB READ ERROR\

        }
        initView();
//        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        m_listview.setOnItemClickListener(new OnItemSingleClickListener(){
            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            public void onItemSingleClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                ListViewItem listItem = (ListViewItem) m_listview.getItemAtPosition(position);
//            Toast.makeText(getApplicationContext(), "ArrNum : "+arrList.get(DATA.COLNUM * position+DATA.ARRNUM), Toast.LENGTH_LONG).show();
                String tmp_tag = m_arrList.get(DATA.COLNUM * position + DATA.TAG);
                String tmp_id = m_arrList.get(DATA.COLNUM * position + DATA.ID);
                String tmp_ip = m_arrList.get(DATA.COLNUM * position + DATA.IP);
                String tmp_pw = m_arrList.get(DATA.COLNUM * position + DATA.PASSWORD);
                String tmp_port = m_arrList.get(DATA.COLNUM * position + DATA.PORT);
                String tmp_keyword = m_arrList.get(DATA.COLNUM * position + DATA.KEYWORD);
                String tmp_retVal = m_arrList.get(DATA.COLNUM * position + DATA.RETVALUE);
                String tmp_backGroundInterval = m_arrList.get(DATA.COLNUM * position + DATA.BACKGROUND_INTERVAL);
                String tmp_backGroundOnOff = m_arrList.get(DATA.COLNUM * position + DATA.BACKGROUND_ONFF);
//                m_mainActivity.showDialog();


                MyListViewListner listnerInMainActivity = (MyListViewListner)m_mainActivity;
                listnerInMainActivity.onTouchListView(position,tmp_tag,tmp_id,tmp_pw,tmp_ip,tmp_port,tmp_keyword,tmp_retVal,tmp_backGroundInterval,tmp_backGroundOnOff);



            }
        });

    }




    private void initView() {
        for (int i = 0; i < m_arrList.size() / DATA.COLNUM; i++) {
            String _arrnum = (String) m_arrList.get(DATA.COLNUM * i + DATA.ARRNUM);
            String tag = (String) m_arrList.get(DATA.COLNUM * i + DATA.TAG);
            String id = (String) m_arrList.get(DATA.COLNUM * i + DATA.ID);
            String ip = (String) m_arrList.get(DATA.COLNUM * i + DATA.IP);
            String pw = (String) m_arrList.get(DATA.COLNUM * i + DATA.PASSWORD);
            String port = (String) m_arrList.get(DATA.COLNUM * i + DATA.PORT);
            String state = (String) m_arrList.get(DATA.COLNUM * i + DATA.STATE);
            String retVal = (String) m_arrList.get(DATA.COLNUM * i + DATA.RETVALUE);
            String keyword = (String) m_arrList.get(DATA.COLNUM * i + DATA.KEYWORD);
            String backGroundInterval = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_INTERVAL);
            String backGroundOnOff = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_ONFF);
            m_adapter.addItem(_arrnum, tag, id, ip, pw, port, state, retVal, keyword,backGroundInterval,backGroundOnOff);
        }
    }
//    private class initOnItemClickListener implements AdapterView.OnItemClickListener
//    {
//
//
//        public void onItemClick (AdapterView < ? > parent, View view,int position, long id)
//        {
//            //final TextView titleTextView = (TextView) getParent().findViewById(R.id.textView1) ;
//            ListViewItem listItem = (ListViewItem) m_listview.getItemAtPosition(position);
////            Toast.makeText(getApplicationContext(), "ArrNum : "+arrList.get(DATA.COLNUM * position+DATA.ARRNUM), Toast.LENGTH_LONG).show();
//            String tmp_tag = m_arrList.get(DATA.COLNUM * position + DATA.TAG);
//            String tmp_id = m_arrList.get(DATA.COLNUM * position + DATA.ID);
//            String tmp_ip = m_arrList.get(DATA.COLNUM * position + DATA.IP);
//            String tmp_pw = m_arrList.get(DATA.COLNUM * position + DATA.PASSWORD);
//            String tmp_port = m_arrList.get(DATA.COLNUM * position + DATA.PORT);
//            String tmp_keyword = m_arrList.get(DATA.COLNUM * position + DATA.KEYWORD);
//            String tmp_retVal = m_arrList.get(DATA.COLNUM * position + DATA.RETVALUE);
////            m_mad.onPrepareShowDialog(DATA.MY_DIALOG,  m_mad.getDialog(), tmp_tag, tmp_id, tmp_ip, tmp_pw, tmp_port, tmp_keyword, tmp_retVal, m_arrList);
////            m_mad.onPrepareShowDialog(DATA.MY_DIALOG,  tmp_tag, tmp_id, tmp_ip, tmp_pw, tmp_port, tmp_keyword, tmp_retVal, m_arrList,m_mainActivity);
//
//
//            // Create and show the dialog.
//
//
//
//        }
//    }




//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//        if (id == R.id.action_add) {
//            Toast.makeText(m_context, "adding_mlv ", Toast.LENGTH_LONG).show();
//
//



            /**Add Host Fab button**/
            //FloatingActionButton fab = findViewById(R.id.fab);
            //fab.setOnClickListener(new View.OnClickListener() {
            //  @Override
            //  public void onClick(View view) {

//            m_mad.onPrepareAddDialog(m_mainActivity);//DATA.MY_DIALOG,m_mad.getDialog() ,m_arrList);
//            m_mad.show();

            /**SaveData Button >> **/
//            saveDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String tag = m_controls.tagEditText.getText().toString();
//                    String userName = m_controls.userNameEditText.getText().toString();
//                    String password = m_controls.passwordEditText.getText().toString();
//                    String ip = m_controls.ipEditText.getText().toString();
//                    String port = m_controls.portEditText.getText().toString();
//                    String keyword = m_controls.keywordEditText.getText().toString();
//
//                    long cnt = m_dbHelper.insert(tag,userName,ip,port,password,keyword);
//                    if(cnt < 0){
//                        Toast.makeText(m_context, "실 패", Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(m_context, "cnt : "+cnt+"추가 성공 db : "+m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();
//
//                        m_arrList.add(Integer.toString(m_dbHelper.getArrNumOfLastRow()));
//                        m_arrList.add(tag);
//                        m_arrList.add(userName);
//                        m_arrList.add(ip);
//                        m_arrList.add(password);
//                        m_arrList.add(port);
//                        m_arrList.add("NONE");
//                        m_arrList.add("Return Value");
//                        m_arrList.add(keyword);
//                        m_adapter.notifyDataSetChanged();
//                        listViewUpdate(m_adapter,m_arrList);
//                    }
//                    m_mad.getDialog().cancel();
//
//                }
//
//            });
            /**Cancel Button>>***/
//            m_controls.cancelDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    m_mad.getDialog().cancel();
//                }
//            });
            // }

            //});
            /**<< Add host Dialog, addButton**/
//            m_adapter.notifyDataSetChanged();
//
//
//            return true;
//        }
//
//        //return super.onOptionsItemSelected(item);
//        return this.onOptionsItemSelected(item);
//    }

    public void listViewUpdateForState(ListViewAdapter adapter,ArrayList _arr) {
        adapter.cleareView();
        m_arrList = _arr;
        for (int i = 0; i < m_arrList.size() / DATA.COLNUM; i++) {
            String _arrnum = (String) m_arrList.get(DATA.COLNUM * i+DATA.ARRNUM);
            String tag = (String) m_arrList.get(DATA.COLNUM * i+DATA.TAG);
            String id = (String) m_arrList.get(DATA.COLNUM * i+DATA.ID);
            String ip = (String) m_arrList.get(DATA.COLNUM * i + DATA.IP);
            String pw = (String) m_arrList.get(DATA.COLNUM * i + DATA.PASSWORD);
            String port = (String) m_arrList.get(DATA.COLNUM * i + DATA.PORT);
            String state = (String) m_arrList.get(DATA.COLNUM * i + DATA.STATE);
            String retVal = (String) m_arrList.get(DATA.COLNUM * i + DATA.RETVALUE);
            String keyword = (String) m_arrList.get(DATA.COLNUM * i + DATA.KEYWORD);
            String backGroundInterval = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_INTERVAL);
            String backGroundOnOff = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_ONFF);
            m_adapter.addItem(_arrnum, tag, id, ip, pw, port, state, retVal, keyword,backGroundInterval,backGroundOnOff);
            m_dbHelper.updateState(Integer.parseInt(_arrnum),state);
        }

    }


    public void listViewUpdate(ListViewAdapter adapter,ArrayList _arr) {
        adapter.cleareView();
        m_arrList = _arr;
        for (int i = 0; i < m_arrList.size() / DATA.COLNUM; i++) {
            String _arrnum = (String) m_arrList.get(DATA.COLNUM * i+DATA.ARRNUM);
            String tag = (String) m_arrList.get(DATA.COLNUM * i+DATA.TAG);
            String id = (String) m_arrList.get(DATA.COLNUM * i+DATA.ID);
            String ip = (String) m_arrList.get(DATA.COLNUM * i + DATA.IP);
            String pw = (String) m_arrList.get(DATA.COLNUM * i + DATA.PASSWORD);
            String port = (String) m_arrList.get(DATA.COLNUM * i + DATA.PORT);
            String state = (String) m_arrList.get(DATA.COLNUM * i + DATA.STATE);
            String retVal = (String) m_arrList.get(DATA.COLNUM * i + DATA.RETVALUE);
            String keyword = (String) m_arrList.get(DATA.COLNUM * i + DATA.KEYWORD);
            String backGroundInterval = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_INTERVAL);
            String backGroundOnOff = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_ONFF);
            m_adapter.addItem(_arrnum, tag, id, ip, pw, port, state, retVal, keyword,backGroundInterval,backGroundOnOff);
        }
    }
    public void listViewUpdateOnBackGrond(ListViewAdapter adapter,ArrayList _arr) {
        adapter.cleareView();
        m_arrList = _arr;
        for (int i = 0; i < m_arrList.size() / DATA.COLNUM; i++) {
            String _arrnum = (String) m_arrList.get(DATA.COLNUM * i + DATA.ARRNUM);
            String tag = (String) m_arrList.get(DATA.COLNUM * i + DATA.TAG);
            String id = (String) m_arrList.get(DATA.COLNUM * i + DATA.ID);
            String ip = (String) m_arrList.get(DATA.COLNUM * i + DATA.IP);
            String pw = (String) m_arrList.get(DATA.COLNUM * i + DATA.PASSWORD);
            String port = (String) m_arrList.get(DATA.COLNUM * i + DATA.PORT);
            String state = (String) m_arrList.get(DATA.COLNUM * i + DATA.STATE);
            String retVal = (String) m_arrList.get(DATA.COLNUM * i + DATA.RETVALUE);
            String keyword = (String) m_arrList.get(DATA.COLNUM * i + DATA.KEYWORD);
            String backGroundInterval = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_INTERVAL);
            String backGroundOnOff = (String) m_arrList.get(DATA.COLNUM * i + DATA.BACKGROUND_ONFF);
            m_adapter.addItem(_arrnum, tag, id, ip, pw, port, state, retVal, keyword, backGroundInterval, backGroundOnOff);
            m_dbHelper.update(Integer.parseInt(_arrnum), tag, id, ip, port, pw, keyword, state, retVal, backGroundInterval, backGroundOnOff);
        }
    }

}