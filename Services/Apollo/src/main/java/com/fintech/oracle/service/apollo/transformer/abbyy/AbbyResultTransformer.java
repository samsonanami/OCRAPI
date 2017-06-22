package com.fintech.oracle.service.apollo.transformer.abbyy;

import com.fintech.oracle.service.apollo.transformer.ResultTransformer;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Document;
import com.fintech.oracle.service.apollo.transformer.abbyy.pojo.Page;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public class AbbyResultTransformer implements ResultTransformer<Document, String> {

    @Override
    public Document transformResults(String rawResults) {
        Document document = new Document();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            document = (Document) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(rawResults.getBytes()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return document;
    }
}
