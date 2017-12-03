package com.betatech.alex.zodis.ui.tabs.levels;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.ui.lesson.LessonActivity;
import com.betatech.alex.zodis.ui.tabs.insights.InsightsAdapter;
import com.betatech.alex.zodis.utilities.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/1/2017.
 */

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelsViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public LevelsAdapter(Cursor mCursor, Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @Override
    public LevelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levels_list,parent,false);
        return new LevelsViewHolder(singleItemLayout);
    }

    @Override
    public void onBindViewHolder(LevelsViewHolder holder, int position) {
        if(position == 0){
            holder.opacityContainerLinearLayout.setAlpha(.87f);
            holder.cardView.setBackground(mContext.getDrawable(R.drawable.card_view_outline));
            holder.imageViewChecked.setVisibility(View.VISIBLE);
        }else{
            holder.opacityContainerLinearLayout.setAlpha(.38f);
        }

        if (mCursor==null) {
            return;
        }

        mCursor.moveToPosition(position);
        // TODO: 12/1/2017 set text
        // TODO: 12/2/2017 Set all these data with correct value
    }

    public void swapCursor(Cursor cursor){
        if (mCursor!=null) {
            mCursor.close();
        }

        mCursor = cursor;
        notifyDataSetChanged();
    }

    // TODO: 12/1/2017 Remove default 10 empty rows
    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount():10;
    }

    class LevelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.card_level_container) CardView cardView;
        @BindView(R.id.linear_level_opacity_container) LinearLayout opacityContainerLinearLayout;
        @BindView(R.id.image_level_pic) ImageView imageView;
        @BindView(R.id.image_tick) ImageView imageViewChecked;
        @BindView(R.id.text_level_name) TextView levelNameTextView;

        public LevelsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            mContext.startActivity(new Intent(mContext, LessonActivity.class));
            // TODO: 12/2/2017 Remove notidication code from here
            NotificationUtils.notifyUserToPractise(mContext);
        }
    }
}
