package com.fintech.oracle.service.apollo.transformer.abbyy;

import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Document;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public class AbbyResultTransformerTest {

    @Test
    public void should_Translate() throws Exception {
        File file = new File("/media/sasitha/Projects/Orion/Code/OCRAPI/Services/Apollo/src/test/java/com/fintech/oracle/service/apollo/transformer/abbyy/results.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Document document = (Document) jaxbUnmarshaller.unmarshal(file);
        assertEquals(0,document.getPage().getId());
    }
}