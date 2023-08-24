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

import com.amplifyframework.core.Amplify;
import com.example.notification.R;

public class PasswordManager extends Fragment {
    private View mView;
    private TextView currentPassword_edt, newPassword_edt, confirmPassword_edt;
    private Button btn_change_password;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUI();
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        return mView;
    }

    private void changePassword() {

        String currentPassword = currentPassword_edt.getText().toString().trim();
        String newPassword = newPassword_edt.getText().toString().trim();
        String confirmNewPassword = confirmPassword_edt.getText().toString().trim();

        if (newPassword.equals(confirmNewPassword)) {
            Amplify.Auth.updatePassword(
                currentPassword,
                    newPassword,
                    () -> Log.i("ChangePassword", "Updated password successfully!"),
                    error -> Log.e("ChangePassword", error.toString())
            );
        } else {
            Log.i("ChangePassword", "New password and confirm new password do not match!");
            Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
        }
//        if (newPassword.equals(confirmPassword)) {
//            // Xác minh đăng nhập bằng mật khẩu hiện tại trước khi thay đổi mật khẩu
//            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
//            user.reauthenticate(credential)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                // Xác thực thành công, thay đổi mật khẩu mới
//                                user.updatePassword(newPassword)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(getActivity(), "Change Success", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(getActivity(), "Change Fail", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            } else {
//                                // Xác thực thất bại, mật khẩu hiện tại không chính xác
//                                Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
//        }
    }

    private void initUI() {
        currentPassword_edt = mView.findViewById(R.id.Current_Password);
        newPassword_edt = mView.findViewById(R.id.New_password);
        confirmPassword_edt = mView.findViewById(R.id.confirm_password);
        btn_change_password = mView.findViewById(R.id.btn_change_password);
    }
}
