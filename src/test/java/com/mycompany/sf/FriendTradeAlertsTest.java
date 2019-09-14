/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendTradeAlertsTest {
    private DataProcessor mockDataProcessor;
    
    public FriendTradeAlertsTest() {
    }
    
    @Before
    public void setUp() {
        mockDataProcessor = Mockito.mock(DataProcessor.class);
    }

    /**
     * interaction test of get friend trades for last week
     */
    @Test
    public void testGetFriendTradesLastWeek() {
        String userId = "some-user-id";
        Date lowerDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date upperDate = cal.getTime(); // just want upperDate != lowerDate for verification
        List<String> expectedList = new ArrayList();
        expectedList.add("doopy doo. randomish string for sake of matching");
        
        when(mockDataProcessor.getPreviousWeekStartDate(any(Date.class))).thenReturn(lowerDate);
        when(mockDataProcessor.getWeekStartDate(any(Date.class))).thenReturn(upperDate);
        
        FriendTradeAlerts friendTradeAlertsSpy = Mockito.spy(new FriendTradeAlerts(mockDataProcessor));
        doReturn(expectedList).when(friendTradeAlertsSpy).getFriendTradesInRange(userId, lowerDate, upperDate);
        
        List<String> result = friendTradeAlertsSpy.getFriendTradesLastWeek(userId);
        
        verify(friendTradeAlertsSpy, times(1)).getFriendTradesInRange(userId, lowerDate, upperDate);
        Assert.assertEquals(expectedList, result);
    }
    
    /*
    another interaction test. but not implemented.
    */
    @Test
    public void getFriendTradesInRange() {
        // stub the getMap method, verify called
        // stub filterAndSort method, verify called
        // verify that the Sorted objects are converted to related strings
    }
}
