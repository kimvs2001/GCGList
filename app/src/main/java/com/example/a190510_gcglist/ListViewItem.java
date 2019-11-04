package com.example.a190510_gcglist;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ListViewItem {

    private Drawable iconDrawable;
    private String titleStr ;
    private String descStr ;
    private String stateStr ;
    private String pwStr;
    private String portStr;
    private String _arrnum;
    private String retVal;
    private String keywordStr;
    private String backGroundOnOff;
    private ImageView imageView_state;


    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setArrnum(String _arrnum){_arrnum = _arrnum;}
    public void setRetVal(String retVal){retVal = retVal;}
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setState(String state) {
        stateStr= state;

    }
    public void setPw(String pw) {
        pwStr= pw;
    }
    public void setPort(String port) {
        portStr= port;
    }
    public void setKeyword(String keyword) {
        keywordStr = keyword ;
    }
    public void setbackGroundOnOff(String _backGroundOnOff){backGroundOnOff=_backGroundOnOff;}
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {return this.titleStr ; }
    public String getDesc() {
        return this.descStr ;
    }
    public String getState() {
        return this.stateStr ;
    }
    public String getPw() {
        return this.pwStr;
    }
    public String getPort() {
        return this.portStr;
    }
    public String getBackGroundOnOff(){return this.backGroundOnOff;}
}
