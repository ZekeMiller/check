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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DataPageFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataPageFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings( "unused" )
    public static DataPageFragment newInstance() {

        DataPageFragment fragment;
        fragment = new DataPageFragment();
        return fragment;
    }

    @Override
    public void onAttach( Context context ) {

        super.onAttach( context );
        if ( context instanceof OnListFragmentInteractionListener ) {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException( context.toString()
                    + " must implement HabitFragmentListener" );
        }
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
            recyclerView.setAdapter( new DataRecyclerViewAdapter( Data.DataDummy.DATAS, mListener ) );
        }
        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        // TODO: Update argument type and name
        void onListFragmentInteraction( Data item );
    }
}
