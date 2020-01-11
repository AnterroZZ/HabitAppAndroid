package com.anterroz.trainingproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anterroz.trainingproject.database.HabitEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitsViewHolder> {

    private Context mContext;
    private List<HabitEntry> mHabitEntry;
    final private ItemClickListener mItemClickListener;



    public HabitsAdapter(Context context,ItemClickListener listener)
    {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public HabitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /* Inflating RecyclerView with single_habit_layout */
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_habit_layout,parent,false);
        return new HabitsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitsViewHolder holder, int position) {

        /* Taking data from a specified entry of mHabitEntry and binding it
        to a RecyclerView ViewHolder
         */
        HabitEntry habitEntry = mHabitEntry.get(position);
        String title = habitEntry.getTitle();
        String category = habitEntry.getCategory();
        int imageViewId = habitEntry.getImageViewId();
        int time = habitEntry.getTimeInSeconds();


        holder.habitTitle.setText(title);
        holder.habitImage.setImageResource(imageViewId);
        holder.habitCategory.setText(category);
        int timeMinutes = time/60;
        if(timeMinutes>59)
        {
            String output = String.format("In %d hours",time);
            holder.habitTime.setText(output);
        }else if(timeMinutes<59){
            String output = String.format("In %d minute",time);
            holder.habitTime.setText(output);
        }else if(timeMinutes>60*24)
        {
            String output = String.format("In %d days",time);
            holder.habitTime.setText(output);
        }

    }

    @Override
    public int getItemCount() {
        if (mHabitEntry == null) {
            return 0;
        }
        return mHabitEntry.size();
    }

    class HabitsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView habitImage;
        TextView habitTitle;
        TextView habitTime;
        TextView habitCategory;

        public HabitsViewHolder(@NonNull View itemView) {
            super(itemView);
            habitImage = itemView.findViewById(R.id.single_habit_image);
            habitTitle = itemView.findViewById(R.id.single_habit_title);
            habitTime = itemView.findViewById(R.id.single_habit_time);
            habitCategory = itemView.findViewById(R.id.single_habit_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elemntId = mHabitEntry.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elemntId);
        }
    }

    public void setHabits(List<HabitEntry> habits)
    {
        mHabitEntry = habits;
        notifyDataSetChanged();
    }

    public List<HabitEntry> getHabits()
    {
        return mHabitEntry;
    }

    public interface ItemClickListener
    {
        void onItemClickListener(int itemId);
    }
}
