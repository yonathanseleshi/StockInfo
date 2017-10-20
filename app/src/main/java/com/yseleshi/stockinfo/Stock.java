package com.yseleshi.stockinfo;


public class Stock {



    private String name;
    private String exchange;
    private String lastTradePrice;



    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    private String textColor;
    private String changePercent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public Stock(String name, String symbol, String exchange, String lastTradePrice, String change, String textColor, String changePercent, String daysHigh, String daysLow, String open) {
        this.name = name;
        this.exchange = exchange;
        this.lastTradePrice = lastTradePrice;
        this.change = change;
        this.textColor = textColor;
        this.symbol = symbol;
        this.changePercent = changePercent;
        this.open = open;
        this.daysHigh = daysHigh;
        this.daysLow = daysLow;
    }

    private String change;
    private String symbol;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getDaysLow() {
        return daysLow;
    }

    public void setDaysLow(String daysLow) {
        this.daysLow = daysLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public void setDaysHigh(String daysHigh) {
        this.daysHigh = daysHigh;
    }

    private String open;
    private String daysLow;
    private String daysHigh;

    public static final String KEY_QUERY = "query";
    public static final String KEY_RESULTS = "results";
    public static final String KEY_QUOTE = "quote";

    public static final String KEY_SYMBOL = "Symbol";
    public static final String KEY_NAME = "Name";
    public static final String KEY_STOCKEXCHANGE = "StockExchange";
    public static final String KEY_LASTTRADEPRICEONLY = "LastTradePriceOnly";
    public static final String KEY_CHANGE = "Change";
    public static final String KEY_PERCENTCHANGE = "PercentChange";
    public static final String KEY_OPEN = "Open";
    public static final String KEY_DAYSLOW = "DaysLow";
    public static final String KEY_DAYSHIGH = "DaysHigh";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(String lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Stock(){


    }




    public String logStockData(){

        return "Name: " + getName() + " Exchange: " + getExchange()
                + " ClosingPrice: " + getLastTradePrice()
                + " Change: " + getChange()
                + " Date: " + getSymbol()
                ;

    }



}
