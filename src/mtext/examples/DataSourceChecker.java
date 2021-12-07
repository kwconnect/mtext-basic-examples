package mtext.examples;
/*
 * Copyright (c) 2015 kuehn & weyh Software GmbH
 * Freiburg, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of kuehn & weyh Software GmbH ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with kuehn & weyh.
 */



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import de.kwsoft.mtext.api.databinding.CSVDataSource;
import de.kwsoft.mtext.api.databinding.DataProvider;
import de.kwsoft.mtext.api.databinding.DataSource;

/**
 * @author Armin Hasler
 *
 */
public class DataSourceChecker {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    	String csvFileName = args[0];    	
    	
    	
        final Reader reader = new InputStreamReader(new FileInputStream(csvFileName));
        final DataSource csvDS = new CSVDataSource("csvDataSource", reader, '#');
      
        for(final DataProvider rows : csvDS.getDataProvider()) {
        	System.out.println("--- start row ---");
            for(final DataProvider value : rows) {
                System.out.println("-" + value.getName() + "<" + value.getValue());
            }
        }
        System.out.println("");
        System.out.println("start loopCounter()");
        loopCounter(csvFileName);
    }

    public static void loopCounter(String fileName) throws IOException {
        final Reader reader = new InputStreamReader(new FileInputStream(fileName));
        final DataSource csvDS = new CSVDataSource("csvDataSource", reader, '#');

        for(final DataProvider rows : csvDS.getDataProvider()) {
        	System.out.println("--- start row ---");
        	int count = rows.count();
            for(int i = 0; i < count; ++i) {
                final DataProvider value = rows.get(i);
                System.out.println("-" + value.getName() + "<" + value.getValue());
            }
        }
    }
}