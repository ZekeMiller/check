package zekem.check;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.lang.reflect.Field;

import zekem.check.dailies.view.DailyPageFragment;
import zekem.check.datas.DataPageFragment;
import zekem.check.habits.model.Habit;
import zekem.check.habits.view.DeleteHabitDialogFragment;
import zekem.check.habits.view.HabitDetailFragment;
import zekem.check.habits.viewmodel.HabitObservables;
import zekem.check.habits.view.HabitPageFragment;
import zekem.check.habits.view.NewHabitFragment;

/**
 * The main Activity, has a bottom navigation and holds other Fragments
 */
public class Main extends AppCompatActivity {


    // fields

    // observers for convenience
    private final Observer< Integer > mDeleteObserver = this::showDeleteDialog;
    private final Observer< Integer > mDetailObserver = this::showDetailFragment;
    private final Observer< Void > mNewHabitObserver = v -> this.showNewHabitFragment();
    private final Observer< Void > mShowHabitPageObserver = v -> this.showHabitPage();
    private final Observer< Habit > mSetTitleObserver = this::setTitle;
    private final BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = this::onNavigationItemSelected;


    private MainViewModel mMainViewModel;
    private HabitObservables mHabitObservables;
    private BottomNavigationView mBottomNavigationView;
    private boolean firstRun;


    // lifecycle methods

    /**
     * called when created, initializes fields and sets up view
     * @param savedInstanceState can be used to restore instance state
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        mBottomNavigationView = findViewById( R.id.navigation );
        removeShiftMode( mBottomNavigationView );

        mBottomNavigationView.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        mHabitObservables = HabitObservables.getInstance();

        mMainViewModel = ViewModelProviders.of( this ).get( MainViewModel.class );

        mHabitObservables.registerDelete( mDeleteObserver );
        mHabitObservables.registerDetail( mDetailObserver );
        mHabitObservables.registerNewHabit( mNewHabitObserver );
        mHabitObservables.registerShowHabitPage( mShowHabitPageObserver );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        firstRun = true;
    }


    /**
     * checks if the activity has just been opened, shows the habit page if so
     */
    @Override
    protected void onResume() {

        super.onResume();

        if ( firstRun ) {
            showHabitPage();
        }
        firstRun = false;
    }

    /**
     * cleans up references for garbage collection and unregisters observation
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();

        mHabitObservables.unregisterDelete( mDeleteObserver );
        mHabitObservables.unRegisterDetail( mDetailObserver );
        mHabitObservables.unregisterNewHabit( mNewHabitObserver );
        mHabitObservables.unregisterShowHabitPage( mShowHabitPageObserver );

        mHabitObservables = null;
        mMainViewModel = null;
        mBottomNavigationView = null;
    }


    /**
     * normal on back press action, but if after the back press the base fragment is the only thing
     * left, resets bottom navigation
     */
    @Override
    public void onBackPressed() {

        int prePopCount = getSupportFragmentManager().getBackStackEntryCount();

        super.onBackPressed();

        if ( prePopCount == 1 ) {
            resetBottomNavigation();
        }
    }



    /**
     * shows the detail fragment for a given habitId
     * @param habitID the id to view details of
     */
    private void showDetailFragment( int habitID ) {

        HabitDetailFragment habitDetailFragment = HabitDetailFragment.newInstance( habitID );

        // replace fragment and add to back stack
        getSupportFragmentManager().beginTransaction()
            .replace( R.id.main_fragment_container, habitDetailFragment )
            .addToBackStack( null )
            .commit();

        // set title when ready
        mMainViewModel.getHabit( habitID ).observe( habitDetailFragment, mSetTitleObserver );
    }

    /**
     * shows the fragment for adding a new habit
     */
    private void showNewHabitFragment() {

        NewHabitFragment newHabitFragment = NewHabitFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace( R.id.main_fragment_container, newHabitFragment )
                .addToBackStack( null )
                .commit();

        setTitle( getString( R.string.title_new_habit_page ) );

    }

    /**
     * shows a delete dialog for a given habitId
     * @param habitId the id to potentially delete
     */
    private void showDeleteDialog( int habitId ) {

        if ( habitId != 0 ) {
            DeleteHabitDialogFragment deleteDialog =
                    DeleteHabitDialogFragment.newInstance( habitId );

            deleteDialog.show( getSupportFragmentManager(), null );
        }

    }


    /**
     * Switch listener for bottom nav
     * @param item  the bottom nav button pressed
     * @return  true if handled (should always be unless something goes aggressively wrong)
     */
    private boolean onNavigationItemSelected( @NonNull MenuItem item ) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        for ( int i = 0 ; i < count ; i++ ) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment;
        String title;

        switch ( item.getItemId() ) {
            case R.id.navigation_habits:
                fragment = HabitPageFragment.newInstance();
                title = getString( R.string.title_habits );
                break;
            case R.id.navigation_dailies:
                fragment = DailyPageFragment.newInstance();
                title = getString( R.string.title_dailies );
                break;
            case R.id.navigation_datas:
                fragment = DataPageFragment.newInstance();
                title = getString( R.string.title_datas );
                break;
            default:
                return false;
        }
        transaction.replace( R.id.main_fragment_container, fragment );
        transaction.commit();
        setTitle( title );
        return true;
    }

    /**
     * resets the bottom navigation (basically just triggers the bottom navigation listener to hard
     * refresh)
     */
    private void resetBottomNavigation() {
        mBottomNavigationView.setSelectedItemId( mBottomNavigationView.getSelectedItemId() );
    }

    /**
     * shows the habit page fragment
     */
    private void showHabitPage() {
        mBottomNavigationView.setSelectedItemId( R.id.navigation_habits );
    }

    /**
     * set the title from a Habit (used for convenience in LiveData observer
     * @param habit the habit to get title from
     */
    private void setTitle( Habit habit ) {
        if ( habit != null ) {
            setTitle( habit.getTitle() );
        }
    }

    /**
     * sets the title of the toolbar to the given string
     * @param title the string to set the title to
     */
    private void setTitle( String title ) {
        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( title );
    }


    /**
     * I found this code online and it gets rid of the weird shifting bottom nav mode
     * @param view  the BottomNavigation to remove shift mode from
     */
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("check", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("check", "Unable to change value of shift mode", e);
        }
    }
}
