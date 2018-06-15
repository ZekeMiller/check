package zekem.check.datas;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Zeke Miller
 */
public class Data {

    private int id;
    private String title;


    public Data( int id, String title ) {
        this.id = id;
        this.title = title;
    }



    public int getId() {

        return id;
    }


    public String getTitle() {

        return title;
    }



    public static class DataDummy {

        public static final List< Data > DATAS = new ArrayList<>();
        public static final int AMOUNT = 20;

        static {
            for ( int i = 0 ; i <= AMOUNT ; i++ ) {
                DATAS.add( new Data( i, String.format( Locale.getDefault(), "Data %d", i ) ) );
            }
        }


    }

}
