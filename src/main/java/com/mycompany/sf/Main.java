package com.mycompany.sf;

import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        DataProcessor dataProcessor = new DataProcessor(new DataLibrary());
        FriendTradeAlerts friendTradeAlerts = new FriendTradeAlerts(dataProcessor);
        
        List<String> friendTradeStats = friendTradeAlerts.getFriendTradesLastWeek("Some-user-id");
        friendTradeStats.forEach(System.out::println);
    }
}

