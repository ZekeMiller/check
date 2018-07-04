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
import zekem.check.habits.HabitDetailFragment;
import zekem.check.habits.HabitPageFragment;
import zekem.check.habits.HabitViewModel;
import zekem.check.habits.NewHabitFragment;

/**
 * The main Activity, has a bottom navigation and holds other Fragments
 */
public class Main extends AppCompatActivity implements
                DailyPageFragment.OnListFragmentInteractionListener,
                DataPageFragment.OnListFragmentInteractionListener,
                AnalyticsPageFragment.OnFragmentInteractionListener {

    private HabitViewModel mHabitViewModel;
    private HabitPageFragment habitsFragment;
    private DailyPageFragment dailiesFragment;
    private DataPageFragment dataFragment;
    private AnalyticsPageFragment analyticsPageFragment;

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
                    fragment = habitsFragment;
                    habitsFragment.setViewModel( mHabitViewModel );
                    title = getString( R.string.title_habits );
                    break;
                case R.id.navigation_dailies:
                    fragment = dailiesFragment;
                    title = getString( R.string.title_dailies );
                    break;
                case R.id.navigation_datas:
                    fragment = dataFragment;
                    title = getString( R.string.title_datas );
                    break;
                case R.id.navigation_analytics:
                    fragment = analyticsPageFragment;
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
        BottomNavigationView bottomNavigationView = findViewById( R.id.navigation );
        bottomNavigationView.setSelectedItemId( bottomNavigationView.getSelectedItemId() );
    }


    /**
     * called when created
     * @param savedInstanceState can be used to restore instance state
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        BottomNavigationView navigation = findViewById( R.id.navigation );
        removeShiftMode( navigation );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        mHabitViewModel = ViewModelProviders.of( this ).get( HabitViewModel.class );

        mHabitViewModel.getHabitDetailListener().observe( this, this::showDetailFragment );
        mHabitViewModel.getNewHabitPageListener().observe( this, bool -> {
            if ( bool ) {
                this.showNewHabitFragment();
            }
            else {
                navigation.setSelectedItemId( R.id.navigation_habits );
            }
        } );

        habitsFragment = HabitPageFragment.newInstance();
        dailiesFragment = DailyPageFragment.newInstance( 1 );
        dataFragment = DataPageFragment.newInstance( 1 );
        analyticsPageFragment = AnalyticsPageFragment.newInstance();

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
    }

    @Override
    protected void onResume() {

        super.onResume();

        BottomNavigationView navigation = findViewById( R.id.navigation );
        navigation.setSelectedItemId( R.id.navigation_habits );
    }

    private void showDetailFragment( int habitID ) {

        HabitDetailFragment habitDetailFragment =
                HabitDetailFragment.newInstance( mHabitViewModel, habitID );

        getSupportFragmentManager().beginTransaction()
            .replace( R.id.main_fragment_container, habitDetailFragment )
            .addToBackStack( null )
            .commit();

        mHabitViewModel.getHabit( habitID ).observe( habitDetailFragment, habit -> {
            if ( habit == null ) {
                setTitle( getString( R.string.title_habits ) );
            }
            else {
                setTitle( habit.getTitle() );
            }
        } );

    }

    private void showNewHabitFragment() {

        NewHabitFragment newHabitFragment = NewHabitFragment.newInstance( mHabitViewModel );

        getSupportFragmentManager().beginTransaction()
            .replace( R.id.main_fragment_container, newHabitFragment )
            .addToBackStack( null )
            .commit();

        setTitle( getString( R.string.title_new_habit_page ) );
    }

    public void setTitle( String title ) {
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
            Log.e("BottomNav", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNav", "Unable to change value of shift mode", e);
        }
    }
}
