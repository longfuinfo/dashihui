package com.dashihui.afford.ui.model;

import java.util.List;

/**
 * Created by bobge on 15/7/31.
 */
public class ModelCatogray {
    private String kind;
    private List<ModelGoods> list;
    private int count;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<ModelGoods> getList() {
        return list;
    }

    public void setList(List<ModelGoods> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
