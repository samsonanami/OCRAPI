package com.fintech.oracle.service.apollo.transformer.abbyy.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by sasitha on 6/22/17.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Text {

    @XmlAttribute
    private String id;
    private int left;
    private int top;
    private int right;
    private int bottom;


    private String value;

    public Text() {
    }

    public Text(String id, int left, int top, int right, int bottom, String value) {
        this.id = id;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.value = value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
