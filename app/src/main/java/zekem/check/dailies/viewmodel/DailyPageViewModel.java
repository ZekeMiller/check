package zekem.check.dailies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import zekem.check.dailies.model.Daily;
import zekem.check.dailies.model.database.DailyDatabase;
import zekem.check.dailies.viewmodel.listener.DailyPageListener;

/**
 * @author Zeke Miller
 */
public class DailyPageViewModel extends AndroidViewModel implements DailyPageListener {

    // Fields

    @NonNull private final DailyDatabase mDailyDatabase;
    @NonNull private final DailyObservables mDailyObservables;

    // Constructor

    public DailyPageViewModel( Application application ) {

        super( application );

        mDailyDatabase = DailyDatabase.getDatabase( getApplication() );
        mDailyObservables = DailyObservables.getInstance();

    }


    // TODO fill in behavior for actions


    @Override
    public LiveData< List< Daily > > getDailies() {

        return mDailyDatabase.getAll();
    }

    @Override
    public void onToolbarAddButtonPress() {
        mDailyDatabase.addGeneric();
    }
}
