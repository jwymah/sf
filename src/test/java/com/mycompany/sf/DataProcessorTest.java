package com.mycompany.sf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class DataProcessorTest {
    DataLibrary dataLibrary;
    DataProcessor dataProcessor;
    
    public DataProcessorTest() {
    }
    
    @Before
    public void setUp() {
        dataLibrary = Mockito.mock(DataLibrary.class);
        dataProcessor = new DataProcessor(dataLibrary);
    }
    
    @Test
    public void getCountedMapOfFriendTrades_dateBoundaries() {
        List<String> friends = new ArrayList();
        String friendOne = "one";
        friends.add(friendOne);
        
        List<String> tradesOne = new ArrayList();
        //remember trades are assumed to be in reverse chronological order!
        //add them put them in order
        tradesOne.add(createTradeString(2018,4,8,TradeType.BUY,"CSCO"));
        tradesOne.add(createTradeString(2018,4,4,TradeType.BUY,"GOOG"));
        tradesOne.add(createTradeString(2018,4,1,TradeType.BUY,"CSCO"));
        tradesOne.add(createTradeString(2018,3,31,TradeType.BUY,"CSCO"));
        
        when(dataLibrary.getFriendsListForUser(anyString())).thenReturn(friends);
        when(dataLibrary.getTradeTransactionsForUser(friendOne)).thenReturn(tradesOne);
        
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 3, 12, 0, 0, 0); //month counter starts at 0. 3 is april
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date upperDate = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        Date lowerDate = cal.getTime();
        
        Map<String, TradeCounter> result = dataProcessor.getCountsOfFriendTrades("some-user-id", lowerDate, upperDate);
        assertEquals(2, result.size());
        assertEquals(1, result.get("CSCO").getCount());
    }
    
    @Test
    public void getCountedMapOfFriendTrades_checkCounting() {
        List<String> friends = new ArrayList();
        String friendOne = "one";
        friends.add("one");
        String friendTwo = "two";
        friends.add("two");
        String friendThree = "three";
        friends.add("three");
        when(dataLibrary.getFriendsListForUser(anyString())).thenReturn(friends);
        
        List<String> tradesOne = new ArrayList();
        tradesOne.add(createTradeString(2018,04,1,TradeType.BUY,"CSCO"));
        tradesOne.add(createTradeString(2018,04,1,TradeType.BUY,"GOOG"));
        
        List<String> tradesTwo = new ArrayList();
        tradesTwo.add(createTradeString(2018,04,1,TradeType.BUY,"CSCO"));
        tradesOne.add(createTradeString(2018,04,1,TradeType.SELL,"GOOG"));
        
        List<String> tradesThree = new ArrayList();
        tradesThree.add(createTradeString(2018,04,1,TradeType.BUY,"CSCO"));
        
        when(dataLibrary.getFriendsListForUser(anyString())).thenReturn(friends);
        when(dataLibrary.getTradeTransactionsForUser(friendOne)).thenReturn(tradesOne);
        when(dataLibrary.getTradeTransactionsForUser(friendTwo)).thenReturn(tradesTwo);
        when(dataLibrary.getTradeTransactionsForUser(friendThree)).thenReturn(tradesThree);
        
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 3, 12, 0, 0, 0); //month counter starts at 0. 03 is april
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date upperDate = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        Date lowerDate = cal.getTime();
        
        Map<String, TradeCounter> result = dataProcessor.getCountsOfFriendTrades("some-user-id", lowerDate, upperDate);
        assertEquals(2, result.size());
        assertEquals(3, result.get("CSCO").getCount());
    }
    
    @Test
    public void testFilterAndSortTradeCounters_zeroCountsRemoved() {
        List<TradeCounter> list = new ArrayList();
        TradeCounter tc1 = new TradeCounter("GOOG");
        tc1.adjustCount(10);
        TradeCounter tc2 = new TradeCounter("YHOO");
        tc2.adjustCount(4);
        TradeCounter tc3 = new TradeCounter("AMZN");
        tc3.adjustCount(0);
        
        list.add(tc1);
        list.add(tc2);
        list.add(tc3);
        
        List<TradeCounter> result = dataProcessor.filterAndSortTradeCounters(list);
        assertEquals(2, result.size());
        assertFalse(result.contains(tc3));
    }
    
    @Test
    public void testFilterAndSortTradeCounters_empty() {
        List result = dataProcessor.filterAndSortTradeCounters(new ArrayList());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testFilterAndSortTradeCounters_single() {
    }
    
    @Test
    public void testFilterAndSortTradeCounters_checkOrder() {
        List<TradeCounter> list = new ArrayList();
        TradeCounter tc1 = new TradeCounter("GOOG");
        tc1.adjustCount(10);
        TradeCounter tc2 = new TradeCounter("YHOO");
        tc2.adjustCount(7);
        TradeCounter tc3 = new TradeCounter("NFLX");
        tc3.adjustCount(-4);
        TradeCounter tc4 = new TradeCounter("AMZN");
        tc4.adjustCount(3);
        
        list.add(tc1);
        list.add(tc2);
        list.add(tc3);
        list.add(tc4);
        
        List<TradeCounter> result = dataProcessor.filterAndSortTradeCounters(list);
        assertEquals(4, result.size());
        assertEquals(tc1, result.get(0));
        assertEquals(tc2, result.get(1));
        assertEquals(tc4, result.get(2));
        assertEquals(tc3, result.get(3));
    }

    @Test
    public void testGetPreviousWeekStartDate() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date result = dataProcessor.getPreviousWeekStartDate(now);
        
        assertEquals(cal.getTime(), result);
    }
    
    @Test
    public void testGetWeekStartDate() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date result = dataProcessor.getWeekStartDate(now);
        
        assertEquals(cal.getTime(), result);
    }
    
    private String createTradeString(int year, int month, int day, TradeType type, String ticker) {
        return String.format("%d-%d-%d,%s,%s", year, month, day, type.toString(), ticker);
    }
}
