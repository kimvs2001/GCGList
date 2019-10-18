package com.example.a190510_gcglist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MyToolBar extends AppCompatActivity
{






    Toolbar m_toolBar;
    private Context m_context;

    public MyToolBar(Context _context, Toolbar _toolBar){

        m_context = _context;
//        m_acd = _acd;
        LayoutInflater layoutInflater = LayoutInflater.from(m_context);
        m_toolBar = _toolBar;




        //init();
    }

    public Toolbar getToolBar(){
        return m_toolBar;
    }
    
    private void init(){
        setSupportActionBar(m_toolBar);
    }

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
            Toast.makeText(m_context, "adding ", Toast.LENGTH_LONG).show();





            /**Add Host Fab button**/
            //FloatingActionButton fab = findViewById(R.id.fab);
            //fab.setOnClickListener(new View.OnClickListener() {
            //  @Override
            //  public void onClick(View view) {

//            m_mad.onPrepareAddDialog(DATA.MY_DIALOG,m_mad.getDialog() ,m_arrList);
//            m_mad.show();
//
//            /**SaveData Button >> **/
//            CONTROLS.saveDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String tag = CONTROLS.tagEditText.getText().toString();
//                    String userName = CONTROLS.userNameEditText.getText().toString();
//                    String password = CONTROLS.passwordEditText.getText().toString();
//                    String ip = CONTROLS.ipEditText.getText().toString();
//                    String port = CONTROLS.portEditText.getText().toString();
//                    String keyword = CONTROLS.keywordEditText.getText().toString();
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
//                        listViewUpdate(m_adapter);
//                    }
//                    m_mad.getDialog().cancel();
//
//                }
//
//            });
//            /**Cancel Button>>***/
//            CONTROLS.cancelDataButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    m_mad.getDialog().cancel();
//                }
//            });
//            // }
//
//            //});
//            /**<< Add host Dialog, addButton**/
//            m_adapter.notifyDataSetChanged();


            return true;
        }

        //return super.onOptionsItemSelected(item);
        return this.onOptionsItemSelected(item);
    }
}
