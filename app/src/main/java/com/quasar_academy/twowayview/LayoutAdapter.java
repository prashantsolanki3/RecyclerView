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


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.CategoryViewHolder> {

    private final Context mContext;
    private final TwoWayView mRecyclerView;
    private final int mLayoutId;
    private ArrayList<Post> arraylist;
    private List<Post> postlist = null;

    public LayoutAdapter(Context context, TwoWayView recyclerView, int layoutId, List<Post> postlist) {
        mContext = context;
        this.postlist = postlist;
        mRecyclerView = recyclerView;
        mLayoutId = layoutId;
        this.arraylist = new ArrayList<Post>();
        this.arraylist.addAll(postlist);
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        postlist.clear();
        if (charText.length() == 0) {
            postlist.addAll(arraylist);
        } else {
            for (Post wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || wp.getContent().toLowerCase(Locale.getDefault()).contains(charText)) {
                    postlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        holder.setData(mContext, postlist.get(position));

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
        return postlist.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView featured_src;
        public Post post;

        public CategoryViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            featured_src = (ImageView) view.findViewById(R.id.featured_src);

        }

        public void setData(Context mContext, Post post) {

            this.post = post;
            if (title != null)
                title.setText(post.getTitle().toString());
            /*if (featured_src != null)
                Picasso.with(mContext).load(post.getFeatured_src()).into(featured_src);*/

        }
    }
} 