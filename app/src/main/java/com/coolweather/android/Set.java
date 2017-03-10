package com.coolweather.android;

/**
 * Created by hebo on 2017/2/16.
 */
public class Set {
    private String name;
    private boolean buttonOn;
    private boolean swipeEnable;
    public Set(String name,boolean buttonOn,boolean swipeEnable){
        this.name=name;
        this.buttonOn=buttonOn;
        this.swipeEnable=swipeEnable;
    }
    public String getName(){
        return name;
    }
    public boolean getButtonOn(){
        return buttonOn;
    }
    public boolean getSwipeEnable(){
        return swipeEnable;
    }
}
