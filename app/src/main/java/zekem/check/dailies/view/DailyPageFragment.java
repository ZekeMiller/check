package zekem.check.dailies.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;
import zekem.check.dailies.viewmodel.DailyPageViewModel;
import zekem.check.dailies.viewmodel.listener.DailyPageListener;

/**
 * Fragment to display the the list of dailies
 */
public class DailyPageFragment extends Fragment {

    private final MenuItem.OnMenuItemClickListener mButtonListener = this::onToolbarButtonPress;

    private DailyPageListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DailyPageFragment() {
        // intentionally empty
    }

    @SuppressWarnings( "unused" )
    public static DailyPageFragment newInstance() {

        DailyPageFragment fragment;
        fragment = new DailyPageFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
        inflater.inflate( R.menu.daily_toolbar, menu );
        menu.findItem( R.id.daily_toolbar_add_button )
                .setOnMenuItemClickListener( mButtonListener );
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );

        mListener = ViewModelProviders.of( this ).get( DailyPageViewModel.class );
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_daily_list, container, false );

        // TODO make static based on ID rather than context
        // Set the adapter
        if ( view instanceof RecyclerView ) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager( new LinearLayoutManager( context ) );

            final DailyPageRecyclerViewAdapter adapter = new DailyPageRecyclerViewAdapter( mListener );

            recyclerView.setAdapter( adapter );
            mListener.getDailies().observe( this, adapter.getSetDataListener() );
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean onToolbarButtonPress( MenuItem menuItem ) {

        switch ( menuItem.getItemId() ) {
            case R.id.daily_toolbar_add_button:
                mListener.onToolbarAddButtonPress();
                return true;

            default:
                return false;
        }
    }
}
