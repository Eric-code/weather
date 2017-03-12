package com.coolweather.android;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.CommonCounty;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity{
    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    //private Button leftButton;
    //private Button rightButton;

    private Button setButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;
    private LinearLayout touchLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private String mWeatherId;
    private String mCityId;
    private String mProvinceId;
    private String mType;
    private static String getmWeatherId;

    public String[] datas ={"1"};
    public String[] counties ={"海淀"};

    private static boolean chaxun=true;

    private static final String TAG = "WeatherActivity";

    private static float x=0;
    private static int num=0;
    private int i=0;

    private ViewGroup dotsGroup = null ;

    private Weather weather;

    // 定义手势动作两点之间的最小距离
    final int FLIP_DISTANCE = 100;
    float x1,y1,x2,y2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chaxun = true;
        List<CommonCounty> commonCountiesList = DataSupport.findAll(CommonCounty.class);
        for (CommonCounty commonCounty : commonCountiesList) {
            i++;
            counties = Arrays.copyOf(counties, counties.length + 1);
            counties[counties.length - 1] = commonCounty.getCountyName().toString();
            Log.e(TAG, "i=" + i);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        touchLayout = (LinearLayout) findViewById(R.id.touch_layout);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        setButton = (Button) findViewById(R.id.set_button);
        //leftButton = (Button)findViewById(R.id.left_button);
        //rightButton = (Button)findViewById(R.id.right_button);
        titleCity = (TextView) findViewById(R.id.title_city);

        dotsGroup = (ViewGroup) findViewById(R.id.viewGroup);
        //init_smallDots ( 0, i+1) ;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        /*if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {}*/
        // 无缓存时去服务器查询天气
        weather = Utility.handleWeatherResponse(weatherString);
        mWeatherId = weather.basic.weatherId;
        Log.e(TAG,"mweatherId="+mWeatherId);
        mWeatherId = getIntent().getStringExtra("weather_id");
        getmWeatherId=mWeatherId;
        mCityId = getIntent().getStringExtra("city_id");
        mProvinceId = getIntent().getStringExtra("province_id");
        mType = getIntent().getStringExtra("type");
        if (mType.equals("new")) {
            //leftButton.setVisibility(View.INVISIBLE);
            //rightButton.setVisibility(View.INVISIBLE);
            init_smallDots(0, 0);
        } else {
            titleCity.setText("海淀");
            init_smallDots(0, i + 1);
        }
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(mWeatherId);
        showWeatherInfo(weather);
        loadBingPic();
        requestWeather(mWeatherId);
        Log.e(TAG,"mweatherId="+mWeatherId);
        x = titleCity.getTranslationX();

        touchLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            Log.e(TAG, "响应发生");
            if (action == MotionEvent.ACTION_DOWN) {
                // 记录点击到ViewPager时候，手指的X坐标
                x1 = event.getX();
                Log.e(TAG, "x1=" + event.getX() + ",action=" + action);
            }
            if (action == MotionEvent.ACTION_MOVE) {
                // 超过阈值
                Log.e(TAG, "x3=" + event.getX());
                if (Math.abs(event.getX() - x1) > FLIP_DISTANCE) {
                    Log.e(TAG, "x3=" + "禁止");
                    //swipeRefresh.setEnabled(false);
                    weatherLayout.requestDisallowInterceptTouchEvent(true);
                }
            }
            if (action == MotionEvent.ACTION_UP) {
                // 用户抬起手指，恢复父布局状态
                x2 = event.getX();
                Log.e(TAG, "x2=" + event.getX());
                if ((x1 - x2) > FLIP_DISTANCE) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(titleCity, "translationX", x, -500f);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (num == i) {
                                titleCity.setText(counties[num]);
                            } else {
                                num++;
                                titleCity.setText(counties[num]);
                            }
                            init_smallDots(num, i + 1);
                            mWeatherId = titleCity.getText().toString();
                            requestWeather(mWeatherId);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }
                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(titleCity, "translationX", 500f, x);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(animator1).after(animator);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.setDuration(250);
                    animatorSet.start();
                }
                if ((x2 - x1) > FLIP_DISTANCE) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(titleCity, "translationX", x, 500f);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (num == 0) {
                                titleCity.setText(counties[num]);
                            } else {
                                num--;
                                titleCity.setText(counties[num]);
                            }
                            init_smallDots(num, i + 1);
                            mWeatherId = titleCity.getText().toString();
                            requestWeather(mWeatherId);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }
                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(titleCity, "translationX", -500f, x);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(animator1).after(animator);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.setDuration(250);
                    animatorSet.start();
                }
                swipeRefresh.requestDisallowInterceptTouchEvent(false);
                weatherLayout.setEnabled(true);
                Log.e(TAG, "x3=" + "解除禁止");
                return false;
            }
            return true;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
                Log.e(TAG,  "刷新进行");
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(setButton);
            }
        });
    }

    protected void onStart(){
        super.onStart();
        requestWeather(mWeatherId);
        loadBingPic();
    }

    //捕获返回事件
    @Override
    public void onBackPressed() {
        if (mType.equals("new")){
            Intent intent2=new Intent(this,MainActivity.class);
            startActivity(intent2);
            finish();
        }
        super.onBackPressed();
    }

    private void showPopupMenu(View view) {  // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_item1:
                        Intent intent=new Intent(WeatherActivity.this,CityActivity.class);
                        intent.putExtra("cityname1",titleCity.getText().toString());
                        intent.putExtra("city_id",mCityId);
                        intent.putExtra("province_id",mProvinceId);
                        startActivity(intent);
                        break;
                    case R.id.action_item2:
                        Intent intent1=new Intent(Intent.ACTION_SEND);
                        intent1.setType("text/plain");
                        intent1.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        intent1.putExtra(Intent.EXTRA_TEXT, titleCity.getText().toString()+"今天天气为"+weatherInfoText.getText().toString()+
                                "，最高温度为"+datas[1]+"℃，最低温度为"+datas[2]+"℃");
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent1, getTitle()));
                        break;
                    case R.id.action_item3:
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }  });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override  public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }  });
        popupMenu.show();
    }

    private void init_smallDots ( int index, int size ) {
        dotsGroup.removeAllViews () ;
        //add small dots
        for ( int i = 0 ; i < size ; i ++ ) {
            ImageView dot_image = new ImageView ( getBaseContext() ) ;
            dot_image.setLayoutParams( new DrawerLayout.LayoutParams( DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT) ) ;
            dot_image.setPadding ( 10, 0, 10, 0 ) ;
            if ((i==0)&&(index==0)){
                dot_image.setImageResource ( R.drawable.address_focus ) ;
            }else if ((i==0)&&(index!=0)){
                dot_image.setImageResource ( R.drawable.address ) ;
            } else if ( (i == index)&&(index!=0) ) { // the picture of selected small dot is different
                dot_image.setImageResource ( R.drawable.page_indicator_focused ) ;
            }else{
                dot_image.setImageResource ( R.drawable.page_indicator) ;
            }
            dotsGroup.addView ( dot_image ) ;
        }
    }
    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=827da030deda44768700244bb82c02cc";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            //Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                        if (weatherInfoText.getText().toString().equals("晴")){
                            bingPicImg.setImageResource(R.drawable.sunny);
                        } else if (weatherInfoText.getText().toString().equals("多云")||weatherInfoText.getText().toString().equals("阴")){
                            bingPicImg.setImageResource(R.drawable.duoyun);
                        }else if (weatherInfoText.getText().toString().equals("小雨")){
                            bingPicImg.setImageResource(R.drawable.xiaoyu);
                        }else if (weatherInfoText.getText().toString().equals("中雨")){
                            bingPicImg.setImageResource(R.drawable.zhongyu);
                        }else if (weatherInfoText.getText().toString().equals("大雨")){
                            bingPicImg.setImageResource(R.drawable.dayu);
                        }else if (weatherInfoText.getText().toString().equals("雷阵雨")){
                            bingPicImg.setImageResource(R.drawable.leizhenyu);
                        }else if (weatherInfoText.getText().toString().equals("暴雨")){
                            bingPicImg.setImageResource(R.drawable.baoyu);
                        }else if (weatherInfoText.getText().toString().equals("冻雨")){
                            bingPicImg.setImageResource(R.drawable.dongyu);
                        }else if (weatherInfoText.getText().toString().equals("阵雨")){
                            bingPicImg.setImageResource(R.drawable.zhenyu);
                        }else if (weatherInfoText.getText().toString().equals("雨夹雪")){
                            bingPicImg.setImageResource(R.drawable.yujiaxue);
                        }else if (weatherInfoText.getText().toString().equals("霾")||weatherInfoText.getText().toString().equals("雾")){
                            bingPicImg.setImageResource(R.drawable.mai);
                        }else if (weatherInfoText.getText().toString().equals("沙尘暴")) {
                            bingPicImg.setImageResource(R.drawable.shachenbao);
                        }else if (weatherInfoText.getText().toString().equals("小雪")) {
                            bingPicImg.setImageResource(R.drawable.xiaoxue);
                        }else if (weatherInfoText.getText().toString().equals("大雪")||weatherInfoText.getText().toString().equals("中雪")) {
                            bingPicImg.setImageResource(R.drawable.daxue);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                /*SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherInfoText.getText().toString().equals("晴")){
                            bingPicImg.setImageResource(R.drawable.sunny);
                        } else if (weatherInfoText.getText().toString().equals("多云")||weatherInfoText.getText().toString().equals("阴")){
                            bingPicImg.setImageResource(R.drawable.duoyun);
                        }else if (weatherInfoText.getText().toString().equals("小雨")){
                            bingPicImg.setImageResource(R.drawable.xiaoyu);
                        }else if (weatherInfoText.getText().toString().equals("中雨")){
                            bingPicImg.setImageResource(R.drawable.zhongyu);
                        }else if (weatherInfoText.getText().toString().equals("大雨")){
                            bingPicImg.setImageResource(R.drawable.dayu);
                        }else if (weatherInfoText.getText().toString().equals("雷阵雨")){
                            bingPicImg.setImageResource(R.drawable.leizhenyu);
                        }else if (weatherInfoText.getText().toString().equals("暴雨")){
                            bingPicImg.setImageResource(R.drawable.baoyu);
                        }else if (weatherInfoText.getText().toString().equals("冻雨")){
                            bingPicImg.setImageResource(R.drawable.dongyu);
                        }else if (weatherInfoText.getText().toString().equals("阵雨")){
                            bingPicImg.setImageResource(R.drawable.zhenyu);
                        }else if (weatherInfoText.getText().toString().equals("雨夹雪")){
                            bingPicImg.setImageResource(R.drawable.yujiaxue);
                        }else if (weatherInfoText.getText().toString().equals("霾")||weatherInfoText.getText().toString().equals("雾")){
                            bingPicImg.setImageResource(R.drawable.mai);
                        }else if (weatherInfoText.getText().toString().equals("沙尘暴")) {
                            bingPicImg.setImageResource(R.drawable.shachenbao);
                        }else if (weatherInfoText.getText().toString().equals("小雪")) {
                            bingPicImg.setImageResource(R.drawable.xiaoxue);
                        }else if (weatherInfoText.getText().toString().equals("大雪")||weatherInfoText.getText().toString().equals("中雪")) {
                            bingPicImg.setImageResource(R.drawable.daxue);
                        }else {
                            Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                        }
                    }
                });
                if (chaxun){
                    chaxun=false;
                    requestWeather(mWeatherId);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = "最近更新时间："+weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        if (mType.equals("new")){
            titleCity.setText(cityName);
        }
        //titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        int n=0;
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView max_min_Text = (TextView) view.findViewById(R.id.max_min_tem);
            TextView maxTem = (TextView) findViewById(R.id.max_tem);
            TextView minTem = (TextView) findViewById(R.id.min_tem);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            max_min_Text.setText(forecast.temperature.max+"℃～"+forecast.temperature.min+"℃");
            //minText.setText(forecast.temperature.min+"℃");
            String tem=max_min_Text.getText().toString();
            //String min=minText.getText().toString();
            datas= Arrays.copyOf(datas, datas.length+2);
            datas[datas.length-2]=forecast.temperature.max.toString();
            datas[datas.length-1]=forecast.temperature.min.toString();
            forecastLayout.addView(view);
            maxTem.setText(datas[1]+"℃");
            minTem.setText(datas[2]+"℃");
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
