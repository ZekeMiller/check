package zekem.check.datas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import zekem.check.R;

public class DataRecyclerViewAdapter extends RecyclerView.Adapter< DataRecyclerViewAdapter.ViewHolder > {

    private final List< Data > mValues;

    public DataRecyclerViewAdapter( List< Data > items ) {

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
        public Data mItem;

        public ViewHolder( View view ) {

            super( view );
            mView = view;
            mIdView = view.findViewById( R.id.item_number );
            mContentView = view.findViewById( R.id.content );
        }

        @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
