package zekem.check.habits.database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDate;

/**
 * @author Zeke Miller
 */
public class DateConverters {

    @TypeConverter
    public String dateToString( LocalDate localDate ) {
        return localDate.toString();
    }

    @TypeConverter
    public LocalDate stringToDate( String string ) {
        return LocalDate.parse( string );
    }
}
