package com.example.alex.stargazer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MySingletonClass {

    private static MySingletonClass instance;

    public static MySingletonClass getInstance() {
        if (instance == null)
            instance = new MySingletonClass();
        return instance;
    }

    private MySingletonClass() {
    }

    private LatLng val1 = new LatLng(42.334515,-71.168648);
    private List<Spot> val2 = new ArrayList();

    public LatLng getValue1() {
        return val1;
    }

    public List<Spot> getValue2() {
        return val2;
    }

    public void setValue1(LatLng value) {
        this.val1 = value;
    }

    public void setValue2(List<Spot> value) {
        this.val2 = value;
    }
}
