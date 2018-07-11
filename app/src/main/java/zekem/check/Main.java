package zekem.check;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
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

import zekem.check.dailies.DailyPageFragment;
import zekem.check.dailies.Daily;
import zekem.check.datas.Data;
import zekem.check.datas.DataPageFragment;
import zekem.check.habits.ui.DeleteHabitDialogFragment;
import zekem.check.habits.ui.HabitDetailFragment;
import zekem.check.habits.HabitObservables;
import zekem.check.habits.ui.HabitPageFragment;
import zekem.check.habits.ui.NewHabitFragment;

/**
 * The main Activity, has a bottom navigation and holds other Fragments
 */
public class Main extends AppCompatActivity implements
                DailyPageFragment.OnListFragmentInteractionListener,
                DataPageFragment.OnListFragmentInteractionListener,
                AnalyticsPageFragment.OnFragmentInteractionListener {


    private MainViewModel mMainViewModel;

    private HabitObservables mHabitObservables;
//    private HabitPageFragment mHabitFragment;
//    private DailyPageFragment mDailiesFragment;
//    private DataPageFragment mDataFragment;
//    private AnalyticsPageFragment mAnalyticPageFragment;

    private final HabitObservables.Observer< Integer > mDeleteObserver = this::showDeleteDialog;
    private final HabitObservables.Observer< Integer > mDetailObserver = this::showDetailFragment;
    private final HabitObservables.Observer< Void > mNewHabitObserver = v -> this.showNewHabitFragment();
    private final HabitObservables.Observer< Void > mShowHabitPageObserver = v -> this.showHabitPage();


    private BottomNavigationView mBottomNavigationView;

    private boolean firstRun;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /**
         * Switch listener for bottom nav
         * @param item  the bottom nav button pressed
         * @return  true if handled (should always be unless something goes aggressively wrong)
         */
        @Override
        public boolean onNavigationItemSelected( @NonNull MenuItem item ) {

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
//                    fragment = mHabitFragment;
                    fragment = HabitPageFragment.newInstance();
                    title = getString( R.string.title_habits );
                    break;
                case R.id.navigation_dailies:
//                    fragment = mDailiesFragment;
                    fragment = DailyPageFragment.newInstance();
                    title = getString( R.string.title_dailies );
                    break;
                case R.id.navigation_datas:
//                    fragment = mDataFragment;
                    fragment = DataPageFragment.newInstance();
                    title = getString( R.string.title_datas );
                    break;
                case R.id.navigation_analytics:
//                    fragment = mAnalyticPageFragment;
                    fragment = AnalyticsPageFragment.newInstance();
                    title = getString( R.string.title_analytics );
                    break;
                default:
                    return false;
            }
            transaction.replace( R.id.main_fragment_container, fragment );
            transaction.commit();
            setTitle( title );
            return true;
        }
    };

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        mBottomNavigationView.setSelectedItemId( mBottomNavigationView.getSelectedItemId() );
    }


    /**
     * called when created
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

//        mHabitViewModel = ViewModelProviders.of( this ).get( HabitViewModel.class );
        mMainViewModel = ViewModelProviders.of( this ).get( MainViewModel.class );

//        mHabitViewModel.getShowHabitDetailListener().observe( this, this::showDetailFragment );
//        mHabitViewModel.getNewHabitPageListener().observe( this, v -> this.showNewHabitFragment() );
//        mHabitViewModel.getDetailDeleteListener().observe( this, this::showDeleteDialog );
//        mHabitViewModel.getShowHabitPageListener().observe( this, v -> this.showHabitPage() );

//        mHabitObservables.getShowHabitDetailListener().observe( this, this::showDetailFragment );
//        mHabitObservables.getNewHabitPageListener().observe( this, v -> this.showNewHabitFragment() );
//        mHabitObservables.getDetailDeleteListener().observe( this, this::showDeleteDialog );
//        mHabitObservables.getShowHabitPageListener().observe( this, v -> this.showHabitPage() );

        mHabitObservables.registerDelete( mDeleteObserver );
        mHabitObservables.registerDetail( mDetailObserver );
        mHabitObservables.registerNewHabit( mNewHabitObserver );
        mHabitObservables.registerShowHabitPage( mShowHabitPageObserver );



//        mHabitFragment = HabitPageFragment.newInstance();
//        mDailiesFragment = DailyPageFragment.newInstance();
//        mDataFragment = DataPageFragment.newInstance();
//        mAnalyticPageFragment = AnalyticsPageFragment.newInstance();

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        firstRun = true;
    }


    @Override
    protected void onResume() {

        super.onResume();

        if ( firstRun ) {
            mBottomNavigationView.setSelectedItemId( R.id.navigation_dailies );
        }
        firstRun = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        mHabitObservables.unregisterDelete( mDeleteObserver );
        mHabitObservables.unRegisterDetail( mDetailObserver );
        mHabitObservables.unregisterNewHabit( mNewHabitObserver );
        mHabitObservables.unregisterShowHabitPage( mShowHabitPageObserver );

    }

    private void showDetailFragment( int habitID ) {

        HabitDetailFragment habitDetailFragment = HabitDetailFragment.newInstance( habitID );

        getSupportFragmentManager().beginTransaction()
            .replace( R.id.main_fragment_container, habitDetailFragment )
            .addToBackStack( null )
            .commit();


        mMainViewModel.getHabit( habitID ).observe( habitDetailFragment, habit -> {
            if ( habit == null ) {
                setTitle( getString( R.string.title_habits ) );
            }
            else {
                setTitle( habit.getTitle() );
            }
        });


    }

    private void showNewHabitFragment() {

        NewHabitFragment newHabitFragment = NewHabitFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace( R.id.main_fragment_container, newHabitFragment )
                .addToBackStack( null )
                .commit();

        setTitle( getString( R.string.title_new_habit_page ) );

    }

    private void showDeleteDialog( int habitId ) {

        if ( habitId != 0 ) {
            DeleteHabitDialogFragment deleteDialog =
                    DeleteHabitDialogFragment.newInstance( habitId );

            deleteDialog.show( getSupportFragmentManager(), null );
        }

    }

    private void resetBottomNavigation() {
        mBottomNavigationView.setSelectedItemId( mBottomNavigationView.getSelectedItemId() );
    }

    private void showHabitPage() {
        mBottomNavigationView.setSelectedItemId( R.id.navigation_habits );
    }

    private void setTitle( String title ) {
        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( title );
    }


    @Override
    public void onFragmentInteraction( Uri uri ) {

    }

    @Override
    public void onListFragmentInteraction( Daily daily ) {

    }

    @Override
    public void onListFragmentInteraction( Data data ) {

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
