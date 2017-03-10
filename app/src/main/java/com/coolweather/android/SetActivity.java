package com.coolweather.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.coolweather.android.service.AutoUpdateService;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity {
    public static Handler revHandler;

    private String mCityId;
    private String mProvinceId;

    private List<Set> setList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initSet();
        SetAdapter adapter=new SetAdapter(SetActivity.this,R.layout.set_item,setList);
        ListView listView=(ListView)findViewById(R.id.list_view);
        //ListView listView1=(ListView)findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
        //listView1.setAdapter(adapter);
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.timepickerlayout);
        //获取WeatherActivity传入的城市名
        Intent intent = getIntent();
        final String cityname=intent.getStringExtra("cityname");
        mCityId=intent.getStringExtra("city_id");
        mProvinceId=intent.getStringExtra("province_id");

        NumberPicker numberPicker=(NumberPicker)findViewById(R.id.updatetimepicker);
        int defaulttime=8;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(24);
        numberPicker.setValue(defaulttime);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldvalue, int newvalue) {
                //Toast.makeText(SetActivity.this,"选择的更新时间为："+newvalue,Toast.LENGTH_SHORT).show();
                AutoUpdateService.updatehour=newvalue;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //Toast.makeText(SetActivity.this,"这是1",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SetActivity.this,CityActivity.class);
                        intent.putExtra("cityname1",cityname);
                        intent.putExtra("city_id",mCityId);
                        intent.putExtra("province_id",mProvinceId);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(SetActivity.this,"这是2",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        // 创建revHandler对象
        revHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 接收到UI线程中用户输入的数据
                if (msg.what == 0x345)
                {
                    try
                    {
                        //Toast.makeText(SetActivity.this,"收到广播",Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                if (msg.what == 0x456)
                {
                    try
                    {
                        //Toast.makeText(SetActivity.this,"收到广播",Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //localBroadcastManager.unregisterReceiver(localReceiver);
    }

    private void initSet(){
            Set first=new Set("对常用城市进行管理",false,true);
            setList.add(first);
            Set second=new Set("后台自动更新天气",true,true);
            setList.add(second);
            /*Set third=new Set("不知道写啥，先空着",R.drawable.set);
            setList.add(third);*/
    }
}
