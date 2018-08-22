package zekem.check.dailies.model.database;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.Map;

import zekem.check.dailies.model.scheduling.AlwaysScheduler;
import zekem.check.dailies.model.scheduling.Scheduler;

/**
 * @author Zeke Miller
 */
public class DailyConverters {

    private static final RuntimeTypeAdapterFactory typeFactory =
            RuntimeTypeAdapterFactory.of( Scheduler.class )
            .registerSubtype( AlwaysScheduler.class );

    private static final Gson gson =
            new GsonBuilder().registerTypeAdapterFactory( typeFactory ).create();


    @TypeConverter
    public static String schedulerToJson( @Nullable Scheduler scheduler ) {
        if ( scheduler == null ) {
            return null;
        }
        return gson.toJson( scheduler );
    }

    @TypeConverter
    public static Scheduler jsonToScheduler( @Nullable String json ) {
        if ( json == null ) {
            return null;
        }
        return gson.fromJson( json, Scheduler.class );
    }

    @TypeConverter
    public static Map< LocalDate, Boolean > jsonToMap( @Nullable String string ) {
        if ( string == null ) {
            return null;
        }
        Gson gson = new GsonBuilder().create();

        Type type = new TypeToken< Map< LocalDate, Boolean > >(){}.getType();
        return gson.fromJson( string, type );
    }

    @TypeConverter
    public static String mapToJson( @Nullable Map< LocalDate, Boolean > map ) {
        if ( map == null ) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        return gson.toJson( map );
    }
}
