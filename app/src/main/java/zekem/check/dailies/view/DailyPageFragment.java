package zekem.check.dailies.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;
import zekem.check.dailies.model.Daily;

/**
 * Fragment to display the the list of dailies
 */
public class DailyPageFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DailyPageFragment() {

    }

    @SuppressWarnings( "unused" )
    public static DailyPageFragment newInstance() {

        DailyPageFragment fragment;
        fragment = new DailyPageFragment();
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_daily_list, container, false );

        // Set the adapter
        if ( view instanceof RecyclerView ) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager( new LinearLayoutManager( context ) );
            recyclerView.setAdapter( new DailyRecyclerViewAdapter( Daily.DailyDummy.DAILIES ) );
        }
        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }
}
