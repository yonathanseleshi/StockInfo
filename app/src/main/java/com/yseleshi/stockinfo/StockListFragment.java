package com.yseleshi.stockinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockListFragment extends Fragment implements VolleyClassString.OnInfoListener<String>, StockRecyclerViewAdapter.OnAdapterItemInteraction {


   StockRecyclerViewAdapter stockAdapter;
    public static ArrayList<Stock> mStockData = new ArrayList<>();
    ArrayList<Stock> stockList = new ArrayList<>();
    private final static String TAG = "StockList Fragment";
    private final static int STOCK_PREFERENCES = 1;


    public String minimumPercentChange = "+3.00%";

    public String colorChoice = "black";
    public DBHelper dbHelper = null;

    public String queryBaseString = "http://query.yahooapis.com/v1/public/yql";
    String query = "q";
    String selectParameter = "Select * from yahoo.finance.quotes where symbol in (\"" ;
    String selectParameterEnd = "\")";
    String envKey = "env";
    String datatables = "store://datatables.org/alltableswithkeys" ;
    String formatKey = "format";
    String formatType = "json";
    String stockSymbol;
    Stock stock;




    public boolean autoUpdateChecked = false;
    public int updateFreq = 0;


    StockAlarm stockAlarm;

    private OnListFragmentInteractionListener mListener;

    private VolleyClassString volleyClassString;

    public StockListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_list, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        try {
            dbHelper = new DBHelper(getContext());
            if (dbHelper.selectAll() != null) {
                mStockData.addAll(dbHelper.selectAll());
            }
        }
        catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();

        }
       Fragment symbolSearch;

        RecyclerView recyclerView = getActivity().findViewById(R.id.stockListRecView);






        stockAdapter = new StockRecyclerViewAdapter(mStockData, this);
        recyclerView.setAdapter(stockAdapter);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
