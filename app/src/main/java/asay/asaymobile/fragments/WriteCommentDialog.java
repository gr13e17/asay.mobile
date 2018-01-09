package asay.asaymobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import asay.asaymobile.R;

/**
 * Dialog fragment to show the pop up where user can write a comment
 */

public class WriteCommentDialog extends DialogFragment {

    public interface WriteCommentListener {
        void onSave(String comment, Double parentId);
    }

    public static WriteCommentDialog newInstance(Double parentId) {
        System.out.println("newInstance");
        WriteCommentDialog dialog = new WriteCommentDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putDouble("parentId", parentId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        System.out.println("onAttach");
        super.onAttach(context);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("onCreateDialog");
        final View writeCommentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_comment, null);
        Dialog dialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        dialog.setContentView(writeCommentView);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        final Button replyButton = writeCommentView.findViewById(R.id.reply_button);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == replyButton) {
                    EditText commentEditText = writeCommentView.findViewById(R.id.content);
                    String commentContent = commentEditText.getText().toString();
                    commentContent = commentContent.trim(); //trim string for trailing and leading whitespaces

                    // Show error if no comment is written
                    if (commentContent.length() == 0) {
                        commentEditText.setError(getResources().getString(R.string.error_no_text));
                        return;
                    }

                    ((WriteCommentListener) getParentFragment()).onSave(commentContent, getArguments().getDouble("parentId")); // save comment
                }
            }
        });

        return dialog;
    }
}
