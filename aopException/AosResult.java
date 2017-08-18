package com.ucsmy.aos.api.common.constants;

/**
 * Created by ucs_liyuan on 2017/4/11.
 */
public class AosResult {
    private int res;
    private Object data;
    private String des;


    public AosResult() {

    }

    public AosResult(int res) {
        this.res = res;
    }

    public AosResult(int res, Object data, String des) {
        this.data = data;
        this.des = des;
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
