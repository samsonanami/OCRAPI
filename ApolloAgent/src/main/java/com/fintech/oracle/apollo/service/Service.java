package com.fintech.oracle.apollo.service;

import com.fintech.oracle.jobchanel.consumer.MessageConsumer;
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

    private static AbstractApplicationContext applicationContext;
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    @Override
    public void init(DaemonContext context) throws DaemonInitException, Exception {
        LOGGER.debug("Initializing an Apollo agent instance");
        this.init();
    }

    @Override
    public void start() throws Exception {
        LOGGER.debug("Starting Apollo agent");
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void destroy() {

    }

    private void init(){
        LOGGER.debug("Loading application context file : ${environment}-context.xml  from classpath ");
        applicationContext = new ClassPathXmlApplicationContext("classpath*:${environment}-application-context.xml");
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
