package com.betatech.alex.zodis.ui.tabs.levels;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.betatech.alex.zodis.R;
import com.betatech.alex.zodis.data.ZodisContract;
import com.betatech.alex.zodis.data.ZodisPreferences;
import com.betatech.alex.zodis.ui.lesson.LessonActivity;
import com.betatech.alex.zodis.ui.lesson.LessonSlidePageFragment;
import com.betatech.alex.zodis.widget.ZodisWidgetService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 12/1/2017.
 */

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelsViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private TypedArray imageArray;

    public LevelsAdapter(Cursor mCursor, Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;

        imageArray = mContext.getResources().obtainTypedArray(R.array.level_image);
    }

    @Override
    public LevelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levels_list,parent,false);
        return new LevelsViewHolder(singleItemLayout);
    }

    @Override
    public void onBindViewHolder(LevelsViewHolder holder, int position) {
        if (mCursor==null) {
            return;
        }

        mCursor.moveToPosition(position);

        int nameColumnIndex = mCursor.getColumnIndex(ZodisContract.LevelEntry.COLUMN_LEVEL_NAME);
        int lessonIdColumnIndex = mCursor.getColumnIndex(ZodisContract.LevelEntry.COLUMN_LESSON_ID);
        int levelStatusColumnIndex = mCursor.getColumnIndex(ZodisContract.LevelEntry.COLUMN_LEVEL_STATUS);

        String levelName = mCursor.getString(nameColumnIndex);
        int lessonId = mCursor.getInt(lessonIdColumnIndex);
        int status = mCursor.getInt(levelStatusColumnIndex);

        holder.imageView.setImageResource(imageArray.getResourceId(position%10, 0));
        holder.levelNameTextView.setText(mContext.getString(R.string.show_level_name,levelName));
        if(status == 1){
            holder.opacityContainerLinearLayout.setAlpha(.87f);
            holder.cardView.setBackground(mContext.getDrawable(R.drawable.card_view_outline));
            holder.imageViewChecked.setVisibility(View.VISIBLE);
        }else{
            holder.opacityContainerLinearLayout.setAlpha(.38f);
        }

        holder.cardView.setTag(R.string.KEY_LESSON_ID,lessonId);
        holder.cardView.setTag(R.string.KEY_LESSON_NAME,levelName);
    }

    public void swapCursor(Cursor cursor){
        if (mCursor!=null) {
            mCursor.close();
        }

        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount():0;
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
            int lessonId = (int) v.getTag(R.string.KEY_LESSON_ID);
            String levelName = (String) v.getTag(R.string.KEY_LESSON_NAME);
            /*

            String selections = ZodisContract.LevelEntry.COLUMN_LESSON_ID + " =?";
            String[] selectionArgs = new String[]{lessonId};
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZodisContract.LevelEntry.COLUMN_LEVEL_STATUS,1);
            mContext.getContentResolver().update(ZodisContract.LevelEntry.CONTENT_URI,contentValues,selections,selectionArgs);
            ZodisPreferences.incrementLessonCompletedPref(mContext);
            ZodisWidgetService.startActionUpdateAllWidgets(mContext);*/

            Intent intent = new Intent(mContext, LessonActivity.class);
            intent.putExtra(LessonActivity.KEY_LESSON_ID,lessonId);
            intent.putExtra(LessonActivity.KEY_LEVEL_NAME,levelName);
            mContext.startActivity(intent);
        }
    }
}
