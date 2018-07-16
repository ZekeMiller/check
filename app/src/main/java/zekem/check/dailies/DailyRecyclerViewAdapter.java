package zekem.check.dailies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import zekem.check.R;

/**
 *
 */
public class DailyRecyclerViewAdapter extends RecyclerView.Adapter< DailyRecyclerViewAdapter.ViewHolder > {

    private final List< Daily > mValues;

    public DailyRecyclerViewAdapter( List< Daily > items ) {

        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_daily, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position ) {

        holder.mItem = mValues.get( position );
        holder.mIdView.setText( String.format( Locale.getDefault(),
                "%d", mValues.get( position ).getId() ) );
        holder.mContentView.setText( mValues.get( position ).getTitle() );
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Daily mItem;

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
