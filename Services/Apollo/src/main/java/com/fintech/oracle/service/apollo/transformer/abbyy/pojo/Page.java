package com.fintech.oracle.service.apollo.transformer.abbyy.pojo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by sasitha on 6/22/17.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Page {
    private int id;

    @XmlElement(name = "text")
    List<Text> text;

    public Page() {
    }

    public Page(int id, List<Text> text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Text> getText() {
        return text;
    }

    public void setText(List<Text> text) {
        this.text = text;
    }
}
