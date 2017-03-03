package com.coolweather.android;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.CommonCounty;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    static int listnum=0;//用来标记listview的数目
    private static final String TAG = "CityActivity";
    private List<Set> setList=new ArrayList<>();
    private String[] data={"first","second","third","fourth","fifth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initCity();
        //ArrayAdapter<String>adapter=new ArrayAdapter<String>(CityActivity.this,android.R.layout.simple_list_item_1,data);
        final ListView listView=(ListView)findViewById(R.id.city_listview);
        final SetAdapter adapter=new SetAdapter(CityActivity.this,R.layout.set_item,setList);
        listView.setAdapter(adapter);//将列表显示出来
        //获取SetActivity传入的城市名
        Intent intent = getIntent();
        final String cityname1=intent.getStringExtra("cityname1");
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
                        first.setCityId("beijing");
                        first.setProvinceId("zhongguo");
                        first.save();
                        Log.e(TAG,cityname1);
                        Set listfirst=new Set(cityname1,R.drawable.set_1,false);
                        setList.add(listfirst);
                        listView.setAdapter(adapter);//将列表显示出来
                        List<CommonCounty> commonCountiesList = DataSupport.findAll(CommonCounty.class);
                        for (CommonCounty commonCounty1:commonCountiesList){
                            Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 2:
                        CommonCounty second=new CommonCounty();
                        second.setCountyName(cityname1);
                        second.save();
                        Set listsecond=new Set(cityname1,R.drawable.set_1,false);
                        setList.add(listsecond);
                        listView.setAdapter(adapter);
                        List<CommonCounty> commonCountiesList1 = DataSupport.findAll(CommonCounty.class);
                        for (CommonCounty commonCounty1:commonCountiesList1){
                            Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        CommonCounty third=new CommonCounty();
                        third.setCountyName(cityname1);
                        third.save();
                        Set listthird=new Set(cityname1,R.drawable.set_1,false);
                        setList.add(listthird);
                        listView.setAdapter(adapter);
                        //Toast.makeText(CityActivity.this,third.getId(),Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        CommonCounty fourth=new CommonCounty();
                        fourth.setCountyName(cityname1);
                        fourth.save();
                        Set listfourth=new Set(cityname1,R.drawable.set_1,false);
                        setList.add(listfourth);
                        listView.setAdapter(adapter);
                        //Toast.makeText(CityActivity.this,fourth.getId(),Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        CommonCounty fifth=new CommonCounty();
                        fifth.setCountyName(cityname1);
                        fifth.save();
                        Set listfifth=new Set(cityname1,R.drawable.set_1,false);
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

            }
        });
    }
    private void initCity(){
        List<CommonCounty> commonCountiesList = DataSupport.findAll(CommonCounty.class);
        int i=0;
        for (CommonCounty commonCounty1:commonCountiesList) {
            //String name=commonCountiesList.get(i).toString();
            Toast.makeText(CityActivity.this,commonCounty1.getCountyName(),Toast.LENGTH_SHORT).show();
            i++;
                switch (i){
                    case 1:
                        Set first=new Set(commonCounty1.getCountyName(),R.drawable.set_1,false);
                        setList.add(first);
                        break;
                    case 2:
                        Set second=new Set(commonCounty1.getCountyName(),R.drawable.set_1,false);
                        setList.add(second);
                        break;
                    case 3:
                        Set third=new Set(commonCounty1.getCountyName(),R.drawable.set_1,false);
                        setList.add(third);
                        break;
                    case 4:
                        Set fourth=new Set(commonCounty1.getCountyName(),R.drawable.set_1,false);
                        setList.add(fourth);
                        break;
                    case 5:
                        Set fifth=new Set(commonCounty1.getCountyName(),R.drawable.set_1,false);
                        setList.add(fifth);
                        break;
                    default:
                        break;


            }

        }


        /*Set second=new Set(WeatherActivity.,R.drawable.set,true);
        setList.add(second);
        Set third=new Set("不知道写啥，先空着",R.drawable.set);
        setList.add(third);*/
    }
}
