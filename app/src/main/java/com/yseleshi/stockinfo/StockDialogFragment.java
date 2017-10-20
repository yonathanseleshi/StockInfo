package com.yseleshi.stockinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yseleshi.stockinfo.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dell laptop on 8/1/2017.
 */

public class StockDialogFragment extends DialogFragment {

    ArrayList<Stock> stocks;
    public int position;



    public static StockDialogFragment newInstance (int position, ArrayList<Stock> stocks){
      StockDialogFragment frag = new StockDialogFragment();
        Stock selectedStock = stocks.get(position);

        String open = selectedStock.getOpen();
        String daysHigh = selectedStock.getDaysHigh();
        String daysLow = selectedStock.getDaysLow();
        String exchange = selectedStock.getExchange();



        Bundle args = new Bundle();
        args.putString("open", open);
        args.putString("dayshigh", daysHigh);
        args.putString("dayslow", daysLow);
        args.putString("exchange", exchange);


        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String open = getArguments().getString("open");
        String daysHigh = getArguments().getString("dayshigh");
        String daysLow = getArguments().getString("dayslow");
        String exchange = getArguments().getString("exchange");




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_layout, null);

        TextView diaOpen = (TextView) mView.findViewById(R.id.diaOpen);
        TextView diaDaysHigh = (TextView) mView.findViewById(R.id.diaDaysHigh);
        TextView diaDaysLow = (TextView) mView.findViewById(R.id.diaDaysLow);
        TextView diaExchange = (TextView) mView.findViewById(R.id.diaExchange);





        diaOpen.setText(open);
        diaDaysHigh.setText(daysHigh);
        diaDaysLow.setText(daysLow);
        diaExchange.setText(exchange);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(mView)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        StockDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StockDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
