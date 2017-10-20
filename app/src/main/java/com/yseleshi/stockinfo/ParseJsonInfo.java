package com.yseleshi.stockinfo;


import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

public class ParseJsonInfo {

    private final static String TAG = "stockInfo_HttpURLConn";

    Stock stock;
    ArrayList<Stock> stocks = new ArrayList<>();


    // constructors
    public ParseJsonInfo() {

    }

    @Override
    public String toString() {
        return super.toString() + stock.toString();

    }

    public interface stockCallback {

        void onStock(Stock stock);
    }

    public Stock decodeMessage(String message, stockCallback callback) {





        try {

            JSONObject jObject;
            JSONObject jQueryObj;
            JSONObject jResultsObj;
            JSONObject jQuoteObj;

            Log.d(TAG, "Parsing: " + message);
            stock = new Stock();

            jObject = new JSONObject(message);

            jQueryObj = jObject.getJSONObject(Stock.KEY_QUERY);

            jResultsObj = jQueryObj.getJSONObject(stock.KEY_RESULTS);

            jQuoteObj = jResultsObj.getJSONObject(stock.KEY_QUOTE);

            stock.setName(jQuoteObj.getString(Stock.KEY_NAME));
            stock.setSymbol(jQuoteObj.getString(Stock.KEY_SYMBOL));

            stock.setExchange(jQuoteObj.getString(Stock.KEY_STOCKEXCHANGE));
            stock.setOpen(jQuoteObj.getString(Stock.KEY_OPEN));
            stock.setLastTradePrice(jQuoteObj.getString(Stock.KEY_LASTTRADEPRICEONLY));
            stock.setChange(jQuoteObj.getString(Stock.KEY_CHANGE));
            stock.setChangePercent(jQuoteObj.getString(Stock.KEY_PERCENTCHANGE));
            stock.setDaysHigh(jQuoteObj.getString(Stock.KEY_DAYSHIGH));
            stock.setDaysLow(jQuoteObj.getString(Stock.KEY_DAYSLOW));
            stock.setTextColor("black");

            Log.i(TAG, "StockData: " + stock.getName() + ", " + stock.getSymbol() + ", " + stock.getOpen());

            if (callback != null) {
                callback.onStock(stock);
            }
        } catch (Exception e) {
            Log.e(TAG, "decodeMessage: exception during parsing");
            e.printStackTrace();
            return null;
        }

        return stock;

    }










}
