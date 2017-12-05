package com.betatech.alex.zodis.ui.lesson;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.pojo.DerivedWord;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/4/2017.
 */

public class DerivedWordAdapter extends RecyclerView.Adapter<DerivedWordAdapter.DerivedWordViewHolder>{

    private ArrayList<DerivedWord> derivedWords;
    private Context mContext;

    public DerivedWordAdapter(Context mContext,ArrayList<DerivedWord> derivedWords) {
        this.derivedWords = derivedWords;
        this.mContext = mContext;
    }

    @Override
    public DerivedWordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_derived_word,parent,false);
        return new DerivedWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DerivedWordViewHolder holder, int position) {
        if (derivedWords==null) return;

        holder.nameTextView.setText(derivedWords.get(position).getDerivedWordName());
        holder.descriptonTextView.setText(derivedWords.get(position).getDerivedWordDescription());
    }

    @Override
    public int getItemCount() {
        return derivedWords!=null?derivedWords.size():0;
    }

    public void swapList(ArrayList<DerivedWord> words){
        derivedWords = words;
        notifyDataSetChanged();
    }

    class DerivedWordViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_derived_word_name) TextView nameTextView;
        @BindView(R.id.text_deried_word_description) TextView descriptonTextView;

        public DerivedWordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
