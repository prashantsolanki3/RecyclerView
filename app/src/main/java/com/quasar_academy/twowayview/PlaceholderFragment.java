package com.quasar_academy.twowayview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.DividerItemDecoration;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    LayoutAdapter adapter;
    ArrayList<Post> arraylist = new ArrayList<Post>();
    EditText editsearch;
    SwipeRefreshLayout swipeRefreshLayout;
    private TwoWayView mRecyclerView;
    private Toast mToast;
    private int mLayoutId = R.layout.fragment_my;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(mLayoutId, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.measure(500,500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"Refreshing",Toast.LENGTH_LONG).show();
               // swipeRefreshLayout.setRefreshing(false);
            }
        });
        mToast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        mRecyclerView = (TwoWayView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mRecyclerView.setLongClickable(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        editsearch = (EditText) view.findViewById(R.id.edit_recycler);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        final ItemClickSupport itemClick = ItemClickSupport.addTo(mRecyclerView);
        itemClick.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
                LayoutAdapter.CategoryViewHolder childViewHolder = (LayoutAdapter.CategoryViewHolder) parent.getChildViewHolder(child);
                mToast.setText("Long Clicked " + childViewHolder.post.getTitle());
                mToast.show();
                return true;
            }
        });


        itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View child, int position, long id) {
                LayoutAdapter.CategoryViewHolder childViewHolder = (LayoutAdapter.CategoryViewHolder) parent.getChildViewHolder(child);
                mToast.setText("Clicked " + childViewHolder.post.getUrl());
                mToast.show();
            }

        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }


            boolean loading = true;
            int pastVisiblesItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                /**
                 * Makes SwipeRefreshLayout Work in  RecyclerView
                 */
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);


                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                pastVisiblesItems = recyclerView.getChildAt(0).getTop();

                if (loading) {
                    if ( (visibleItemCount+pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        Log.d("...", "Last Item Wow !");
                    }
                }else{
                    loading =true;
                    Log.d("...", "Loading");
                }
            }
        });
        final Drawable divider = getResources().getDrawable(R.drawable.divider);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(divider));

        loadData();

    }

    public void loadData() {
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                "https://prashantsolanki3.github.io/tut-jsons/recycler_data.json", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
            }
        });
        if (isNetworkConnected())
            AppController.getInstance().addToRequestQueue(jsonReq);
        else {
            editsearch.setEnabled(false);
            mToast.setText("Connect To the Internet");
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("products");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Post item = new Post();
                item.setId(feedObj.getInt("id"));
                item.setTitle(feedObj.getString("title"));
                item.setSlug(feedObj.getString("description"));
                item.setContent(feedObj.getString(("featured_src")));

                arraylist.add(item);
                Log.d("LOAD DATA", item.getId() + " " + item.getTitle());
            }
            adapter = new LayoutAdapter(getActivity(), mRecyclerView, mLayoutId, arraylist);
            mRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}