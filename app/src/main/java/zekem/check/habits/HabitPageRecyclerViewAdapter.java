package zekem.check.habits;

import android.arch.lifecycle.Observer;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Locale;

import zekem.check.R;
import zekem.check.habits.listener.HabitFragmentListener;

import static zekem.check.habits.HabitObservables.TAG;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitPageRecyclerViewAdapter extends RecyclerView.Adapter< HabitPageRecyclerViewAdapter.ViewHolder > {

    private static final int MIN_MINUS_STREAK = 2;
    private static final int MIN_PLUS_STREAK = 2;

    private final HabitFragmentListener mListener;
    private final Observer< List< HabitWithDays > > mSetDataListener = this::setData;

    private List<HabitWithDays> habitsWithDays;
    private LocalDate date = LocalDate.now();


    public HabitPageRecyclerViewAdapter(HabitFragmentListener listener ) {
        mListener = listener;
    }

    public synchronized void setData( final List< HabitWithDays > habitsWithDays ) {

        Log.d( TAG, "habit page setData" );

        if ( habitsWithDays == null || habitsWithDays.size() == 0 ) {
            return;
        }

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


    @Override
    public int getItemCount() {

        if ( habitsWithDays == null ) {
            return 0;
        }
        return habitsWithDays.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }


    public Observer< List< HabitWithDays > > getSetDataListener() {
        return mSetDataListener;
    }


    @Override
    public void onBindViewHolder( @NonNull final ViewHolder holder, final int position ) {

        HabitWithDays habitWithDays = habitsWithDays.get( position );
        holder.mHabitWithDays = habitWithDays;
        holder.mHabitDay = habitWithDays.getForDate( date );

        if ( holder.mHabitDay == null ) {
            mListener.onMissingDay( holder.mHabitWithDays.getHabit(), date );
        }
        holder.mContentView.setText( holder.mHabitWithDays.getHabit().getTitle() );


        showMinusButton( holder );
        showPlusButton( holder );

        showMinusStreak( holder );
        showPlusStreak( holder );

        setBorderColor( holder );

    }

    private void setBorderColor( final ViewHolder holder ) {
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
    }


    private void showMinusStreak( ViewHolder holder ) {
        int minusStreak = holder.mHabitWithDays.calculateMinusStreak();

        if ( minusStreak >= MIN_MINUS_STREAK ) {
            holder.mMinusStreak.setText( String.valueOf( minusStreak ) );
        }
        else {
            holder.mMinusStreak.setVisibility( View.GONE );
        }
    }


    private void showPlusStreak( ViewHolder holder ) {
        int plusStreak = holder.mHabitWithDays.calculatePlusStreak();

        if ( plusStreak >= MIN_PLUS_STREAK ) {
            holder.mPlusStreak.setText( String.valueOf( plusStreak ) );
        }
        else {
            holder.mPlusStreak.setVisibility( View.GONE );
        }
    }


    private void showMinusButton( ViewHolder holder ) {

        int drawableId;

        if ( holder.mHabitWithDays.getHabit().isMinusActive() ) {
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

        if ( holder.mHabitWithDays.getHabit().isPlusActive() ) {
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

        private HabitWithDays mHabitWithDays;
        @Nullable private HabitDay mHabitDay;

        private ViewHolder( final View view ) {

            super( view );
            mView = view;
            mContentView = view.findViewById( R.id.content );
            mMinus = view.findViewById( R.id.minusButton );
            mPlus = view.findViewById( R.id.plusButton );
            mMinusStreak = view.findViewById( R.id.minusStreak );
            mPlusStreak = view.findViewById( R.id.plusStreak );


            mContentView.setOnClickListener( this::onContentPress );

            mContentView.setOnLongClickListener( this::onContentLongPress );

            mPlus.setOnClickListener( this::onPlusPress);

            mMinus.setOnClickListener( this::onMinusPress );

        }

        private void onMinusPress( View v ) {
            Log.d( TAG, "HabitRecyclerViewAdapter::onMinusPress" );
            if ( mHabitWithDays != null && mHabitWithDays.getHabit().isMinusActive() ) {
                mListener.onMinusPress(mHabitDay);
            }
        }

        private void onPlusPress( View v ) {
            Log.d( TAG, "HabitRecyclerViewAdapter::onPlusPress" );
            if ( mHabitWithDays != null && mHabitWithDays.getHabit().isPlusActive() ) {
                mListener.onPlusPress(mHabitDay);
            }
        }

        private void onContentPress( View v ) {
            Log.d( TAG, "HabitRecyclerViewAdapter::onContentPress" );
            mListener.onContentShortPress(
                    habitsWithDays.get( getAdapterPosition() ).getHabit().getId() );
        }

        private boolean onContentLongPress( View v ) {
            Log.d( TAG, "HabitRecyclerViewAdapter::onContentLongPress" );
            mListener.onContentLongPress( mHabitDay );
            return true;
        }


        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
