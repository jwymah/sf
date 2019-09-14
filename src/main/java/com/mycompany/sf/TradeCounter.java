package com.mycompany.sf;

import java.util.concurrent.atomic.AtomicInteger;

class TradeCounter {
    private final String ticker;
    private final AtomicInteger counter;

    public TradeCounter(String ticker) {
        this.ticker = ticker;
        counter = new AtomicInteger();
    }
    
    public void adjustCount(int i) {
        this.counter.addAndGet(i);
    }

    public String getTicker() {
        return ticker;
    }

    public int getCount() {
        return counter.get();
    }

    @Override
    public String toString() {
        if (counter.intValue() > 0) {
            return String.format("%d,%s,%s", getCount(), TradeType.BUY.name(), ticker);
        }
        else if (counter.intValue() < 0) {
            return String.format("%d,%s,%s", -getCount(), TradeType.SELL.name(), ticker);
        }
        return String.format("%d,%s,%s this should not be shown", getCount(), TradeType.SELL.name(), ticker);
    }
}