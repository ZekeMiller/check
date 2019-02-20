package zekem.check.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * @author Zeke Miller
 */
public class DateAdapter extends TypeAdapter< LocalDate > {

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