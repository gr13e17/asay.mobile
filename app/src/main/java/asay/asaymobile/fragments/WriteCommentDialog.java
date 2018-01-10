package asay.asaymobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
        WriteCommentDialog dialog = new WriteCommentDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putDouble("parentId", parentId);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View writeCommentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_write_comment, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(writeCommentView);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        final Button replyButton = writeCommentView.findViewById(R.id.reply_button);
        final EditText commentEditText = writeCommentView.findViewById(R.id.content);

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentEditText.getText().toString();
                commentContent = commentContent.trim(); //trim string for trailing and leading whitespaces
                ((WriteCommentListener) getParentFragment()).onSave(commentContent, getArguments().getDouble("parentId")); // save comment
            }
        });

        commentEditText.addTextChangedListener(new TextWatcher() {
            ColorStateList oldColors =  replyButton.getTextColors();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    replyButton.setEnabled(false);
                    replyButton.setTextColor(oldColors);
                } else {
                    replyButton.setEnabled(true);
                    replyButton.setTextColor(getResources().getColor(R.color.primaryColor));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        writeCommentView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialog;
    }
}