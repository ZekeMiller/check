package zekem.check.habits;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zekem.check.R;
import zekem.check.habits.HabitPageFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class HabitRecyclerViewAdapter extends RecyclerView.Adapter< HabitRecyclerViewAdapter.ViewHolder > {

    public List< Habit > mValues;
    public final OnListFragmentInteractionListener mListener;

    public HabitRecyclerViewAdapter( List< Habit > items,
                                     OnListFragmentInteractionListener listener ) {

        mValues = items;
        mListener = listener;
    }

    public void setData( final List<Habit> newData ) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff( new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {

                return getItemCount();
            }

            @Override
            public int getNewListSize() {

                if ( newData == null ) {
                    Log.d( "Check", "setData DiffUtil on null newData" );
                    return 0;
                }
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame( int oldItemPosition, int newItemPosition ) {

                return mValues.get( oldItemPosition ).equals( newData.get(
                        newItemPosition ) );
            }

            @Override
            public boolean areContentsTheSame( int oldItemPosition, int newItemPosition ) {

                return mValues.get( oldItemPosition ).sameContents( newData.get( newItemPosition ) );
            }
        } );
        result.dispatchUpdatesTo( this );
        this.mValues = newData;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, final int position ) {

        holder.mItem = mValues.get( position );
        holder.mContentView.setText( String.valueOf( mValues.get( position ).getInfo() ) );

    }

    @Override
    public int getItemCount() {

        if ( mValues == null ) {
            Log.d( "Check", "getItemCount on null" );
            return 0;
        }
        return mValues.size();
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

                final Habit habit = mValues.get( getAdapterPosition() );
                mListener.onContentLongPress( habit );
                return true;

            } );

            mPlus.setOnClickListener( v -> {
                    mListener.onPlusPress( mValues.get( getAdapterPosition() ) );
                    notifyItemChanged( getAdapterPosition() );
            } );

            mMinus.setOnClickListener( v -> {
                    mListener.onMinusPress( mValues.get( getAdapterPosition() ) );
                    notifyItemChanged( getAdapterPosition() );
            } );
        }

        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
