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

/**
 * The main Activity, has a bottom navigation and holds other Fragments
 */
public class Main extends AppCompatActivity implements
                DailyPageFragment.OnListFragmentInteractionListener,
                DataPageFragment.OnListFragmentInteractionListener,
                AnalyticsPageFragment.OnFragmentInteractionListener {

    private HabitViewModel habitViewModel;
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

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment;
            String title;

            switch ( item.getItemId() ) {
                case R.id.navigation_habits:
                    fragment = habitsFragment;
                    habitsFragment.setViewModel( habitViewModel );
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
            transaction.addToBackStack( null );
            // TODO fix bottom nav appearing wrong on back press
            transaction.commit();
            Toolbar toolbar = findViewById( R.id.toolbar );
            toolbar.setTitle( title );
            return true;
        }
    };

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

        habitViewModel = ViewModelProviders.of( this ).get( HabitViewModel.class );
        habitViewModel.getDetail().observe( this, habitID -> {

            HabitDetailFragment habitDetailFragment =
                    HabitDetailFragment.newInstance( habitViewModel, habitID );
            getSupportFragmentManager().beginTransaction()
                    .replace( R.id.main_fragment_container, habitDetailFragment )
                    .addToBackStack( null )
                    .commit();

        });

        habitsFragment = HabitPageFragment.newInstance();
        dailiesFragment = DailyPageFragment.newInstance( 1 );
        dataFragment = DataPageFragment.newInstance( 1 );
        analyticsPageFragment = AnalyticsPageFragment.newInstance();

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        navigation.setSelectedItemId( R.id.navigation_habits );
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
