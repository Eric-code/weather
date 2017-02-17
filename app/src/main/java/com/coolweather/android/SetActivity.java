package com.coolweather.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity {

    private List<Set> setList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initSet();
        SetAdapter adapter=new SetAdapter(SetActivity.this,R.layout.set_item,setList);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        /*CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(SetActivity.this, "你喜欢球类运动", Toast.LENGTH_SHORT).show();
                }
                // 当按钮再次被点击时候响应的事件
                else {
                    Toast.makeText(SetActivity.this, "你不喜欢球类运动", Toast.LENGTH_SHORT).show();
                }
            }
        };
        toggleButton.setOnCheckedChangeListener(listener);*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(SetActivity.this,"这是1",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(SetActivity.this,"这是2",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initSet(){
            Set first=new Set("后台自动更新天气",R.drawable.set);
            setList.add(first);
            /*Set second=new Set("设定更新的频率",R.drawable.set);
            setList.add(second);
            Set third=new Set("不知道写啥，先空着",R.drawable.set);
            setList.add(third);*/

    }
}
