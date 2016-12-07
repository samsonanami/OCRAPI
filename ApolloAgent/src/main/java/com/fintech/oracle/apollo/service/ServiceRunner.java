package com.fintech.oracle.apollo.service;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class ServiceRunner {
    public static void main(String[] args) throws RuntimeException{
        final Service service = new Service();
        try {
            service.init(null);
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
