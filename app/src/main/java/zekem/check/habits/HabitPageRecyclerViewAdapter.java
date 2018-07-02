package zekem.check.habits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import zekem.check.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitPageRecyclerViewAdapter extends RecyclerView.Adapter< HabitPageRecyclerViewAdapter.ViewHolder > {

    private final HabitViewModel mViewModel;
    private List< HabitDay > habits;

    public HabitPageRecyclerViewAdapter( HabitViewModel viewModel ) {

        mViewModel = viewModel;
    }

    public void setData( List< HabitDay > habits ) {

        this.habits = habits;
        notifyDataSetChanged();
        // TODO DiffUtil
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

        holder.mItem = habits.get( position );
        holder.mContentView.setText( String.format(Locale.getDefault(),
                "(%d) %s (%d)",
                holder.mItem.getPlusCount(),
                holder.mItem.getTitle(),
//                 holder.mItem.getHabit().getTitle(),
//                "placeholder",
                holder.mItem.getMinusCount() ) );

    }

    @Override
    public int getItemCount() {

        if ( habits == null ) {
            return 0;
        }
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private final View mView;
        private final TextView mContentView;
        private final ImageView mPlus;
        private final ImageView mMinus;
        private HabitDay mItem;

        private ViewHolder( final View view ) {

            super( view );
            mView = view;
            mContentView = view.findViewById( R.id.content );
            mPlus = view.findViewById( R.id.plusButton );
            mMinus = view.findViewById( R.id.minusButton );

            mContentView.setOnClickListener( v ->
                mViewModel.viewHabitDetail( habits.get( getAdapterPosition() ).getHabitID() )
            );

            mContentView.setOnLongClickListener( v -> {

                mViewModel.onContentLongPress( mItem );
                notifyItemRemoved( getAdapterPosition() );
                return true;

            } );

            mPlus.setOnClickListener( v -> {
                    mViewModel.onPlusPress( mItem );
                    notifyItemChanged( getAdapterPosition() );
            } );

            mMinus.setOnClickListener( v -> {
                    mViewModel.onMinusPress( mItem );
                    notifyItemChanged( getAdapterPosition() );
            } );
        }

        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
