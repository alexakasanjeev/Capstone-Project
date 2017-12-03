package com.betatech.alex.zodis.ui.tabs.levels;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LevelsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 876;
    @BindView(R.id.recycler_levels) RecyclerView levelsList;
    private LevelsAdapter mAdapter;

    public LevelsFragment() {
        // Required empty public constructor
    }

    public static LevelsFragment newInstance() {
        LevelsFragment fragment = new LevelsFragment();;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        ButterKnife.bind(this,view);

        mAdapter = new LevelsAdapter(null,getActivity());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        manager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // 2 column size for first row
                        return (position % 3 == 0 ? 2 : 1);
                    }
                });
        levelsList.setLayoutManager(manager);
        levelsList.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ZodisContract.LevelEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null &&data.getCount()>0) {
            mAdapter.swapCursor(data);
        }else{
            // TODO: 12/1/2017 Show error message, database problem
            Toast.makeText(getActivity(), "Level Database is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
