package com.mycompany.sf;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendTradeAlerts {
    private final DataProcessor dataProcessor;
    
    public FriendTradeAlerts(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    /**
     * Retrieve friend trades for last week
     * @param userId
     * @return 
     */
    public List<String> getFriendTradesLastWeek(String userId) {
        Date now = new Date();
        Date lowerDate = dataProcessor.getPreviousWeekStartDate(now);
        Date upperDate = dataProcessor.getWeekStartDate(now);
        
        return getFriendTradesInRange(userId, lowerDate, upperDate);
    }

    /**
     * retrieve friend trades for the specified period in the range [lowerDate, upperDate)
     * @param userId
     * @param lowerDate
     * @param upperDate
     * @return 
     */
    public List<String> getFriendTradesInRange(String userId, Date lowerDate, Date upperDate) {
        Map<String, TradeCounter> map = dataProcessor.getCountsOfFriendTrades(userId, lowerDate, upperDate);
        
        return dataProcessor.filterAndSortTradeCounters(map.values())
                .stream()
                .map(trade -> trade.toString())
                .collect(Collectors.toList());
    }
}