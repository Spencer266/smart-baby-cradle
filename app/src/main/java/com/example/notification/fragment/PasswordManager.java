package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.core.Amplify;
import com.example.notification.R;

public class PasswordManager extends Fragment {
    private View mView;
    private TextView currentPassword, newPassword, confirmPassword;
    private Button changePasswordButton;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUI();
        changePasswordButton.setOnClickListener(view -> changePassword());
        return mView;
    }

    private void changePassword() {

        String tmpCurrentPassword = currentPassword.getText().toString().trim();
        String tmpNewPassword = newPassword.getText().toString().trim();
        String tmpConfirmNewPassword = confirmPassword.getText().toString().trim();

        if (tmpNewPassword.equals(tmpConfirmNewPassword)) {
            Amplify.Auth.updatePassword(
                tmpCurrentPassword,
                    tmpNewPassword,
                    () -> {
                        Log.i("ChangePassword", "Updated password successfully!");

                        try {
                            getActivity().runOnUiThread(() -> Toast.makeText(
                                    getActivity(),
                                    "Updated password successfully!",
                                    Toast.LENGTH_SHORT
                            ));
                        } catch (NullPointerException e) {
                            Log.e("ChangePassword", "Can not show toast message", e);
                        }

                    },
                    error -> {
                        Log.e("ChangePassword", "Could not update password: " + error);

                        try {
                            getActivity().runOnUiThread(() -> Toast.makeText(
                                    getActivity(),
                                    error.getCause().toString(),
                                    Toast.LENGTH_SHORT).show()
                            );
                        } catch (NullPointerException e) {
                            Log.e("ChangePassword", "Can not show toast message", e);
                        }
                    }
            );
        } else {
            Log.i("ChangePassword", "New password and confirm new password do not match!");
            Toast.makeText(getContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        currentPassword = mView.findViewById(R.id.PasswordManager_currentPassword);
        newPassword = mView.findViewById(R.id.PasswordManager_newPassword);
        confirmPassword = mView.findViewById(R.id.PasswordManager_confirmPassword);
        changePasswordButton = mView.findViewById(R.id.PasswordManager_changePasswordButton);
    }
}
