package zekem.check.habits;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;


public class HabitDetailFragment extends Fragment {

    private HabitViewModel habitViewModel;
    private int habitID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitDetailFragment() {
    }


    public static HabitDetailFragment newInstance(HabitViewModel habitViewModel, int habitID) {
        HabitDetailFragment habitDetailFragment = new HabitDetailFragment();
        habitDetailFragment.habitViewModel = habitViewModel;
        habitDetailFragment.habitID = habitID;
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater ) {
        inflater.inflate( R.menu.habit_detail_toolbar, menu );
        menu.findItem( R.id.habit_detail_toolbar_add_button )
                .setOnMenuItemClickListener( menuItem -> {
                    habitViewModel.addDay( habitID );
                    return true;
                } );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_detail_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            HabitDetailRecyclerViewAdapter adapter = new HabitDetailRecyclerViewAdapter( habitViewModel );
            recyclerView.setAdapter( adapter );
            habitViewModel.registerDetail( this, adapter::setData, habitID);
        }
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        habitViewModel = null;
    }
}
