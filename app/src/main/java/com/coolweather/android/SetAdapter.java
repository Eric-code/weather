package com.coolweather.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.coolweather.android.service.AutoUpdateService;

import java.lang.reflect.Array;
import java.util.List;

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
        ImageView setImage=(ImageView)view.findViewById(R.id.set_image);
        final TextView setName=(TextView)view.findViewById(R.id.set_name);
        setImage.setImageResource(set.getImageId());
        setName.setText(set.getName());
        final ToggleButton toggleButton=(ToggleButton)view.findViewById(R.id.toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                toggleButton.setChecked(isChecked);
                if (isChecked) {
                    setName.setText("后台自动更新天气");
                    AutoUpdateService.ifupdateweather=true;
                }
                // 当按钮再次被点击时候响应的事件
                else {
                    setName.setText("后台不自动更新天气");
                    AutoUpdateService.ifupdateweather=false;
                }
            }
        });
        return view;
    }
}
