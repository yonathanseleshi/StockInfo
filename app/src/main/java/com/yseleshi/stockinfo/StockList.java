package com.yseleshi.stockinfo;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.yseleshi.stockinfo.R;

import java.util.ArrayList;

public class StockList extends AppCompatActivity implements StockListFragment.OnListFragmentInteractionListener, StockSymbolSearchFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container1, new StockListFragment())
                .add(R.id.fragment_container, new StockSymbolSearchFragment())
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(int position, ArrayList<Stock> stocks){
        DialogFragment dialog = StockDialogFragment.newInstance(position, stocks);
        dialog.show(getSupportFragmentManager(), "Stock Dialog");


    }

    @Override
    public void onFragmentStockDeletedInteraction(Stock stock) {

    }

    @Override
    public void onFragmentStockAddedInteraction(Stock stock) {

    }

    @Override
    public void onButtonClick(String symbol) {

        StockListFragment stockListFrag = (StockListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container1);
        if(stockListFrag !=  null) {
            stockListFrag.getNetworkInfo(symbol);
        }
    }
}
