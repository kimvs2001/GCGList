package com.example.a190510_gcglist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MyListView  {//extends Activity {

    ListViewAdapter m_adapter;
    ListView m_listview;
    ArrayList<String> m_arrList;
    DBHelper m_dbHelper;
    MyAlertDialog m_mad;
    View m_view;
    Context m_context;
    initOnItemClickListener m_initOnItemClickListener;
    public MyListView(ListView _listview,ListViewAdapter _adapter, DBHelper _dbHelper, MyAlertDialog _mad, Context m_context,ArrayList<String> _arrList)  {
        m_adapter = _adapter;
        m_listview = _listview;
        m_dbHelper = _dbHelper;
        m_mad = _mad;
        m_arrList=_arrList;

        init();

    }


    public ListViewAdapter getAdapter() {
        return m_adapter;

    }

    private void init() {

        m_initOnItemClickListener = new initOnItemClickListener();
        m_listview.setAdapter(m_adapter);
        m_arrList = m_dbHelper.getAllList();
        if (m_arrList.isEmpty()) {
            //LIST EMPTY OR DB READ ERROR\
            int a = 99;
        }
        initView();

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
            m_adapter.addItem(_arrnum, tag, id, ip, pw, port, state, retVal, keyword);
        }
    }

    private class initOnItemClickListener implements AdapterView.OnItemClickListener
    {


        public void onItemClick (AdapterView < ? > parent, View view,int position, long id)
        {
            //final TextView titleTextView = (TextView) getParent().findViewById(R.id.textView1) ;
            ListViewItem listItem = (ListViewItem) m_listview.getItemAtPosition(position);
//            Toast.makeText(getApplicationContext(), "ArrNum : "+arrList.get(DATA.COLNUM * position+DATA.ARRNUM), Toast.LENGTH_LONG).show();
            String tmp_tag = m_arrList.get(DATA.COLNUM * position + DATA.TAG);
            String tmp_id = m_arrList.get(DATA.COLNUM * position + DATA.ID);
            String tmp_ip = m_arrList.get(DATA.COLNUM * position + DATA.IP);
            String tmp_pw = m_arrList.get(DATA.COLNUM * position + DATA.PASSWORD);
            String tmp_port = m_arrList.get(DATA.COLNUM * position + DATA.PORT);
            String tmp_keyword = m_arrList.get(DATA.COLNUM * position + DATA.KEYWORD);
            String tmp_retVal = m_arrList.get(DATA.COLNUM * position + DATA.RETVALUE);
            m_mad.onPrepareShowDialog(DATA.MY_DIALOG,  m_mad.getDialog(), tmp_tag, tmp_id, tmp_ip, tmp_pw, tmp_port, tmp_keyword, tmp_retVal, m_arrList);
//            m_mad.show();
        }
    }




    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_add) {
            Toast.makeText(m_context, "adding ", Toast.LENGTH_LONG).show();





            /**Add Host Fab button**/
            //FloatingActionButton fab = findViewById(R.id.fab);
            //fab.setOnClickListener(new View.OnClickListener() {
            //  @Override
            //  public void onClick(View view) {

            m_mad.onPrepareAddDialog();//DATA.MY_DIALOG,m_mad.getDialog() ,m_arrList);
//            m_mad.show();

            /**SaveData Button >> **/
            CONTROLS.saveDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = CONTROLS.tagEditText.getText().toString();
                    String userName = CONTROLS.userNameEditText.getText().toString();
                    String password = CONTROLS.passwordEditText.getText().toString();
                    String ip = CONTROLS.ipEditText.getText().toString();
                    String port = CONTROLS.portEditText.getText().toString();
                    String keyword = CONTROLS.keywordEditText.getText().toString();

                    long cnt = m_dbHelper.insert(tag,userName,ip,port,password,keyword);
                    if(cnt < 0){
                        Toast.makeText(m_context, "실 패", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(m_context, "cnt : "+cnt+"추가 성공 db : "+m_dbHelper.getDatabaseName(), Toast.LENGTH_LONG).show();

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
                        listViewUpdate(m_adapter,m_arrList);
                    }
                    m_mad.getDialog().cancel();

                }

            });
            /**Cancel Button>>***/
            CONTROLS.cancelDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m_mad.getDialog().cancel();
                }
            });
            // }

            //});
            /**<< Add host Dialog, addButton**/
            m_adapter.notifyDataSetChanged();


            return true;
        }

        //return super.onOptionsItemSelected(item);
        return this.onOptionsItemSelected(item);
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

            adapter.addItem(_arrnum,tag,id, ip, pw,port,state,retVal,keyword);
        }


    }

















}