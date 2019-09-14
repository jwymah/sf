package com.mycompany.sf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * class holding the 'provided' methods.
 * here they are simply stubbed with sample data
 */
public class DataLibrary {
    private static final Logger LOG = Logger.getLogger(DataLibrary.class);

    /*
    This method was specified to be an available as a library function.
    For this it will be stubbed with sample data.
     */
    public List<String> getTradeTransactionsForUser(String userId) {
        List<String> list;
        try {
            list = loadTransactionsForUser("trades_" + userId);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            list = new ArrayList();
        }
        return list;
    }

    private List<String> loadTransactionsForUser(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        return readFromBuffer(bufferedReader);
    }

    private List<String> readFromBuffer(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        List<String> strings = new ArrayList();
        while (line != null) {
            strings.add(line);
            line = bufferedReader.readLine();
        }
        return strings;
    }

    /*
    This method was specified to be provided as a library function.
    For this it will be stubbed with sample data.
     */
    public List<String> getFriendsListForUser(String userId) {
        List<String> list = new ArrayList();
        list.add("111");
        list.add("222");
        list.add("333");
        return list;
    }
}
