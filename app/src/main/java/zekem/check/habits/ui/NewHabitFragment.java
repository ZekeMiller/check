package zekem.check.habits.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import zekem.check.R;
import zekem.check.habits.listener.NewHabitPageListener;
import zekem.check.habits.viewmodel.NewHabitViewModel;

/**
 * @author Zeke Miller
 */
public class NewHabitFragment extends Fragment {

    private View view;

    private NewHabitPageListener mListener;

    public NewHabitFragment() {

    }

    public static NewHabitFragment newInstance() {

        NewHabitFragment newHabitFragment;
        newHabitFragment = new NewHabitFragment();
        return newHabitFragment;

    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mListener = ViewModelProviders.of( this ).get( NewHabitViewModel.class );
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_new_habit, container, false );

        Button button = view.findViewById( R.id.submit_new_habit );
        button.setOnClickListener( v -> onSubmitPress() );

        this.view = view;

        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }


    private void onSubmitPress() {

        EditText habitName = view.findViewById( R.id.new_habit_name );
        String name = habitName.getText().toString();

        CheckBox minusActiveCheckBox = view.findViewById( R.id.minusActiveCheckBox );
        boolean minusActive = minusActiveCheckBox.isChecked();

        CheckBox plusActiveCheckBox = view.findViewById( R.id.plusActiveCheckBox );
        boolean plusActive = plusActiveCheckBox.isChecked();

        mListener.onSubmitPress( name, minusActive, plusActive );
    }
}
