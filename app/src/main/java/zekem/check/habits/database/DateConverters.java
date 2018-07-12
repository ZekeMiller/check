package zekem.check.habits.database;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * @author Zeke Miller
 */
public class DateConverters {

    @TypeConverter
    public String dateToString( @Nullable LocalDate localDate ) {
        return localDate == null ? null : localDate.toString();
    }

    @TypeConverter
    public LocalDate stringToDate( @Nullable String string ) {
        return string == null ? null : LocalDate.parse( string );
    }
}
