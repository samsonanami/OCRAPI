
package com.fintech.oracle.service.apollo.transformer.abbyy.pojo;

import javax.xml.bind.annotation.*;

/**
 * Created by sasitha on 6/22/17.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Document {
    @XmlElement(name = "page")
    private Page page;
    public Document() {
    }

    public Document(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
