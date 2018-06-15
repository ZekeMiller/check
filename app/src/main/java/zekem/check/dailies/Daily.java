package zekem.check.dailies;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Zeke Miller
 */
public class Daily {

    private int id;
    private String title;


    public Daily( int id, String title ) {
        this.id = id;
        this.title = title;
    }



    public int getId() {

        return id;
    }


    public String getTitle() {

        return title;
    }



    public static class DailyDummy {

        public static final List< Daily > DAILIES = new ArrayList<>();
        public static final int AMOUNT = 20;

        static {
            for ( int i = 0 ; i <= AMOUNT ; i++ ) {
                DAILIES.add( new Daily( i, String.format( Locale.getDefault(), "Daily %d", i ) ) );
            }
        }


    }

}
