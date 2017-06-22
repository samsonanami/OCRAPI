package com.fintech.oracle.apollo.service;

import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.jni.JNIImageProcessor;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class Service implements Daemon {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private static final String ENV_FILE = "file";
    public static void main(String[] args) {
        LOGGER.debug("Starting Apollo agent from main method with arguments {} ", (Object[])args);
        initializeApplicationContext();
    }

    @Override
    public void init(DaemonContext context) throws DaemonInitException {
        LOGGER.debug("Initializing an Apollo agent instance");
        initializeApplicationContext();
    }

    @Override
    public void start() throws Exception {
        LOGGER.debug("Starting Apollo agent");
    }

    @Override
    public void stop() throws Exception {
        LOGGER.debug("Stopping Apollo agent");
    }

    @Override
    public void destroy() {
        LOGGER.debug("Destroying Apollo agent");
    }

    private static   void initializeApplicationContext(){

        String applicationContextLoadLocation = System.getProperty("applicationContextLoadFrom");
        ApplicationContext applicationContext;
        String contextFilePath = System.getProperty("contextFilePath");
        String contextFileName = System.getProperty("contextFileName");
        if(ENV_FILE.equals(applicationContextLoadLocation)){
            LOGGER.debug("Loading application context file from file system");
            applicationContext = new FileSystemXmlApplicationContext("file:"+contextFilePath+contextFileName);
        }else {
            LOGGER.debug("Loading application context file from classpath");
            applicationContext =
                    new ClassPathXmlApplicationContext("classpath*:"+contextFileName);
        }
        LOGGER.debug("Initialising JNI image processing agent");
        JNIImageProcessor jniImageProcessor = (JNIImageProcessor) applicationContext.getBean("jniImageProcessor");
        try {
            jniImageProcessor.initializeAgent();
            LOGGER.debug("Successfully initialized JNI image processing agent.");
        } catch (JobException e) {
            LOGGER.error("Error initializing JNI image processing agent", e);
        }
    }
}
