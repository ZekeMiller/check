package zekem.check.dailies.model.database;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.Map;

import zekem.check.dailies.model.scheduling.AlwaysScheduler;
import zekem.check.dailies.model.scheduling.DailyScheduler;
import zekem.check.dailies.model.scheduling.Scheduler;
import zekem.check.util.DateAdapter;

/**
 * @author Zeke Miller
 */
public class DailyConverters {

    private static final RuntimeTypeAdapterFactory<Scheduler> typeFactory =
            RuntimeTypeAdapterFactory.of( Scheduler.class, "type" )
            .registerSubtype( AlwaysScheduler.class, AlwaysScheduler.class.getName() )
            .registerSubtype( DailyScheduler.class, DailyScheduler.class.getName() );

    private static final Gson gson =
            new GsonBuilder()
                    .registerTypeAdapterFactory( typeFactory )
                    .registerTypeAdapter( LocalDate.class, new DateAdapter() )
                    .create();


    @TypeConverter
    public static String schedulerToJson( @Nullable Scheduler scheduler ) {
        if ( scheduler == null ) {
            return null;
        }
        String json = gson.toJson( scheduler );
        Log.d( "check_log", String.format( "Serialized %s", json ) );
        return json;
    }

    @TypeConverter
    public static Scheduler jsonToScheduler( @Nullable String json ) {
        if ( json == null ) {
            return null;
        }
        Log.d( "check_log", String.format( "Deserialized %s", json ) );
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
