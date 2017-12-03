package com.betatech.alex.zodis.ui.tabs.insights;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InsightsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 786;
    @BindView(R.id.recycler_insights) RecyclerView insightsList;
    @BindView(R.id.text_insights_message) TextView insightsMessageTextView;
    private InsightsAdapter mAdapter;


    public InsightsFragment() {
        // Required empty public constructor
    }

    public static InsightsFragment newInstance() {
        InsightsFragment fragment = new InsightsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_insights, container, false);
        ButterKnife.bind(this,view);

        mAdapter = new InsightsAdapter(null,getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(insightsList.getContext(),
                manager.getOrientation());
        insightsList.addItemDecoration(dividerItemDecoration);
        insightsList.setLayoutManager(manager);
        insightsList.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections=new String[]{ZodisContract.RootEntry._ID,ZodisContract.RootEntry.COLUMN_NAME, ZodisContract.RootEntry.COLUMN_DESCRIPTION};
        String selections = ZodisContract.RootEntry.COLUMN_STATUS + " = ?";
        String[] selectionArgs=new String[]{"1"};
        return new CursorLoader(getActivity(), ZodisContract.RootEntry.CONTENT_URI,projections,selections,selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null &&data.getCount()>0) {
            showList();
            mAdapter.swapCursor(data);
        }else if(data!=null && data.getCount() ==0){
            hideList();
        }else{
            // TODO: 12/1/2017 Show error message, database problem
            Toast.makeText(getActivity(), "Algo problemo con database", Toast.LENGTH_SHORT).show();
        }
    }


    private void showList(){
        insightsMessageTextView.setVisibility(View.GONE);
        insightsList.setVisibility(View.VISIBLE);
    }

    private void hideList(){
        insightsMessageTextView.setVisibility(View.VISIBLE);
        insightsList.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
