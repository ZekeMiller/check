package zekem.check.habits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import zekem.check.R;
import zekem.check.habits.listeners.HabitDeleteDialogListener;

/**
 * @author Zeke Miller
 */
public class DeleteHabitDialogFragment extends DialogFragment {

    private HabitDeleteDialogListener mListener;
    private int habitId;

    public DeleteHabitDialogFragment() {

    }

    public static DeleteHabitDialogFragment newInstance( HabitDeleteDialogListener listener,
                                                         int habitId ) {

        DeleteHabitDialogFragment deleteHabitDialogFragment;
        deleteHabitDialogFragment = new DeleteHabitDialogFragment();
        deleteHabitDialogFragment.mListener = listener;
        deleteHabitDialogFragment.habitId = habitId;
        return deleteHabitDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setTitle( R.string.delete_habit_dialog_title )
                .setMessage( R.string.delete_habit_confirmation_text )
                .setPositiveButton( R.string.delete_habit_confirm, ( dialog, id ) -> delete() )
                .setNegativeButton( R.string.delete_habit_cancel, ( dialog, id ) -> cancel() );

        return builder.create();
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    private void delete() {
        mListener.delete( habitId );
        dismiss();
    }

    private void cancel() {
        dismiss();
    }
}
