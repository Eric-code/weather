package com.coolweather.android;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.CommonCounty;
import com.coolweather.android.db.County;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    public static Handler delHandler;
    static int listnum=0;//用来标记listview的数目
    static int deletenum=0;//用来标记listview的数目
    static int id1,id2,id3,id4,id5=0;//用来标记listview的数目
    private static final String TAG = "CityActivity";
    private List<Set> setList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initCity();
        Button buttondelete=(Button)findViewById(R.id.btnDelete);
        //ArrayAdapter<String>adapter=new ArrayAdapter<String>(CityActivity.this,android.R.layout.simple_list_item_1,data);
        final ListView listView=(ListView)findViewById(R.id.city_listview);
        final SetAdapter adapter=new SetAdapter(CityActivity.this,R.layout.set_item,setList);
        listView.setAdapter(adapter);//将列表显示出来
        //获取SetActivity传入的城市名
        Intent intent = getIntent();
        final String cityname1=intent.getStringExtra("cityname1");
        final String cityid=intent.getStringExtra("city_id");
        final String provinceid=intent.getStringExtra("province_id");
        //悬浮按钮点击事件
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listnum++;
                //Toast.makeText(CityActivity.this,cityname1,Toast.LENGTH_SHORT).show();
                switch (listnum){
                    case 1:
                        CommonCounty first=new CommonCounty();
                        first.setCountyName(cityname1);
                        first.setCityId(cityid);
                        first.setProvinceId(provinceid);
                        first.save();
                        //Log.e(TAG,cityname1);
                        Set listfirst=new Set(cityname1,false,true);
                        setList.add(listfirst);
                        listView.setAdapter(adapter);//将列表显示出来
                        /*for (CommonCounty commonCounty1:commonCountiesList){
                            Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
                        }*/

                        break;
                    case 2:
                        CommonCounty second=new CommonCounty();
                        second.setCountyName(cityname1);
                        second.setCityId(cityid);
                        second.setProvinceId(provinceid);
                        second.save();
                        Set listsecond=new Set(cityname1,false,true);
                        setList.add(listsecond);
                        listView.setAdapter(adapter);
                        List<CommonCounty> commonCountiesList1 = DataSupport.findAll(CommonCounty.class);
                        /*for (CommonCounty commonCounty1:commonCountiesList1){
                            Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
                        }*/
                        break;
                    case 3:
                        CommonCounty third=new CommonCounty();
                        third.setCountyName(cityname1);
                        third.setCityId(cityid);
                        third.setProvinceId(provinceid);
                        third.save();
                        Set listthird=new Set(cityname1,false,true);
                        setList.add(listthird);
                        listView.setAdapter(adapter);
                        //Toast.makeText(CityActivity.this,third.getId(),Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        CommonCounty fourth=new CommonCounty();
                        fourth.setCountyName(cityname1);
                        fourth.setCityId(cityid);
                        fourth.setProvinceId(provinceid);
                        fourth.save();
                        Set listfourth=new Set(cityname1,false,true);
                        setList.add(listfourth);
                        listView.setAdapter(adapter);
                        //Toast.makeText(CityActivity.this,fourth.getId(),Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        CommonCounty fifth=new CommonCounty();
                        fifth.setCountyName(cityname1);
                        fifth.setCityId(cityid);
                        fifth.setProvinceId(provinceid);
                        fifth.save();
                        Set listfifth=new Set(cityname1,false,true);
                        setList.add(listfifth);
                        listView.setAdapter(adapter);
                        //Toast.makeText(CityActivity.this,fifth.getId(),Toast.LENGTH_SHORT).show();
                        listnum=0;
                        DataSupport.deleteAll(CommonCounty.class);
                        break;
                    default:
                        break;
                }
            }
        });

        //常用查询城市列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                deletenum=position;
                Log.e(TAG,"position="+position);
                listView.getCheckedItemPosition();
                /*List<CommonCounty> commonCountiesList = DataSupport.findAll(CommonCounty.class);
                CommonCounty commonCounty=commonCountiesList.get(position);
                String countyname=commonCounty.getCountyName();
                List<County> counties=DataSupport.where("countyName=?",countyname).find(County.class);
                County county=counties.get(0);
                //Toast.makeText(CityActivity.this,county.getCountyName(),Toast.LENGTH_SHORT).show();
                String weatherId=county.getWeatherId();
                Intent intent = new Intent(CityActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", weatherId);
                intent.putExtra("province_id",provinceid);
                intent.putExtra("city_id",cityid);
                startActivity(intent);
                finish();*/
            }
        });
        // 创建revHandler对象
        delHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 接收到UI线程中用户输入的数据
                if (msg.what == 0x123)
                {
                    try
                    {
                        Bundle b = msg.getData();
                        int index = b.getInt("index");
                        switch (index){
                            case 0:
                                setList.remove(0);
                                listView.setAdapter(adapter);
                                DataSupport.delete(CommonCounty.class, id1);
                                Log.e(TAG,"删除数据库1");
                                break;
                            case 1:
                                setList.remove(1);
                                listView.setAdapter(adapter);
                                DataSupport.delete(CommonCounty.class, id2);
                                Log.e(TAG,"删除数据库2");
                                break;
                            case 2:
                                setList.remove(2);
                                listView.setAdapter(adapter);
                                DataSupport.delete(CommonCounty.class, id3);
                                Log.e(TAG,"删除数据库3");
                                break;
                            default:
                                break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void initCity(){
        List<CommonCounty> commonCountiesList = DataSupport.findAll(CommonCounty.class);
        int i=0;
        for (CommonCounty commonCounty1:commonCountiesList) {
            //String name=commonCountiesList.get(i).toString();
            //Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
            i++;
                switch (i){
                    case 1:
                        Set first=new Set(commonCounty1.getCountyName(),false,true);
                        id1=commonCounty1.getId();
                        Log.e(TAG,"id1="+id1);
                        setList.add(first);
                        break;
                    case 2:
                        Set second=new Set(commonCounty1.getCountyName(),false,true);
                        id2=commonCounty1.getId();
                        Log.e(TAG,"id2="+id2);
                        setList.add(second);
                        break;
                    case 3:
                        Set third=new Set(commonCounty1.getCountyName(),false,true);
                        id3=commonCounty1.getId();
                        Log.e(TAG,"id3="+id3);
                        setList.add(third);
                        break;
                    case 4:
                        Set fourth=new Set(commonCounty1.getCountyName(),false,true);
                        id4=commonCounty1.getId();
                        Log.e(TAG,"id4="+id4);
                        setList.add(fourth);
                        break;
                    case 5:
                        Set fifth=new Set(commonCounty1.getCountyName(),false,true);
                        id5=commonCounty1.getId();
                        Log.e(TAG,"id5="+id5);
                        setList.add(fifth);
                        break;
                    default:
                        break;
            }
        }
    }
}
