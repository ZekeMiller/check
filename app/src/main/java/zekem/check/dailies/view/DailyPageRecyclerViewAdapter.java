package zekem.check.dailies.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import zekem.check.R;
import zekem.check.dailies.model.Daily;
import zekem.check.dailies.viewmodel.listener.DailyPageListener;

/**
 *
 */
public class DailyPageRecyclerViewAdapter extends RecyclerView.Adapter< DailyPageRecyclerViewAdapter.ViewHolder > {

    private final Observer< List< Daily > > mSetDataListener;
    private final DailyPageListener mListener;

    private List< Daily > mDailies;

    public DailyPageRecyclerViewAdapter( DailyPageListener listener ) {
        mListener = listener;
        mSetDataListener = this::setData;
    }

    private void setData( List< Daily > dailies ) {
        mDailies = dailies;
        notifyDataSetChanged();
        // TODO DiffUtil
    }


    @Override
    public int getItemCount() {

        if ( mDailies == null ) {
            return 0;
        }
        return mDailies.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_daily, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder( @NonNull final ViewHolder holder, int position ) {

        Daily daily = mDailies.get( position );
        holder.mItem = daily;
        holder.mIdView.setText( String.format( Locale.getDefault(), "%d", daily.getId() ) );
        holder.mContentView.setText( daily.getScheduler().toString() );
    }


    @NonNull
    public Observer< List< Daily > > getSetDataListener() {
        return mSetDataListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final TextView mIdView;
        private final TextView mContentView;
        private Daily mItem;

        private ViewHolder( View view ) {

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
