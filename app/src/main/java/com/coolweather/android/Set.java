package com.coolweather.android;

/**
 * Created by hebo on 2017/2/16.
 */
public class Set {
    private String name;
    private int imageId;
    private boolean buttonOn;
    public Set(String name,int imageId,boolean buttonOn){
        this.name=name;
        this.imageId=imageId;
        this.buttonOn=buttonOn;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public boolean getButtonOn(){
        return buttonOn;
    }
}
