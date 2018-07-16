package zekem.check.datas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;

/**
 * Fragment to display Datas
 */
public class DataPageFragment extends Fragment {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataPageFragment() {

    }

    @SuppressWarnings( "unused" )
    public static DataPageFragment newInstance() {

        DataPageFragment fragment;
        fragment = new DataPageFragment();
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
            recyclerView.setAdapter( new DataRecyclerViewAdapter( Data.DataDummy.DATAS ) );
        }
        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }
}
