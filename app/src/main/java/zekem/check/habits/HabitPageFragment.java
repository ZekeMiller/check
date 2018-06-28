package zekem.check.habits;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import zekem.check.R;

/**
 * A fragment representing a list of Habits
 */
public class HabitPageFragment extends Fragment {

    private final MenuItem.OnMenuItemClickListener mButtonListener = this::onToolbarButtonPress;

    private HabitViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitPageFragment() {

    }

    /**
     * Factory method, takes no parameters currently but can be changed to do so in the future,
     * needed in order to keep constructor empty
     * @return  a new HabitPageFragment instance
     */
    public static HabitPageFragment newInstance() {

        return new HabitPageFragment();
    }

    /**
     * adds the menu items from habit_toolbar.xml to the action bar
     * @param menu  Menu passed from the Fragment (or wherever else in Android internal source)
     * @param inflater  MenuInflater passed from Fragment
     */
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
        inflater.inflate( R.menu.habit_toolbar, menu );
        menu.findItem( R.id.habit_toolbar_add_button )
                .setOnMenuItemClickListener( mButtonListener );
    }

    /**
     * First method called for a Fragment instance, attach ViewModel
     * @param context   the Context the Fragment is being created in
     */
    @Override
    public void onAttach( Context context ) {

        super.onAttach( context );
        mViewModel = ViewModelProviders.of( this ).get( HabitViewModel.class );
        
    }

    /**
     * called when Fragment created
     * @param savedInstanceState    the instance state from the creating activity
     */
    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );

    }

    /**
     * called when the system draws the UI, we want to create the RecyclerView now
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return  the created View
     */
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_habit_list, container, false );

        view.findViewById( R.id.habit_toolbar_add_button );

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

    /**
     * Last method called before fragment destruction, nulls ViewModel reference (unregistering
     * unnecessary since LiveData takes care of it)
     */
    @Override
    public void onDetach() {

        super.onDetach();
        mViewModel = null;
    }

    /**
     * click listener as a switch for the action bar
     *
     * @param menuItem  the clicked menuItem id
     * @return  true if handled, false if not (as per listener requirements)
     */
    private boolean onToolbarButtonPress( MenuItem menuItem ) {

        switch ( menuItem.getItemId() ) {
            case R.id.habit_toolbar_add_button:
                mViewModel.addHabit();
                return true;

            default:
                return false;
        }
    }
}
