package zekem.check.habits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zekem.check.R;

public class HabitDetailRecyclerViewAdapter extends RecyclerView.Adapter<HabitDetailRecyclerViewAdapter.ViewHolder> {

    private List< HabitDay > mValues;

    public HabitDetailRecyclerViewAdapter() {
    }

    public void setData( List< HabitDay > habitDays ) {

        mValues = habitDays;

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
        holder.mHabitDay = mValues.get(position);
        holder.mPlusCount.setText( String.valueOf( mValues.get(position).getPlusCount()) );
        holder.mMinusCount.setText( String.valueOf( mValues.get(position).getMinusCount()) );
        holder.mDate.setText( mValues.get( position ).getDate().toString() );

        holder.mView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {

        if ( mValues == null ) {
            return 0;
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final TextView mPlusCount;
        private final TextView mMinusCount;
        private final TextView mDate;
        private HabitDay mHabitDay;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            mPlusCount = view.findViewById(R.id.plusCount);
            mMinusCount = view.findViewById(R.id.minusCount);
            mDate = view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlusCount.getText() + "'";
        }
    }
}
