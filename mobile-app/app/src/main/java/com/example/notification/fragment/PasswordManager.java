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
        onClickChangePasswordButton();
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
                        Log.i("changePassword", "Updated password successfully!");

                        ToastMessage.showToastMessage(
                                getActivity(),
                                "Updated password successfully!",
                                "changePassword");
                    },
                    error -> {
                        Log.e("changePassword", "Could not update password: " + error);

                        ToastMessage.showToastMessage(
                                getActivity(),
                                error.getCause().toString(),
                                "changePassword"
                        );
                    }
            );
        } else {
            Log.i("changePassword", "New password and confirm new password do not match!");
            ToastMessage.showToastMessage(
                    getActivity(),
                    "New password and confirm password do not match",
                    "ChangePassword"
            );
        }
    }

    void onClickChangePasswordButton() {
        changePasswordButton.setOnClickListener(view -> changePassword());
    }

    private void initUI() {
        currentPassword      = mView.findViewById(R.id.PasswordManager_currentPassword);
        newPassword          = mView.findViewById(R.id.PasswordManager_newPassword);
        confirmPassword      = mView.findViewById(R.id.PasswordManager_confirmPassword);
        changePasswordButton = mView.findViewById(R.id.PasswordManager_changePasswordButton);
    }
}
