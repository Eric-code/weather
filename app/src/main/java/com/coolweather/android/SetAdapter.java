package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.coolweather.android.service.AutoUpdateService;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Timer;

/**
 * Created by hebo on 2017/2/16.
 */
public class SetAdapter extends ArrayAdapter<Set> {

    //public String[] datas = {"是否允许后台自动更新天气","设定更新的频率","不知道写啥，先空着"};
    private int resourceId;
    public SetAdapter(Context context, int textViewResourceId, List<Set> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        Set set=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        final SwipeMenuLayout swipeMenuLayout=(SwipeMenuLayout)view.findViewById(R.id.swipe_Layout);
        if (set.getSwipeEnable()==true){
            swipeMenuLayout.setSwipeEnable(true);
        }else {
            swipeMenuLayout.setSwipeEnable(false);
        }

        //ImageView setImage=(ImageView)view.findViewById(R.id.set_image);

        final TextView setName=(TextView)view.findViewById(R.id.set_name);
        //setImage.setImageResource(set.getImageId());
        setName.setText(set.getName());
        final ToggleButton toggleButton=(ToggleButton)view.findViewById(R.id.toggle);
        if (set.getButtonOn()==true){
            toggleButton.setVisibility(View.VISIBLE);
        }else{
            toggleButton.setVisibility(View.INVISIBLE);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                toggleButton.setChecked(isChecked);
                if (isChecked) {
                    setName.setText("后台自动更新天气");
                    AutoUpdateService.ifupdateweather=true;
                    try
                    {
                        // 当用户按下发送按钮后，将用户输入的数据封装成Message
                        // 然后发送给子线程的Handler
                        Message msg = new Message();
                        msg.what = 0x345;
                        SetActivity.revHandler.sendMessage(msg);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                // 当按钮再次被点击时候响应的事件
                else {
                    setName.setText("后台自动更新天气");
                    AutoUpdateService.ifupdateweather=false;
                    try
                    {
                        // 当用户按下发送按钮后，将用户输入的数据封装成Message
                        // 然后发送给子线程的Handler
                        Message msg = new Message();
                        msg.what = 0x456;
                        SetActivity.revHandler.sendMessage(msg);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        });
        Button btnDelete=(Button)view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hehh");
            }
        });
        return view;
    }

}
