package zekem.check.habits;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zekem.check.R;
import zekem.check.habits.listener.HabitDetailListener;

public class HabitDetailRecyclerViewAdapter extends RecyclerView.Adapter<HabitDetailRecyclerViewAdapter.ViewHolder> {

    private final HabitDetailListener mListener;
    private final Observer< Habit > mSetDataListener = this::setData;

    private List< HabitDay > mHabitDays;

    public HabitDetailRecyclerViewAdapter( HabitDetailListener listener ) {
        mListener = listener;
    }

    public void setData( Habit habit ) {

        mHabitDays = habit.getHabitDays();

        notifyDataSetChanged();

        // TODO DiffUtil

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.fragment_habit_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull final ViewHolder holder, int position) {
        holder.mHabitDay = mHabitDays.get( position );

        holder.mPlusCount.setText( String.valueOf( holder.mHabitDay.getPlusCount() ) );
        holder.mMinusCount.setText( String.valueOf( holder.mHabitDay.getMinusCount() ) );
        holder.mDateView.setText( holder.mHabitDay.getDate().toString() );

        holder.mView.setOnClickListener(v -> mListener.press( holder.mHabitDay ) );

        holder.mView.setOnLongClickListener( v -> {
            mListener.longPress( holder.mHabitDay );
            return true;
        } );
    }

    @Override
    public int getItemCount() {

        if ( mHabitDays == null ) {
            return 0;
        }
        return mHabitDays.size();
    }

    public Observer< Habit > getSetDataListener() {
//    public Observer< List< HabitDay > > getSetDataListener() {
        return mSetDataListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final TextView mPlusCount;
        private final TextView mMinusCount;
        private final TextView mDateView;

//        private LocalDate mDate;
        private HabitDay mHabitDay;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mPlusCount = view.findViewById(R.id.plusCount);
            mMinusCount = view.findViewById(R.id.minusCount);
            mDateView = view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlusCount.getText() + "'";
        }
    }
}
