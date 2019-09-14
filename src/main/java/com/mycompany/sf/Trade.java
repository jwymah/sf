package com.mycompany.sf;

import java.util.Date;


class Trade {
    private final Date date;
    private final TradeType tradeType;
    private final String ticker;

    public Trade(Date date, TradeType tradeType, String ticker) {
        this.date = date;
        this.tradeType = tradeType;
        this.ticker = ticker;
    }

    public Date getDate() {
        return date;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public String getTicker() {
        return ticker;
    }
    
    public int getDiff() {
        if (tradeType.equals(TradeType.BUY)) {
            return 1;
        }
        else if (tradeType.equals(TradeType.SELL)) {
            return -1;
        }
        return 0;
    }
}
