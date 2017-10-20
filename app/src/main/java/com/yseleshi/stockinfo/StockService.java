package com.yseleshi.stockinfo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.yseleshi.stockinfo.R;
import java.nio.file.ReadOnlyFileSystemException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StockService extends IntentService implements VolleyClassString.OnInfoListener<String> {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_REFRESH_STOCK = "com.yseleshi.stockinfo.action.REFRESH_STOCK";
    //private static final String ACTION_BAZ = "com.yseleshi.stock.action.BAZ";

    // TODO: Rename parameters
    private static final String PREF_MIN_PERCENT_CHANGE = Constants.PREF_MIN_PERCENT_CHANGE;
    private static final String PREF_COLOR_CHOICE = Constants.PREF_COLOR_CHOICE;

    private final String TAG = "StockService";
    VolleyClassString volleyClassString;
    ParseJsonInfo stockParser;

    public String minimumPerc = "+3.00";
    public String color_choice = "black";
    public Notification StockNotification;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    public static final int NOTIFICATION_ID = 1;
    private DBHelper dbHelper;
    ArrayList<Stock> stockList = new ArrayList<>();


    public String queryBaseString = "http://query.yahooapis.com/v1/public/yql";
    String query = "q";
    String selectParameter = "Select * from yahoo.finance.quotes where symbol in (\"" ;
    String selectParameterEnd = "\")";
    String envKey = "env";
    String datatables = "store://datatables.org/alltableswithkeys" ;
    String formatKey = "format";
    String formatType = "json";
    String stockSymbol;

    public StockService() {
        super("StockService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startRefreshStocks(Context context, String param1, String param2) {
        Intent intent = new Intent(context, StockService.class);
        intent.setAction(ACTION_REFRESH_STOCK);
        intent.putExtra(PREF_MIN_PERCENT_CHANGE, param1);
        intent.putExtra(PREF_COLOR_CHOICE, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    /*
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, StockService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(PREF_MIN_PERCENT_CHANGE, param1);
        intent.putExtra(PREF_COLOR_CHOICE, param2);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REFRESH_STOCK.equals(action)) {
                final String param1 = intent.getStringExtra(PREF_MIN_PERCENT_CHANGE );
                final String param2 = intent.getStringExtra(PREF_COLOR_CHOICE);
                handleRefreshStock(param1, param2);
            /*} else if (ACTION_BAZ.equals(action)) {
                final int param1 = intent.getStringExtra(PREF_MIN_PERCENT_CHANGE);
                final long param2 = intent.getStringExtra(PREF_COLOR_CHOICE);
                handleRefreshEarthquake(param1, param2);
            */}
        }
    }


    @Override
    public void onInfoAvailable(String responseString) {
        Log.v(TAG, "onInfoAvailable " + responseString);
        if (stockParser == null) {
            stockParser = new ParseJsonInfo();
        }
        stockParser.decodeMessage(responseString, new ParseJsonInfo.stockCallback(){
            @Override
            public void onStock(Stock stock) {


                String chg = stock.getChangePercent();

                DecimalFormat df = new DecimalFormat("+####.##%;-####.##%");
                DecimalFormat sf = new DecimalFormat("+##.##%;-##.##%");


                try {

                  /*  Double chgP;
                    Double minPCh;

                    chgP = (Double)df.parse(chg);
                    minPCh = (Double)df.parse(minimumPerc);
                     */

                    Float minPCh;
                    Float chgP;
                    Number ch;
                    Number min;
                    String change;
                    String minimum;

                    ch = df.parse(chg);
                    min = sf.parse(minimumPerc);

                    change = "" + ch;
                    minimum = "" + min;

                    chgP = Float.parseFloat(change);
                    minPCh = Float.parseFloat(minimum);

                    Log.d( TAG, "Stock Service - " + "chgP: " + chgP + "minPCh:" + minPCh);

                    if (minPCh > 0){
                        if (chgP > minPCh){


                            notificationManager.notify(NOTIFICATION_ID, StockNotification);
                            Log.d(TAG, "onInfoAvailable update stock info" + stock.logStockData());


                        }
                    }
                    else if (minPCh < 0){
                        if (chgP < minPCh){


                            notificationManager.notify(NOTIFICATION_ID, StockNotification);
                            Log.d(TAG, "onInfoAvailable update stock info" + stock.logStockData());


                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void makeNotification(){
        Log.d(TAG,"makeNotification entry");
        String svcName = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager)getSystemService(svcName);
        Context context = getApplicationContext();
        Intent startActivityIntent = new Intent(StockService.this, com.yseleshi.stockinfo.StockNotification.class);
        PendingIntent launchIntent = PendingIntent.getActivity(context,0, startActivityIntent, 0);
        Resources res = context.getResources();

        builder = new NotificationCompat.Builder(context);
        int icon = R.mipmap.ic_launcher;



        builder.setSmallIcon(icon)
                .setTicker(res.getString(R.string.expandedTitle))
                .setWhen(java.lang.System.currentTimeMillis())
                .setContentTitle(res.getString(R.string.expandedTitle))
                .setContentText(res.getString(R.string.expandedText))
                .setWhen(java.lang.System.currentTimeMillis())
                .setContentIntent(launchIntent);

        StockNotification = builder.build();

    }


    private void refreshStocks(){


        if(stockList.isEmpty() != true) {
            stockList.clear();
        }

        stockList.addAll(StockListFragment.mStockData);
        StockListFragment.mStockData.clear();

        if (dbHelper != null){

            dbHelper.deleteAll();

        }

        try {

            dbHelper = new DBHelper(getApplicationContext());



        } catch (Exception e) {
            e.printStackTrace();
        }


        if (volleyClassString == null) {
            volleyClassString = new VolleyClassString(this, this);
        }
        for(int i = 0; i < stockList.size(); i++) {

            String desiredStockQuery = selectParameter + stockList.get(i).getSymbol() + selectParameterEnd;

            // query the yahoo finance site.
            final String yahooQuery = Uri.parse(queryBaseString).buildUpon()
                    .appendQueryParameter(query, desiredStockQuery)
                    .appendQueryParameter(envKey, datatables)
                    .appendQueryParameter(formatKey, formatType)
                    .build().toString();

            volleyClassString.makeNetworkRequests(yahooQuery);


        }
    }



    private void handleRefreshStock(String param1, String param2) {
        minimumPerc = param1;
        color_choice = param2;
        makeNotification();
        Log.d(TAG, "onHandleIntent: minimumPercentChange " + minimumPerc
                + " color_choice: " + color_choice);
        refreshStocks();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */

    /*
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
