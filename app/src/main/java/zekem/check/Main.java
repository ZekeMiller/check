package zekem.check;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import java.lang.reflect.Field;

import zekem.check.dailies.DailyPageFragment;
import zekem.check.dailies.Daily;
import zekem.check.datas.Data;
import zekem.check.datas.DataPageFragment;
import zekem.check.habits.HabitPageFragment;

public class Main extends AppCompatActivity implements
                DailyPageFragment.OnListFragmentInteractionListener,
                DataPageFragment.OnListFragmentInteractionListener,
                AnalyticsPageFragment.OnFragmentInteractionListener {

    private HabitPageFragment habitsFragment;
    private DailyPageFragment dailiesFragment;
    private DataPageFragment datasFragment;
    private AnalyticsPageFragment analyticsPageFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( @NonNull MenuItem item ) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment;
            String title;
            Toolbar.OnMenuItemClickListener onMenuItemClickListener;

            // TODO decide if I should reuse button or make different buttons or make different
            // bars in general or what overall depending on behavior, probably best to just make
            // a different bar for each page so everything can be separate and such but I'll do
            // that another day
            switch ( item.getItemId() ) {
                case R.id.navigation_habits:
                    fragment = habitsFragment;
                    onMenuItemClickListener = habitsFragment.getButtonListener();
                    title = getString( R.string.title_habits );
                    break;
                case R.id.navigation_dailies:
                    fragment = dailiesFragment;
                    onMenuItemClickListener = null; // TODO actual listener
                    title = getString( R.string.title_dailies );
                    break;
                case R.id.navigation_datas:
                    fragment = datasFragment;
                    onMenuItemClickListener = null; // TODO actual listener
                    title = getString( R.string.title_datas );
                    break;
                case R.id.navigation_analytics:
                    fragment = analyticsPageFragment;
                    onMenuItemClickListener = null; // TODO actual listener
                    title = getString( R.string.title_analytics );
                    break;
                default:
                    return false;
            }
            transaction.replace( R.id.main_fragment_container, fragment );
            transaction.commit();
            Toolbar toolbar = findViewById( R.id.toolbar );
            toolbar.setTitle( title );
            toolbar.setOnMenuItemClickListener( onMenuItemClickListener );
            return true;
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        BottomNavigationView navigation = findViewById( R.id.navigation );
        removeShiftMode( navigation );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        habitsFragment = HabitPageFragment.newInstance();
        dailiesFragment = DailyPageFragment.newInstance( 1 );
        datasFragment = DataPageFragment.newInstance( 1 );
        analyticsPageFragment = AnalyticsPageFragment.newInstance();

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        navigation.setSelectedItemId( R.id.navigation_habits );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.toolbar, menu );
        return true;
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
