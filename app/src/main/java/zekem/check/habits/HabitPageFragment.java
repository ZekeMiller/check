package zekem.check.habits;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;

/**
 * A fragment representing a list of Habits
 */
public class HabitPageFragment extends Fragment {

    private HabitViewModel mViewModel;
    private final Toolbar.OnMenuItemClickListener mButtonListener = this::onToolbarButtonPress;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitPageFragment() {

    }

    public static HabitPageFragment newInstance() {

        return new HabitPageFragment();
    }

    @Override
    public void onAttach( Context context ) {

        super.onAttach( context );
        mViewModel = ViewModelProviders.of( this ).get( HabitViewModel.class );
        
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_habit_list, container, false );


        // Set the adapter
        if ( view instanceof RecyclerView ) {

            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager( new LinearLayoutManager( context ) );

            final HabitRecyclerViewAdapter adapter = new HabitRecyclerViewAdapter( mViewModel );

            recyclerView.setAdapter( adapter );
            mViewModel.register( this, adapter::setData );

        }
        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mViewModel = null;
    }

    private boolean onToolbarButtonPress( MenuItem menuItem ) {

        switch ( menuItem.getItemId() ) {
            case R.id.action_bar_button:
                mViewModel.addHabit();
                return true;

            default:
                return false;
        }
    }

    public Toolbar.OnMenuItemClickListener getButtonListener() {
        return mButtonListener;
    }


    public interface HabitFragmentListener {

        void onContentLongPress( Habit habit );

        void onPlusPress( Habit habit );

        void onMinusPress( Habit habit );
    }
}
