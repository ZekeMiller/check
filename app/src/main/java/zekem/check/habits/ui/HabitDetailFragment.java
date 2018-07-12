package zekem.check.habits.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;
import zekem.check.habits.HabitDetailRecyclerViewAdapter;
import zekem.check.habits.listener.HabitDetailListener;
import zekem.check.habits.viewmodel.HabitDetailViewModel;


public class HabitDetailFragment extends Fragment {

    private HabitDetailListener mListener;
    private int mHabitId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitDetailFragment() {
    }


    public static HabitDetailFragment newInstance( int habitId ) {
        HabitDetailFragment habitDetailFragment = new HabitDetailFragment();
        habitDetailFragment.mHabitId = habitId;
        return habitDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
        mListener = ViewModelProviders.of( this ).get( HabitDetailViewModel.class );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
        inflater.inflate( R.menu.habit_detail_toolbar, menu );
        menu.findItem( R.id.habit_detail_toolbar_add_button )
            .setOnMenuItemClickListener( menuItem -> {
                mListener.onDetailToolbarButton( mHabitId );
                return true;
            } );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_detail_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HabitDetailRecyclerViewAdapter adapter = new HabitDetailRecyclerViewAdapter( mListener );
            recyclerView.setAdapter( adapter );
            mListener.getDaysForDetail( mHabitId ).observe( this, adapter::setData );
        }
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
