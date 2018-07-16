package zekem.check.habits;

import android.arch.lifecycle.Observer;
import android.graphics.drawable.Drawable;
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

import zekem.check.R;
import zekem.check.habits.listener.HabitFragmentListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitPageRecyclerViewAdapter extends RecyclerView.Adapter< HabitPageRecyclerViewAdapter.ViewHolder > {

    private static final int MIN_MINUS_STREAK = 2;
    private static final int MIN_PLUS_STREAK = 2;

    private final HabitFragmentListener mListener;
    private final Observer< List< Habit > > mSetDataListener = this::setData;

    private List< Habit > mHabits;
    private LocalDate mDate = LocalDate.now();


    public HabitPageRecyclerViewAdapter(HabitFragmentListener listener ) {
        mListener = listener;
    }

    public synchronized void setData( final List< Habit > habits ) {

        if ( habits == null || habits.size() == 0 ) {
            return;
        }

        final List< Habit > oldList = this.mHabits;

        this.mHabits = habits;

        DiffUtil.DiffResult result = DiffUtil.calculateDiff( new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList == null ? 0 : oldList.size();
            }

            @Override
            public int getNewListSize() {
                return habits.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get( oldItemPosition ).equals( habits.get( newItemPosition ) );
            }

            @Override
            public boolean areContentsTheSame( int oldItemPosition, int newItemPosition ) {
                HabitDay oldDay = oldList.get( oldItemPosition ).getDay( mDate );
                HabitDay newDay = habits.get( newItemPosition ).getDay( mDate );
                return oldDay != null && oldDay.sameContents( newDay );
            }
        }, true );

        result.dispatchUpdatesTo( this );
    }


    @Override
    public int getItemCount() {

        if ( mHabits == null ) {
            return 0;
        }
        return mHabits.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }


    public Observer< List< Habit > > getSetDataListener() {
        return mSetDataListener;
    }


    @Override
    public void onBindViewHolder( @NonNull final ViewHolder holder, final int position ) {

        Habit habit = mHabits.get( position );
        holder.mHabit = habit;
        holder.mHabitDay = habit.getDay( mDate );

        if ( holder.mHabitDay == null ) {
            mListener.onMissingDay( holder.mHabit, mDate );
        }
        holder.mContentView.setText( holder.mHabit.getTitle() );


        showMinusButton( holder );
        showPlusButton( holder );

        showMinusStreak( holder );
        showPlusStreak( holder );

        setBorderColor( holder );

    }

    private void setBorderColor( final ViewHolder holder ) {
        int colorId;

        switch ( holder.mHabit.getGrade() ) {
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
    }


    private void showMinusStreak( ViewHolder holder ) {
        int minusStreak = holder.mHabit.calculateMinusStreak();

        if ( minusStreak >= MIN_MINUS_STREAK ) {
            holder.mMinusStreak.setText( String.valueOf( minusStreak ) );
        }
        else {
            holder.mMinusStreak.setVisibility( View.GONE );
        }
    }


    private void showPlusStreak( ViewHolder holder ) {
        int plusStreak = holder.mHabit.calculatePlusStreak();

        if ( plusStreak >= MIN_PLUS_STREAK ) {
            holder.mPlusStreak.setText( String.valueOf( plusStreak ) );
        }
        else {
            holder.mPlusStreak.setVisibility( View.GONE );
        }
    }


    private void showMinusButton( ViewHolder holder ) {

        int drawableId;

        if ( holder.mHabit.isMinusActive() ) {
            drawableId = R.drawable.ic_habit_minus_circle_24dp;
        }
        else {
            drawableId = R.drawable.ic_habit_minus_circle_inactive_24dp;
        }
        Drawable drawable = ContextCompat.getDrawable( holder.mView.getContext(), drawableId );
        holder.mMinus.setImageDrawable( drawable );

    }


    private void showPlusButton( ViewHolder holder ) {

        int drawableId;

        if ( holder.mHabit.isPlusActive() ) {
            drawableId = R.drawable.ic_habit_plus_circle_24dp;
        }
        else {
            drawableId = R.drawable.ic_habit_plus_circle_inactive_24dp;
        }
        Drawable drawable = ContextCompat.getDrawable( holder.mView.getContext(), drawableId );
        holder.mPlus.setImageDrawable( drawable );

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        private final View mView;
        private final TextView mContentView;
        private final ImageView mMinus;
        private final ImageView mPlus;
        private final TextView mMinusStreak;
        private final TextView mPlusStreak;

        private Habit mHabit;
        @Nullable private HabitDay mHabitDay;

        private ViewHolder( final View view ) {

            super( view );

            mView = view;
            mContentView = view.findViewById( R.id.content );

            mMinus = view.findViewById( R.id.minusButton );
            mPlus = view.findViewById( R.id.plusButton );
            mMinusStreak = view.findViewById( R.id.minusStreak );
            mPlusStreak = view.findViewById( R.id.plusStreak );


            mContentView.setOnClickListener( v -> onContentPress() );
            mContentView.setOnLongClickListener( v -> onContentLongPress() );
            mPlus.setOnClickListener( v -> onPlusPress() );
            mMinus.setOnClickListener( v -> onMinusPress() );

        }

        private void onMinusPress() {
            if ( mHabit != null && mHabit.isMinusActive() ) {
                mListener.onMinusPress( mHabitDay );
            }
        }

        private void onPlusPress() {
            if ( mHabit != null && mHabit.isPlusActive() ) {
                mListener.onPlusPress( mHabitDay );
            }
        }

        private void onContentPress() {
            mListener.onContentShortPress( mHabit.getId() );
                    // mHabits.get( getAdapterPosition() ).getId() );
        }

        private boolean onContentLongPress() {
            mListener.onContentLongPress( mHabitDay );
            return true;
        }


        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
