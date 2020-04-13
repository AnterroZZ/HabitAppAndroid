package com.anterroz.trainingproject;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anterroz.trainingproject.database.HabitEntry;
import com.anterroz.trainingproject.database.HabitsDatabase;
import com.anterroz.trainingproject.utilities.NotificationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitsViewHolder> {

    private Context mContext;
    private List<HabitEntry> mHabitEntry;
    final private ItemClickListener mItemClickListener;
    private HabitsDatabase mDatabase;
    private List<Boolean> isFirstTimeHabitRunning;



    public HabitsAdapter(Context context,ItemClickListener listener)
    {
        mContext = context;
        mItemClickListener = listener;
        isFirstTimeHabitRunning = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final HabitsViewHolder holder, final int position) {

        /* Taking data from a specified entry of mHabitEntry and binding it
        to a RecyclerView ViewHolder
         */
        final HabitEntry habitEntry = mHabitEntry.get(position);
        String title = habitEntry.getTitle();
        String category = habitEntry.getCategory();
        int imageViewId = habitEntry.getImageViewId();
        int time = habitEntry.getTimeInSeconds();
        int currentTime = (int) new Date().getTime();
        int timeRemainingInMillis=time - currentTime;
//        Toast.makeText(mContext, "" + habitEntry.isFirstTimerrunning(), Toast.LENGTH_SHORT).show();

            if (holder.timer != null) {
                holder.timer.cancel();
            }
                holder.timer = new CountDownTimer(timeRemainingInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String output = "Will go down in " + millisUntilFinished / 1000;
                        holder.habitTime.setText(output);
                    }

                    @Override
                    public void onFinish() {
                        holder.habitTime.setText("Done!");
                    }
                }.start();

        holder.habitTitle.setText(title);
        holder.habitImage.setImageResource(imageViewId);
        holder.habitCategory.setText(category);


    }

    @Override
    public int getItemCount() {
        if (mHabitEntry == null) {
            return 0;
        }
        return mHabitEntry.size();
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

    class HabitsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView habitImage;
        TextView habitTitle;
        TextView habitTime;
        TextView habitCategory;
        CountDownTimer timer;
        CountDownTimer timer2;

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
}
