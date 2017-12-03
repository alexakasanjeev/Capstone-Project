package com.betatech.alex.zodis.ui.tabs.insights;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/1/2017.
 */

public class InsightsAdapter extends RecyclerView.Adapter<InsightsAdapter.InsightsViewHolder>{

    /* To show table header*/
    private static final int EXTRA_ROWS = 1;
    private Cursor mCursor;
    private Context mContext;

    public InsightsAdapter(Cursor mCursor,Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @Override
    public InsightsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insights_list,parent,false);
        return new InsightsViewHolder(singleItemLayout);
    }

    @Override
    public void onBindViewHolder(InsightsViewHolder holder, int position) {
        if (mCursor==null) {
            return;
        }

        if (position==0) {
            formatHeader(holder, mContext.getString(R.string.insights_root_header), mContext.getString(R.string.insights_description_header));
            return;
        }

        mCursor.moveToPosition(position-1);

        int nameColumnIndex = mCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_NAME);
        int descriptionColumnIndex = mCursor.getColumnIndex(ZodisContract.RootEntry.COLUMN_DESCRIPTION);

        holder.nameTextView.setText(mCursor.getString(nameColumnIndex));
        holder.descriptionTextView.setText(mCursor.getString(descriptionColumnIndex));
    }

    private void formatHeader(InsightsViewHolder holder, String string, String string2) {
        holder.nameTextView.setText(string);
        holder.nameTextView.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
        holder.nameTextView.setTypeface(null, Typeface.NORMAL);
        holder.descriptionTextView.setText(string2);
        holder.descriptionTextView.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount()+EXTRA_ROWS:0;
    }

    public void swapCursor(Cursor cursor){
        if (mCursor!=null) {
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class InsightsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_insights_name) TextView nameTextView;
        @BindView(R.id.text_insights_description) TextView descriptionTextView;

        public InsightsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
