package zekem.check.habits.model.database;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import zekem.check.habits.model.HabitDay;

/**
 * @author Zeke Miller
 */
public class Converters {

    @TypeConverter
    public static String dateToString( @Nullable LocalDate localDate ) {
        return localDate == null ? null : localDate.toString();
    }

    @TypeConverter
    public static LocalDate stringToDate( @Nullable String string ) {
        return string == null ? null : LocalDate.parse( string );
    }

    @TypeConverter
    public static Map< LocalDate, HabitDay > jsonToMap( @Nullable String string ) {
        if ( string == null ) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter( LocalDate.class, new DateAdapter() )
                .create();

        Type type = new TypeToken< Map< LocalDate, HabitDay > >(){}.getType();
        return gson.fromJson( string, type );
    }

    @TypeConverter
    public static String mapToJson( @Nullable Map< LocalDate, HabitDay > map ) {
        if ( map == null ) {
            return null;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter( LocalDate.class, new DateAdapter() )
                .create();
        return gson.toJson( map );
    }

    private static class DateAdapter extends TypeAdapter< LocalDate > {

        @Override
        public void write( JsonWriter out, LocalDate value ) throws IOException {
            if ( value == null ) {
                out.nullValue();
                return;
            }
            out.value( value.toString() );
        }

        @Override
        public LocalDate read( JsonReader in ) throws IOException {

            if ( in.peek() == JsonToken.NULL ) {
                in.nextNull();
                return null;
            }
            return LocalDate.parse( in.nextString() );
        }
    }
}
