package com.example.notification.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notification.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends Fragment {
    private View mView;
    private TextView  currentPassword_edt, newPassword_edt, confirmPassword_edt;
    private Button btn_change_password;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUI();
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_pass_word();
            }
        });
        return mView;
    }

    private void change_pass_word() {

        String currentPassword = currentPassword_edt.getText().toString().trim();
        String newPassword = newPassword_edt.getText().toString().trim();
        String confirmPassword = confirmPassword_edt.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (newPassword.equals(confirmPassword)) {
            // Xác minh đăng nhập bằng mật khẩu hiện tại trước khi thay đổi mật khẩu
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Xác thực thành công, thay đổi mật khẩu mới
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Change Success", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Change Fail", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Xác thực thất bại, mật khẩu hiện tại không chính xác
                                Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        currentPassword_edt = mView.findViewById(R.id.Current_Password);
        newPassword_edt = mView.findViewById(R.id.New_password);
        confirmPassword_edt = mView.findViewById(R.id.confirm_password);
        btn_change_password = mView.findViewById(R.id.btn_change_password);
    }
}
