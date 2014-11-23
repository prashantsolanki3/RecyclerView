package com.quasar_academy.twowayview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.SpannableGridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

    private final Context mContext;
    private final TwoWayView mRecyclerView;
    private final int mLayoutId;
    private ArrayList<Product> arraylist;
    private List<Product> productlist = null;

    public LayoutAdapter(Context context, TwoWayView recyclerView, int layoutId, List<Product> productlist) {
        mContext = context;
        this.productlist = productlist;
        mRecyclerView = recyclerView;
        mLayoutId = layoutId;
        this.arraylist = new ArrayList<Product>();
        this.arraylist.addAll(productlist);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productlist.clear();
        if (charText.length() == 0) {
            productlist.addAll(arraylist);
        } else {
            for (Product wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        holder.setData(mContext, productlist.get(position));

        boolean isVertical = (mRecyclerView.getOrientation() == TwoWayLayoutManager.Orientation.VERTICAL);

        final View itemView = holder.itemView;

        final int itemId = position;

        if (mLayoutId == R.layout.fragment_my) {
            final SpannableGridLayoutManager.LayoutParams lp =
                    (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();


            final int span1 = (itemId == 0 || itemId == 3 ? 2 : 1);
            final int span2 = (itemId == 0 ? 2 : (itemId == 3 ? 3 : 1));


            final int colSpan = (isVertical ? span2 : span1);
            final int rowSpan = (isVertical ? span1 : span2);


            if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
                lp.rowSpan = rowSpan;
                lp.colSpan = colSpan;
                itemView.setLayoutParams(lp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView featured_src;
        public Product product;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            featured_src = (ImageView) view.findViewById(R.id.featured_src);

        }

        public void setData(Context mContext, Product product) {
            this.product = product;
            if (title != null)
                title.setText(product.getTitle().toString());
            if (featured_src != null)
                Picasso.with(mContext).load(product.getFeatured_src()).into(featured_src);

        }
    }
} 