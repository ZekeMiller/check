package zekem.check.habits;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import zekem.check.R;
import zekem.check.habits.listeners.NewHabitPageListener;

/**
 * @author Zeke Miller
 */
public class NewHabitFragment extends Fragment {


    private NewHabitPageListener mListener;

    public NewHabitFragment() {

    }

    public static NewHabitFragment newInstance( NewHabitPageListener listener ) {

        NewHabitFragment newHabitFragment;
        newHabitFragment = new NewHabitFragment();
        newHabitFragment.mListener = listener;
        return newHabitFragment;

    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_new_habit, container, false );

        Button button = view.findViewById( R.id.submit_new_habit );
        EditText habitName = view.findViewById( R.id.new_habit_name );
        button.setOnClickListener( v -> mListener.onSubmitPress( habitName.getText().toString() ) );

        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }
}
