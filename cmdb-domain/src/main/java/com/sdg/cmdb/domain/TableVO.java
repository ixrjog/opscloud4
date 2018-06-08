package com.sdg.cmdb.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/1.
 */
public class TableVO<T> implements Serializable {
    private static final long serialVersionUID = -7647497115882990932L;

    private int nowPage;

    private long size;

    private T data;

    public TableVO(long size, T data) {
        this.size = size;
        this.data = data;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TableVO{" +
                "nowPage=" + nowPage +
                ", size=" + size +
                ", data=" + data +
                '}';
    }
}
