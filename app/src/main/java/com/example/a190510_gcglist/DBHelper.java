package com.example.a190510_gcglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBTABLENAME = "GCGS123";
    private DBHelper helper;
    private SQLiteDatabase m_db;

    public DBHelper(Context context){
        super(context,"gcglists.db",null,1);
        m_db = getWritableDatabase();
        onCreate(m_db);
        m_db.close();
    }

    public void onCreate(SQLiteDatabase db ){
        m_db = db;

        String table = "CREATE TABLE IF NOT EXISTS "+DBTABLENAME
                +" (_arrnum INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"tag TEXT,"
                +"id TEXT,"
                +"ip TEXT NOT NULL,"
                +"port TEXT NOT NULL,"
                +"pw TEXT,"
                +"state TEXT,"
                +"retVal TEXT,"
                +"keyword TEXT)";
        m_db.execSQL(table);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ DBTABLENAME ) ;
        onCreate(db);
    }
    public long insert(String tag,String id,String ip, String port,String pw,String keyword){
        m_db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tag",tag);
        values.put("id",id);
        values.put("ip",ip);
        values.put("pw",pw);
        //if(port.length()<1) values.put("port","22");
        values.put("port",port);
        values.put("state","NONE");
        values.put("keyword",keyword);

        long res = m_db.insert(DBTABLENAME,null,values  );
        m_db.close();
        return res;
    }
    public void delete(int position){
        m_db =getWritableDatabase();
        m_db.execSQL("DELETE FROM "+DBTABLENAME+" WHERE _arrnum=" + position + ";");
        m_db.close();
    }
    public int update(int position,String tag,String id,String ip, String port,String pw,String keyword) {

        m_db =getWritableDatabase();

        m_db.execSQL("UPDATE " + DBTABLENAME + " SET tag='" + tag + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET id='" + id + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET ip='" + ip + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET port='" + port + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET pw='" + pw + "'" + " WHERE _arrnum=" + position + ";");
        //ContentValues values = new ContentValues();
        //values.put("keyword",keyword);
       // m_db.update(DBTABLENAME,values,"keyword=?",new String[]{keyword});
        try {
            m_db.execSQL("UPDATE " + DBTABLENAME + " SET keyword='" + keyword + "'" + " WHERE _arrnum=" + position + ";");
        }catch (RuntimeException e ){
            m_db.close();
            return -2;
        }


        if(select(position,0)==position){
            m_db.close();
            return position;
        }
        else{
            m_db.close();
            return -1;
        }

    }


    public int select(int arrnum,int col) throws SQLException{
        int ret=-1;
        m_db=getWritableDatabase();
        String selectQuery="SELECT * FROM "+DBTABLENAME+" WHERE _arrnum="+arrnum;
        Cursor cursor=m_db.rawQuery(selectQuery,null );
//        Cursor cursor = m_db.query(true, DBTABLENAME,new String[]{"_arrnum","id","ip","port"},
//                "_arrnum"+"="+arrnum,null,null,null,null,null);

        if(cursor != null){

            cursor.moveToFirst();
            ret = cursor.getInt(cursor.getColumnIndex("_arrnum"));

        }

        cursor.close();
        m_db.close();

        return ret;
    }
    public boolean isExistTable(){

        Cursor cursor = m_db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name=" + DBTABLENAME   ,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){return true;}
        else{return false;}

    }
    public int getArrNumOfLastRow(){
        int arrnum=0;
        m_db= getWritableDatabase();
        String selectQuery = "SELECT * FROM "+ DBTABLENAME + " ORDER BY _arrnum ASC";
        Cursor cursor = m_db.rawQuery(selectQuery, null);
        if(cursor.moveToLast()) {
            arrnum = cursor.getInt(cursor.getColumnIndex("_arrnum"));
        }
        else arrnum = -1;
        cursor.close();
        m_db.close();
        return arrnum;
    }
    public ArrayList<String> getAllList(){
        ArrayList<String> lists;// = new ArrayList<>();
        m_db= getWritableDatabase();
        String selectQuery = "SELECT * FROM "+ DBTABLENAME + " ORDER BY _arrnum ASC";
        Cursor cursor = m_db.rawQuery(selectQuery, null);
        try {

            lists = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    lists.add(Integer.toString(cursor.getInt(cursor.getColumnIndex("_arrnum"))));
                    lists.add(cursor.getString(cursor.getColumnIndex("tag")));
                    lists.add(cursor.getString(cursor.getColumnIndex("id")));
                    lists.add(cursor.getString(cursor.getColumnIndex("ip")));
                    lists.add(cursor.getString(cursor.getColumnIndex("pw")));
                    lists.add(cursor.getString(cursor.getColumnIndex("port")));
                    lists.add(cursor.getString(cursor.getColumnIndex("state")));
                    lists.add("EMPTY VALUE"); //retValue
                    lists.add(cursor.getString(cursor.getColumnIndex("keyword")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(java.lang.NullPointerException e){
            e.printStackTrace();
            lists = new ArrayList<>(); //Error
            //lists.add("Error");//
            return lists;
        }
        finally{
            cursor.close();
            m_db.close();
        }
        //m_db.close();
        return lists;
    }


}
