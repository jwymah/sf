package com.mycompany.sf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

public class DataProcessor {
    private static final Logger LOG = Logger.getLogger(DataProcessor.class);
    private final DataLibrary dataLibrary;

    public DataProcessor(DataLibrary dataLibrary) {
        this.dataLibrary = dataLibrary;
    }
    
    /**
     * Get trades of friends that are in the specified date range.
     * Returned in a map of ticker String and TradeCounter
     * @param userId
     * @param lowerDate
     * @param upperDate
     * @return 
     */
    public Map<String, TradeCounter> getCountsOfFriendTrades(String userId, Date lowerDate, Date upperDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, TradeCounter> map = new HashMap();
        for (String friendId : dataLibrary.getFriendsListForUser(userId)) {
            for (String tradeString : dataLibrary.getTradeTransactionsForUser(friendId)) {
                String[] split = tradeString.split(",| ");
                Trade trade;
                try {
                    trade = new Trade(sdf.parse(split[0]),
                            TradeType.valueOf(split[1].toUpperCase()),
                            split[2].toUpperCase());
                    // there is an assumption the trades are sorted in reverse-chronological order,
                    // so skip until end of last week, compute values until start of last week.
                    if (trade.getDate().after(upperDate) || trade.getDate().equals(upperDate)) {
                        continue;
                    }
                    if (trade.getDate().before(lowerDate)) {
                        break;
                    }
                } catch (ParseException ex) {
                    LOG.error("Could not parse date from string: " + split[0], ex);
                    continue;
                }
                if (!map.containsKey(trade.getTicker())) {
                    map.put(trade.getTicker(), new TradeCounter(trade.getTicker()));
                }
                map.get(trade.getTicker()).adjustCount(trade.getDiff());
            }
        }
        return map;
    }
    
    /**
     * Given a collection of TradeCounter, filters out count==0 and orders by descending count
     * @param values Collection of TradeCounter
     * @return List sorted and filtered list of TradeCounter
     */
    //TODO: what is being exposed that netbeans is warning about ?
    public List<TradeCounter> filterAndSortTradeCounters(Collection<TradeCounter> values) {
        return values.stream()
                .filter(trade -> trade.getCount() != 0)
                .sorted((trade1, trade2) -> -Integer.compare(trade1.getCount(), trade2.getCount()))
                .collect(Collectors.toList());
    }
    
    //date utils
    /**
     * Get 12:00:000am of Sunday of the previous week for the given date
     * Uses default time zone
     * @param date
     * @return 
     */
    public Date getPreviousWeekStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartDate(date));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        return cal.getTime();
    }
    
    /**
     * Get 12:00:000am of Sunday for the week the given date.
     * Uses default time zone
     * @param date
     * @return 
     */
    public Date getWeekStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
