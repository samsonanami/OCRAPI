package com.fintech.oracle.service.apollo.connector.abbyy.config.roi.reader;

import com.fintech.oracle.service.apollo.exception.config.ConfigurationFileReaderException;
import com.mysql.cj.core.exceptions.ClosedOnExpiredPasswordException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class RoiConfigurationReader {

    public byte[] readDataFromConfigurationFile(String fileLocation) throws ConfigurationFileReaderException, IOException{
        File file = new File(fileLocation);
        long fileLength = file.length();
        byte[] dataBuffer = new byte[(int) fileLength];

        InputStream inputStream = new FileInputStream(file);
        try {

            int offset = 0;
            int numRead = 0;
            while (true) {
                if (offset >= dataBuffer.length) {
                    break;
                }
                numRead = inputStream.read(dataBuffer, offset, dataBuffer.length - offset);
                if (numRead < 0) {
                    break;
                }
                offset += numRead;
            }
            if (offset < dataBuffer.length) {
                throw new ConfigurationFileReaderException("Could not completely read file "
                        + file.getName());
            }
        }finally {
            inputStream.close();
        }
        return dataBuffer;
    }
}
