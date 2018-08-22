package zekem.check.dailies.viewmodel;

/**
 * @author Zeke Miller
 */
public class DailyObservables {


    private static DailyObservables sInstance;

    public static DailyObservables getInstance() {
        if ( sInstance == null ) {
            sInstance = new DailyObservables();
        }
        return sInstance;
    }

}
