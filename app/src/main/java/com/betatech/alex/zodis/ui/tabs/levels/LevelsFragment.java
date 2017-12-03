package com.betatech.alex.zodis.ui.tabs.levels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betatech.alex.zodis.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LevelsFragment extends Fragment {

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

        return view;
    }


}
