package com.example.a190510_gcglist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    private Map<Integer,ListViewItem> map = new HashMap<>();

    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }



    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


        final TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;
        TextView stateTextView = (TextView) convertView.findViewById(R.id.textView3) ;
        //ImageView imageViewState = (ImageView) convertView.findViewById(R.id.imageView_state); //190521 added
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView_state) ;//





        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());
        stateTextView.setText(listViewItem.getState());

        /*** STATE에 따라 IMAGE 셋팅 >>***/
        if(listViewItem.getState().equals("CONNECTED")){
            iconImageView.setImageResource(R.drawable.green);
        }
        else if(listViewItem.getState().equals("NONE")){
            iconImageView.setImageResource(R.drawable.gray);
        }
        else if(listViewItem.getState().equals("...")){
            iconImageView.setImageResource(R.drawable.yellow);
        }
        else{
            iconImageView.setImageResource(R.drawable.red);
        }
        /***<< STATE에 따라 IMAGE 셋팅 ***/

        descTextView.setVisibility(View.GONE); // ID : IP 설명 하는 부분 가리기

        //위젯에 대한 리스너

        /*titleTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context,"선택"+titleTextView.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });*/
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String _arrnum,String tag,String id, String ip,String pw,String port, String state,String retVal,String keyword,String backGroundInterval,String backGroundOnOff) {
        ListViewItem item = new ListViewItem();
        item.setArrnum(_arrnum );
        item.setArrnum(tag);
        item.setTitle(tag);
        item.setDesc(id +" : "+ip );
        item.setState(state);
        item.setPw(pw);
        item.setPort(port );
        item.setRetVal(retVal);
        item.setKeyword(keyword);
        item.setKeyword(backGroundInterval);
        if(backGroundOnOff.equals(("t"))) {
            item.setbackGroundOnOff(backGroundOnOff);
        }
        item.setbackGroundOnOff("f");

        map.put(getCount(),item);
        listViewItemList.add(item);


    }

    public void updateState(int rowNumber,String state){
        map.get(rowNumber).setState(state);

    }
    public void cleareView(){
        this.listViewItemList.clear();
    }


   // AdapterView.OnItemClickListener itemClickListenerOfLanguageList = new AdapterView.OnItemClickListener()
   // {
   //     public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
   //     {
                /*String toastMessage = ((TextView)clickedView).getText().toString() + " is selected.";
                Toast.makeText(
                        getApplicationContext(),
                        toastMessage,
                        Toast.LENGTH_SHORT
                ).show();*/
           // Toast.makeText(getApplicationContext(), "리스트뷰 터치", Toast.LENGTH_LONG).show();
    //    }

    //};
    //listview.setOnItemClickListener(itemClickListenerOfLanguageList);

}

