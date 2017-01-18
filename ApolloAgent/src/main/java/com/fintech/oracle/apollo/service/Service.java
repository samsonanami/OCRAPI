package com.fintech.oracle.apollo.service;

import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.apollo.jni.JNIImageProcessor;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class Service implements Daemon {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    public static void main(String[] args) {
        LOGGER.debug("Starting Apollo agent from main method with arguments {} ", args);
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
        LOGGER.debug("Loading application context file : ${environment}-context.xml  from classpath ");
        AbstractApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath*:${environment}-application-context.xml");
        LOGGER.debug("Loaded application context");
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
