package com.fintech.oracle.service.apollo.transformer.abbyy.pojo;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public class Char {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public Char() {
    }

    public Char(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
}
