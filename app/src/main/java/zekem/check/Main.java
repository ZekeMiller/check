package zekem.check;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import zekem.check.dailies.DailyPageFragment;
import zekem.check.dailies.Daily;
import zekem.check.datas.Data;
import zekem.check.datas.DataPageFragment;
import zekem.check.habits.Habit;
import zekem.check.habits.HabitDao;
import zekem.check.habits.HabitDatabase;
import zekem.check.habits.HabitDay;
import zekem.check.habits.HabitPageFragment;

public class Main extends AppCompatActivity implements
                HabitPageFragment.OnListFragmentInteractionListener,
                DailyPageFragment.OnListFragmentInteractionListener,
                DataPageFragment.OnListFragmentInteractionListener,
                AnalyticsFragment.OnFragmentInteractionListener {

    private Fragment habitsFragment;
    private Fragment dailiesFragment;
    private Fragment datasFragment;
    private Fragment analyticsFragment;

    private static final String HABIT_DATABASE = "db_habit";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( @NonNull MenuItem item ) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment;

            switch ( item.getItemId() ) {
                case R.id.navigation_habits:
                    fragment = habitsFragment;
                    break;
                case R.id.navigation_dailies:
                    fragment = dailiesFragment;
                    break;
                case R.id.navigation_datas:
                    fragment = datasFragment;
                    break;
                case R.id.navigation_analytics:
                    fragment = analyticsFragment;
                    break;
                default:
                    return false;
            }
            transaction.replace( R.id.main_fragment_container, fragment );
            transaction.commit();
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

        habitsFragment = HabitPageFragment.newInstance( 1 );
        dailiesFragment = DailyPageFragment.newInstance( 1 );
        datasFragment = DataPageFragment.newInstance( 1 );
        analyticsFragment = AnalyticsFragment.newInstance();

        new Thread( new Runnable() {
            @Override
            public void run() {
                if ( HabitDatabase.getHabitDatabase( getApplicationContext() ).habitDao().getAll().size() == 0 ) {
                    HabitDatabase.getHabitDatabase( getApplicationContext() ).habitDao().insert( Habit.HabitDummy.HABITS );
                }
            }
        } ).start();

        navigation.setSelectedItemId( R.id.navigation_habits );
    }

    @Override
    public void onFragmentInteraction( Uri uri ) {

    }

    @Override
    public void onListFragmentInteraction( Daily item ) {

    }

    @Override
    public void onListFragmentInteraction( Data item ) {

    }

    @Override
    public void onListFragmentInteraction( Habit item ) {

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
