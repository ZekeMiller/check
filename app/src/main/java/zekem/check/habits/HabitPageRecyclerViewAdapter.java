package zekem.check.habits;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitPageRecyclerViewAdapter extends RecyclerView.Adapter< HabitPageRecyclerViewAdapter.ViewHolder > {

    private final HabitViewModel mViewModel;
    private final Handler mainHandler = new Handler( Looper.getMainLooper() );
    private List< HabitWithDays > habitsWithDays;
    private LocalDate date = LocalDate.now();

    public HabitPageRecyclerViewAdapter( HabitViewModel viewModel ) {

        mViewModel = viewModel;
    }

    public void setData( final List< HabitWithDays > habitDays ) {

        final List< HabitWithDays > oldList = this.habitsWithDays;

        this.habitsWithDays = habitDays;

        DiffUtil.DiffResult result = DiffUtil.calculateDiff( new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList == null ? 0 : oldList.size();
            }

            @Override
            public int getNewListSize() {
                return habitDays == null ? 0 : habitDays.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get( oldItemPosition ).equals( habitDays.get( newItemPosition ) );
//                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                HabitDay oldDay = oldList.get( oldItemPosition ).getForDate( date );
                if ( oldDay == null ) {
                    return false;
                }
                return oldDay.sameContents( habitDays.get( newItemPosition ).getForDate( date ) );
//                return true;
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
        holder.mHabit = habitWithDays.getHabit();
        holder.mHabitDay = habitWithDays.getForDate( date );

        int plusCount = -2;
        int minusCount = -3;

        if ( holder.mHabitDay == null ) {
             mViewModel.addDay( holder.mHabit, () -> mainHandler.post( () -> this.notifyItemChanged( position ) ), date );
        }
        else {
            plusCount = holder.mHabitDay.getPlusCount();
            minusCount = holder.mHabitDay.getMinusCount();
        }
        holder.mContentView.setText( String.format( Locale.getDefault(), "(%d) %s (%d)",
                plusCount,
                // holder.mHabit.getTitle(),
                System.currentTimeMillis(),
                minusCount ) );

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
        private HabitDay mHabitDay;
        private Habit mHabit;

        private ViewHolder( final View view ) {

            super( view );
            mView = view;
            mContentView = view.findViewById( R.id.content );
            mPlus = view.findViewById( R.id.plusButton );
            mMinus = view.findViewById( R.id.minusButton );

            mContentView.setOnClickListener( v ->
                mViewModel.viewHabitDetail( habitsWithDays.get( getAdapterPosition() ).getHabit().getId() )
            );

            mContentView.setOnLongClickListener( v -> {
                mViewModel.onContentLongPress(mHabitDay);
                // notifyItemRemoved( getAdapterPosition() );
                return true;

            } );

            mPlus.setOnClickListener( v -> {
                mViewModel.onPlusPress(mHabitDay);
                // notifyItemChanged( getAdapterPosition() );
            } );

            mMinus.setOnClickListener( v -> {
                mViewModel.onMinusPress(mHabitDay);
                // notifyItemChanged( getAdapterPosition() );
            } );
        }

        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
