package zekem.check.dailies.viewmodel.listener;

import android.arch.lifecycle.LiveData;

import java.util.List;

import zekem.check.dailies.model.Daily;

/**
 * @author Zeke Miller
 */
public interface DailyPageListener {

    LiveData< List< Daily > > getDailies();

    void onToolbarAddButtonPress();

}
