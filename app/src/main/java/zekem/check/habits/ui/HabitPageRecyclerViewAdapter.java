package zekem.check.habits.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Locale;

import zekem.check.R;
import zekem.check.habits.Habit;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitWithDays;
import zekem.check.habits.listener.HabitFragmentListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitPageRecyclerViewAdapter extends RecyclerView.Adapter< HabitPageRecyclerViewAdapter.ViewHolder > {



    private final HabitFragmentListener mListener;
    private List<HabitWithDays> habitsWithDays;
    private LocalDate date = LocalDate.now();


    public HabitPageRecyclerViewAdapter(HabitFragmentListener listener ) {
        mListener = listener;
    }

    public synchronized void setData( final List< HabitWithDays > habitsWithDays ) {

        final List< HabitWithDays > oldList = this.habitsWithDays;

        this.habitsWithDays = habitsWithDays;

        DiffUtil.DiffResult result = DiffUtil.calculateDiff( new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList == null ? 0 : oldList.size();
            }

            @Override
            public int getNewListSize() {
                return habitsWithDays.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get( oldItemPosition )
                        .sameHabitId( habitsWithDays.get( newItemPosition ) );
            }

            @Override
            public boolean areContentsTheSame( int oldItemPosition, int newItemPosition ) {
                HabitDay oldDay = oldList.get(oldItemPosition).getForDate(date);
                return oldDay != null && oldDay.sameContents(habitsWithDays.get(newItemPosition).getForDate(date));
            }
        }, true );

        result.dispatchUpdatesTo( this );
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull final ViewHolder holder, final int position ) {

        HabitWithDays habitWithDays = habitsWithDays.get( position );
        holder.mHabitWithDays = habitWithDays;
        holder.mHabitDay = habitWithDays.getForDate( date );

        int plusCount = 0;
        int minusCount = 0;

        if ( holder.mHabitDay == null ) {
            mListener.onMissingDay( holder.mHabitWithDays.getHabit(), date );
        }
        else {
            plusCount = holder.mHabitDay.getPlusCount();
            minusCount = holder.mHabitDay.getMinusCount();
        }
        holder.mContentView.setText( String.format( Locale.getDefault(), "(%d) (%d) %s (%d) (%d)",
                holder.mHabitWithDays.getHabit().getTotalPlus(),
                plusCount,
                holder.mHabitWithDays.getHabit().getTitle(),
                minusCount,
                holder.mHabitWithDays.getHabit().getTotalMinus()
        ) );

        int colorId;

        switch ( holder.mHabitWithDays.getValue() ) {
            case -3:
                colorId = R.color.colorHabitMinusThree;
                break;
            case -2:
                colorId = R.color.colorHabitMinusTwo;
                break;
            case -1:
                colorId = R.color.colorHabitMinusOne;
                break;
            case 0:
                colorId = R.color.colorHabitZero;
                break;
            case 1:
                colorId = R.color.colorHabitPlusOne;
                break;
            case 2:
                colorId = R.color.colorHabitPlusTwo;
                break;
            case 3:
                colorId = R.color.colorHabitPlusThree;
                break;
            default:
                colorId = R.color.cardview_dark_background;
                break;
        }

        int color = ContextCompat.getColor( holder.mView.getContext(), colorId );
        View view = holder.mView.findViewById( R.id.cardView );
        view.setBackgroundColor( color );


        if ( ! holder.mHabitWithDays.getHabit().isPlusActive() ) {
            holder.mPlus.setImageDrawable(
                    ContextCompat.getDrawable( holder.mView.getContext(),
                            R.drawable.ic_habit_plus_circle_inactive_24dp ) );
        }

        if ( ! holder.mHabitWithDays.getHabit().isMinusActive() ) {
            holder.mMinus.setImageDrawable(
                    ContextCompat.getDrawable( holder.mView.getContext(),
                            R.drawable.ic_habit_minus_circle_inactive_24dp ) );
        }

    }

    @Override
    public int getItemCount() {

        if ( habitsWithDays == null ) {
            return 0;
        }
        return habitsWithDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private final View mView;
        private final TextView mContentView;
        private final ImageView mPlus;
        private final ImageView mMinus;

        private HabitWithDays mHabitWithDays;
        @Nullable private HabitDay mHabitDay;

        private ViewHolder( final View view ) {

            super( view );
            mView = view;
            mContentView = view.findViewById( R.id.content );
            mPlus = view.findViewById( R.id.plusButton );
            mMinus = view.findViewById( R.id.minusButton );

            mContentView.setOnClickListener( v ->
                mListener.onContentShortPress(
                        habitsWithDays.get( getAdapterPosition() ).getHabit().getId() )
            );

            mContentView.setOnLongClickListener( this::onContentLongPress );

            mPlus.setOnClickListener( this::onPlusPress);

            mMinus.setOnClickListener( this::onMinusPress );

        }

        private void onPlusPress( View v ) {
            if ( mHabitWithDays != null && mHabitWithDays.getHabit().isPlusActive() ) {
                mListener.onPlusPress(mHabitDay);
            }
        }

        private void onMinusPress( View v ) {
            if ( mHabitWithDays != null && mHabitWithDays.getHabit().isMinusActive() ) {
                mListener.onMinusPress(mHabitDay);
            }
        }

        private boolean onContentLongPress( View v ) {
            mListener.onContentLongPress( mHabitDay );
            return true;
        }


        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