/*
        getStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stockSymbol = getSymbol.getText().toString();
                getNetworkInfo(stockSymbol);

            }
        });
*/

    }



    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentStockDeletedInteraction(Stock stock);
        void onFragmentStockAddedInteraction(Stock stock);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), STOCK_PREFERENCES);
                return true;
            case R.id.action_refresh:
                Log.d(TAG, "onOptionsItemSelected: action_refresh");
                updateFromPreferences();
                StockService.startRefreshStocks(getActivity(), minimumPercentChange, colorChoice);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        Log.d(TAG, "onActivityResult reqCode:  " + reqCode + " resCode: " + resCode);
        switch(reqCode) {
            case STOCK_PREFERENCES: {
                refreshStocks();
                break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_stock_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    public void updateFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        minimumPercentChange = prefs.getString(Constants.PREF_MIN_PERCENT_CHANGE, "3");

        colorChoice = prefs.getString(Constants.PREF_COLOR_CHOICE, "black");




        updateFreq = Integer.parseInt(prefs.getString(Constants.PREF_UPDATE_FREQ, "60"));
        autoUpdateChecked = prefs.getBoolean(Constants.PREF_AUTO_UPDATE, false);


        if (stockAlarm == null) {
            stockAlarm = new StockAlarm(getActivity());
            stockAlarm.setUpAlarms();
        }
    }

    // Call the network to get the stock list.
    public void refreshStocks() {

        if(stockList.isEmpty() != true) {
            stockList.clear();
        }
        stockList.addAll(mStockData);
        mStockData.clear();
        updateFromPreferences();

        if (dbHelper != null){

            dbHelper.deleteAll();

        }

        try {
            dbHelper = new DBHelper(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (volleyClassString == null){
            volleyClassString = new VolleyClassString(getActivity(), this);
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

    @Override
    public void onInfoAvailable(String responseString) {


        if(responseString != null) {


            ParseJsonInfo parseJsonInfo = new ParseJsonInfo();



            stock = parseJsonInfo.decodeMessage(responseString, new ParseJsonInfo.stockCallback() {

                @Override
                public void onStock(Stock stock) {
                    if (stock != null) {



                        String chg = stock.getChangePercent();

                        DecimalFormat df = new DecimalFormat("+####.##%;-####.##%" );
                        DecimalFormat sf = new DecimalFormat("+####.##%;-####.##%" );


                        try {

                            /*Double chgP;
                            Double minPCh;
                            Double negMinPCh;
                            chgP = (Double)df.parse(chg);
                            minPCh = (Double)df.parse(minimumPercentChange);

                             */

                            Float minPCh;
                            Float chgP;
                            Number ch;
                            Number min;
                            String change;
                            String minimum;

                            ch = df.parse(chg);
                            min = sf.parse(minimumPercentChange);

                            change = "" + ch;
                            minimum = "" + min;

                            chgP = Float.parseFloat(change);
                            minPCh = Float.parseFloat(minimum);

                            Log.d( TAG, "chgP: " + chgP + "minPCh:" + minPCh);

                            if (minPCh > 0){
                              if (chgP > minPCh){


                                stock.setTextColor(colorChoice);

                              }
                            }
                            else if (minPCh < 0){
                              if (chgP < minPCh){


                                stock.setTextColor(colorChoice);

                              }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }





                    }
                }
            });
            //stocks = parseJsonInfo.decodeMessage(responseString);
            addStock(stock);
        }
    }



    @Override
    public void onItemSelected(StockRecyclerViewAdapter.ViewHolder holder, Integer position, ArrayList<Stock> stockList) {


        //setupDialog(position, stockList);
/*
           Stock selectedEarthquake = stockList.get(position);

        TextView diaAddress = (TextView) getActivity().findViewById(R.id.addressDialog);
        TextView diaDate = (TextView)getActivity().findViewById(R.id.dateDialog);
        TextView diaMag = (TextView) getActivity().findViewById(R.id.magDialog);
        TextView diaLat = (TextView) getActivity().findViewById(R.id.latDialog);
        TextView diaLong = (TextView) getActivity().findViewById(R.id.longDialog);

        String address = selectedEarthquake.getLocation();
        Log.i(TAG, address);
        Log.i(TAG, selectedEarthquake.getDateTimeOf().toString());

        diaAddress.setText(address);
        diaDate.setText(selectedEarthquake.getDateTimeOf().toString());
        diaMag.setText(String.valueOf(selectedEarthquake.getMagnitude()));
        diaLat.setText(String.valueOf(selectedEarthquake.getLatitude()));
        diaLong.setText(String.valueOf(selectedEarthquake.getLongitude()));
        */

        ((StockList)getActivity()).showDialog(position, stockList);


    }

    @Override
    public void onItemLongSelected(StockRecyclerViewAdapter.ViewHolder holder, Integer position, ArrayList<Stock> stockList) {
        Stock selectedStock = stockList.get(position);
        deleteStock(selectedStock);

    }


    public void deleteStock(Stock stock){
        if (stock != null) {
            String item = "deleting: " + stock.getName();
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemClick: " + stock.getName());

            // database delete record
            if (dbHelper != null) dbHelper.deleteRecord(stock.getId());




            // Removes the object from the array
            stockAdapter.remove(stock);
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onFragmentStockDeletedInteraction(stock);
            }
        }
    }

    public void addStock(Stock stock) {
        long stockId = 0;
        if (dbHelper != null) {

            stockId = dbHelper.insert(stock);
            stock.setId(stockId);
        }

        mStockData.add(stock);
        //stockAdapter.add(stock);
        stockAdapter.notifyDataSetChanged();

//        Log.d(TAG, " addStock: " + stock.getName() );

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentStockAddedInteraction(stock);
        }
    }


    public void getNetworkInfo(String stockSymbol) {

        String desiredStockQuery = selectParameter + stockSymbol + selectParameterEnd;

        // query the yahoo finance site.
        final String yahooQuery = Uri.parse(queryBaseString).buildUpon()
                .appendQueryParameter(query, desiredStockQuery)
                .appendQueryParameter(envKey, datatables)
                .appendQueryParameter(formatKey, formatType)
                .build().toString();



        Log.d( TAG, ": getNetworkInfo url: " + queryBaseString);

        if (volleyClassString == null){
            volleyClassString = new VolleyClassString(getActivity(), this);
        }
        volleyClassString.makeNetworkRequests(yahooQuery);

    }


    private void setupDialog(int position, ArrayList<Stock> stockList){

        Stock selectedStock = stockList.get(position);
        TextView diaOpen = (TextView) getActivity().findViewById(R.id.diaOpen);
        TextView diaDaysHigh = (TextView)getActivity().findViewById(R.id.diaDaysHigh);
        TextView diaDaysLow = (TextView) getActivity().findViewById(R.id.diaDaysLow);
        TextView diaExchange = (TextView) getActivity().findViewById(R.id.diaExchange);



        diaOpen.setText(selectedStock.getOpen());
        diaDaysHigh.setText(selectedStock.getDaysHigh());
        diaDaysLow.setText(selectedStock.getDaysLow());
        diaExchange.setText(selectedStock.getExchange());


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
