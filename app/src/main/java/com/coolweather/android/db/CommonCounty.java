package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hebo on 2017/2/22.
 */
public class CommonCounty extends DataSupport {
    private int id;
    private String countyName;
    private String cityId;
    private String provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String proviceId) {
        this.provinceId = proviceId;
    }
}
