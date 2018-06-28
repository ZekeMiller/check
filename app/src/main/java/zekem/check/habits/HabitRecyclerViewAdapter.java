package zekem.check.habits;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zekem.check.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link HabitFragmentListener}.
 */
public class HabitRecyclerViewAdapter extends RecyclerView.Adapter< HabitRecyclerViewAdapter.ViewHolder > {

    public final HabitViewModel mViewModel;
    private List< Habit > habits;

    public HabitRecyclerViewAdapter( HabitViewModel viewModel ) {

        mViewModel = viewModel;
    }

    public void setData( List< Habit > habits ) {

        this.habits = habits;
        notifyDataSetChanged();
        // TODO DiffUtil
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, final int position ) {

        holder.mItem = habits.get( position );
        holder.mContentView.setText( String.valueOf( holder.mItem.getInfo() ) );

    }

    @Override
    public int getItemCount() {

        if ( habits == null ) {
            return 0;
        }
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public final View mView;
        public final TextView mContentView;
        public final ImageView mPlus;
        public final ImageView mMinus;
        public Habit mItem;

        public ViewHolder( final View view ) {

            super( view );
            mView = view;
            mContentView = view.findViewById( R.id.content );
            mPlus = view.findViewById( R.id.plusButton );
            mMinus = view.findViewById( R.id.minusButton );

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
