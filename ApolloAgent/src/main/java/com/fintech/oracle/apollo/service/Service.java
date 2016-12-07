package com.fintech.oracle.apollo.service;

import com.fintech.oracle.jobchanel.consumer.MessageConsumer;
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

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void destroy() {

    }

    private void init(){
        applicationContext = new ClassPathXmlApplicationContext("classpath*:${runtime.environment}-context.xml");
        LOGGER.debug("Loaded application context from file " + applicationContext.getBean("application-file-name"));
    }
}
