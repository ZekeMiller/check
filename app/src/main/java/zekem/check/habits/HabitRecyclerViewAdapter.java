package zekem.check.habits;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zekem.check.R;
import zekem.check.habits.HabitPageFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Habit} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HabitRecyclerViewAdapter extends RecyclerView.Adapter< HabitRecyclerViewAdapter.ViewHolder > {

    public final List< Habit > mValues;
    public final OnListFragmentInteractionListener mListener;

    public HabitRecyclerViewAdapter( List< Habit > items, OnListFragmentInteractionListener listener ) {

        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_habit, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position ) {

        holder.mItem = mValues.get( position );
        holder.mIdView.setText( String.format( Locale.getDefault(), "%d", position ) );
        holder.mContentView.setText( mValues.get( position ).getTitle() );

        holder.mView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                if ( null != mListener ) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction( holder.mItem );
                }
            }
        } );
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Habit mItem;

        public ViewHolder( View view ) {

            super( view );
            mView = view;
            mIdView = (TextView) view.findViewById( R.id.item_number );
            mContentView = (TextView) view.findViewById( R.id.content );
        }

        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
