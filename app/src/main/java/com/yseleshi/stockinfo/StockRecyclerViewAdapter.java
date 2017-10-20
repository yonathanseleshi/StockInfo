package com.yseleshi.stockinfo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yseleshi.stockinfo.R;

import java.util.ArrayList;


public class StockRecyclerViewAdapter extends RecyclerView.Adapter<StockRecyclerViewAdapter.ViewHolder>  {



    public interface OnAdapterItemInteraction {
        void onItemSelected(ViewHolder holder, Integer position, ArrayList<Stock> stockList);
        void onItemLongSelected(ViewHolder holder, Integer position, ArrayList<Stock> stockList);


    }


    public ArrayList<Stock> stocks = new ArrayList<>();
    private final OnAdapterItemInteraction mListener;

    public StockRecyclerViewAdapter(ArrayList<Stock> stocks, OnAdapterItemInteraction listener) {
        this.stocks = stocks;
        mListener = listener;
    }

    @Override
    public StockRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final StockRecyclerViewAdapter.ViewHolder holder, final int position) {

        Stock stock = stocks.get(position);

        String stockColor = stock.getTextColor();
        holder.price.setText(stock.getLastTradePrice());
        holder.company.setText(stock.getName());
        holder.chgPrice.setText(stock.getChange());
        holder.symbol.setText(stock.getSymbol());
        holder.chgPerc.setText(stock.getChangePercent());
        holder.price.setTextColor(Color.parseColor(stockColor));
        holder.chgPerc.setTextColor(Color.parseColor(stockColor));
        holder.chgPrice.setTextColor(Color.parseColor(stockColor));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   // Stock selectedQuake = stocks.get(position);
                    mListener.onItemSelected(holder, position, stocks);
                }

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mListener.onItemLongSelected(holder, position, stocks);
                return true;
            }
        });
    }




    public void add(Stock item){
        int count = getItemCount();
        add(item,count);
    }

    public void add(Stock item, int position) {
        stocks.add(item);
        notifyItemInserted(stocks.size()-1);
    }

    public void remove(int position){
        stocks.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(Stock item) {
        int position = stocks.indexOf(item);
        remove(position);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public final TextView company;
        public final TextView symbol;
        public final TextView price;
        public final TextView chgPrice;
        public final TextView chgPerc;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            company = (TextView)mView.findViewById(R.id.compName);
            symbol = (TextView)mView.findViewById(R.id.symbolName);

            price = (TextView)mView.findViewById(R.id.lastTradePrice);
            chgPrice = (TextView)mView.findViewById(R.id.changePrice);
            chgPerc = (TextView)mView.findViewById(R.id.percent_changeTxt);
        }


    }


}
