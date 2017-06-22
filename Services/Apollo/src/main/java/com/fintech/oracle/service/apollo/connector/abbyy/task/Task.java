package com.fintech.oracle.service.apollo.connector.abbyy.task;

import com.fintech.oracle.service.apollo.exception.task.TaskParseException;
import org.omg.PortableInterceptor.ServerRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private TaskStatus taskStatus;
    private String taskId;
    private String resultUrl;
    private String resultString;

    public static Task parseTask(String result) throws TaskParseException{
        DocumentBuilder documentBuilder = null;
        Task task = new Task();
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(result));
            Document document = documentBuilder.parse(inputSource);
            NodeList taskNodes = document.getElementsByTagName("task");
            for( int i = 0; i < taskNodes.getLength(); i++ ) {

                Element taskEl = (Element) taskNodes.item(i);
                task.setTaskId(taskEl.getAttribute("id"));
                task.setTaskStatus(TaskStatus.valueOf(taskEl.getAttribute("status")));

                if(task.taskStatus.equals(TaskStatus.Completed)){
                    task.setResultUrl(taskEl.getAttribute("resultUrl"));
                }
            }
        } catch (ParserConfigurationException | SAXException |IOException e) {
            throw new TaskParseException(e);
        }
        return task;
    }

    public Boolean isTaskActive() {
        if (this.taskStatus == TaskStatus.Queued || this.taskStatus == TaskStatus.InProgress) {
            return true;
        }
        return false;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public String getResultString() {
        return resultString;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Task {").append(System.lineSeparator());

        sb.append("     taskId: ").append(toIndentedString(taskId)).append(System.lineSeparator());
        sb.append("     taskStatus: ").append(toIndentedString(taskStatus)).append(System.lineSeparator());
        sb.append("     resultURL: ").append(toIndentedString(resultUrl)).append(System.lineSeparator());
        sb.append("}");
        return sb.toString();
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    private void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    private void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    private void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}