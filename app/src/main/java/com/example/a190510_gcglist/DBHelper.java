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
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBTABLENAME = "GCGS123";

    public static final String SYSTEM_DBTABLENAME = "GCGS_SYSTEM_DB";
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
                +"keyword TEXT,"
                +"backGroundInterval TEXT,"
                +"backGroundOnOff TEXT)"
                ;


        String table_system = "CREATE TABLE IF NOT EXISTS "+SYSTEM_DBTABLENAME
                +" (_arrnum INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"BACKGROUND_ID TEXT)"
                ;




        m_db.execSQL(table);
        m_db.execSQL(table_system);

    }

    public int updateBackgroundID(UUID _bgID){

        String bgIDStr = UUID.randomUUID().toString();
        m_db =getWritableDatabase();

        try {
            //m_db.execSQL("UPDATE " + SYSTEM_DBTABLENAME + " SET = BACKGROUND_ID'" + bgIDStr + "'"+";");// + " WHERE _arrnum=" + position + ";");
//            m_db.execSQL("REPLACE INTO " + SYSTEM_DBTABLENAME + " SET _arrnum = " + "1" + ";");// + " WHERE _arrnum=" + position + ";");
//            m_db.execSQL("REPLACE " + SYSTEM_DBTABLENAME + " SET = BACKGROUND_ID'" + bgIDStr + "'"+";");// + " WHERE _arrnum=" + position + ";");

            ContentValues cv  = new ContentValues();
            cv.put("_arrnum",1) ;
            m_db.replace(SYSTEM_DBTABLENAME , "_arrnum", cv );
            cv.put("BACKGROUND_ID",bgIDStr) ;
            m_db.replace(SYSTEM_DBTABLENAME , "BACKGROUND_ID", cv );


        }catch (RuntimeException e ){
            m_db.close();
            return -2;
        }

        m_db.close();
        return 0;

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ DBTABLENAME ) ;
        onCreate(db);
    }
    public long insert(String _tag,String _id,String _ip, String _port,String _pw,String _keyword,String _backGroundOnOff){
        m_db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tag",_tag);
        values.put("id",_id);
        values.put("ip",_ip);
        values.put("pw",_pw);
        //if(port.length()<1) values.put("port","22");
        values.put("port",_port);
        values.put("state","NONE");
        values.put("keyword",_keyword);
        values.put("backGroundInterval","0");
        values.put("backGroundOnOff",_backGroundOnOff);

        long res = m_db.insert(DBTABLENAME,null,values  );
        m_db.close();
        return res;
    }
    public void delete(int position){
        m_db =getWritableDatabase();
        m_db.execSQL("DELETE FROM "+DBTABLENAME+" WHERE _arrnum=" + position + ";");
        m_db.close();
    }
    public int updateState(int position,String _state){
        m_db =getWritableDatabase();
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET state='" + _state + "'" + " WHERE _arrnum=" + position + ";");

        if(select(position,0)==position){
            m_db.close();
            return position;
        }
        else{
            m_db.close();
            return -1;
        }
    }
    public int updateAllBgOnOff(String _onOff){
        m_db =getWritableDatabase();
//      m_db.execSQL("UPDATE " + DBTABLENAME + " SET backGroundOnOff='" + _onOff + "'" + " WHERE _arrnum=" + position + ";");

        try {
            m_db.execSQL("UPDATE " + DBTABLENAME + " SET backGroundOnOff='" + _onOff + "'" + ";");
        }catch (RuntimeException e ){
            m_db.close();
            return -1;
        }
        m_db.close();
        return 0;
    }
    public int update(int position,String tag,String id,String ip, String port,String pw,String keyword,String _state,String _retVal,String _backGroundInterval,String _backGroundOnOff) {

        m_db =getWritableDatabase();

        m_db.execSQL("UPDATE " + DBTABLENAME + " SET tag='" + tag + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET id='" + id + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET ip='" + ip + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET port='" + port + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET pw='" + pw + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET state='" + _state + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET retVal='" + _retVal + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET backGroundInterval='" + _backGroundInterval + "'" + " WHERE _arrnum=" + position + ";");
        m_db.execSQL("UPDATE " + DBTABLENAME + " SET backGroundOnOff='" + _backGroundOnOff + "'" + " WHERE _arrnum=" + position + ";");
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

    public int isAllBgExeOnOff(){ //t 있으면 true
        int ret=-1;
        m_db=getWritableDatabase();
        String countQuery="SELECT * FROM "+DBTABLENAME+" WHERE backGroundOnOff="+"'t'";
        Cursor cursor=m_db.rawQuery(countQuery,null );
//        Cursor cursor = m_db.query(true, DBTABLENAME,new String[]{"_arrnum","id","ip","port"},
//                "_arrnum"+"="+arrnum,null,null,null,null,null);

        if(cursor != null){

            cursor.moveToFirst();
            ret = cursor.getCount();

        }

        cursor.close();
        m_db.close();

        return ret;
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
                    lists.add(Integer.toString(cursor.getInt(cursor.getColumnIndex("_arrnum"))));//0
                    lists.add(cursor.getString(cursor.getColumnIndex("tag")));//1
                    lists.add(cursor.getString(cursor.getColumnIndex("id")));//2
                    lists.add(cursor.getString(cursor.getColumnIndex("ip")));//3
                    lists.add(cursor.getString(cursor.getColumnIndex("pw")));//4
                    lists.add(cursor.getString(cursor.getColumnIndex("port")));//5
                    lists.add(cursor.getString(cursor.getColumnIndex("state")));//6
                    lists.add("EMPTY VALUE"); //retValue 7
                    lists.add(cursor.getString(cursor.getColumnIndex("keyword")));// 8
                    lists.add("0");//backGroundInterval 9
                    lists.add(cursor.getString(cursor.getColumnIndex("backGroundOnOff")));// 10
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


    public String getBgID(){
        String uuidStr="-1";
        m_db= getWritableDatabase();
        String selectQuery = "SELECT * FROM "+ SYSTEM_DBTABLENAME;
        Cursor cursor = m_db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    uuidStr=cursor.getString(cursor.getColumnIndex("BACKGROUND_ID"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(java.lang.NullPointerException e){
            e.printStackTrace();
            return uuidStr;
        }
        finally{
            cursor.close();
            m_db.close();
        }






        return uuidStr;

    }


}
