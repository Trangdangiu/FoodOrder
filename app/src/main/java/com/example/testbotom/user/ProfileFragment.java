package com.example.testbotom.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.LoginAndRegister.LogginActivity;
import com.example.testbotom.R;

public class ProfileFragment extends Fragment {
    TextView txt_history_order, txt_change_pass, txt_logout;
    private Create_database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txt_history_order = view.findViewById(R.id.txt_history_order);
        txt_change_pass = view.findViewById(R.id.txt_change_pass);
        txt_logout = view.findViewById(R.id.txt_logout);
        database = new Create_database(getContext());

        txt_logout.setOnClickListener(view1 -> logout());
        txt_change_pass.setOnClickListener(view2 -> showChangePasswordDialog());

        return view;
    }
    //dialog đổi pasword
    private void showChangePasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_change_pass, null);

        EditText editOldPass = dialogView.findViewById(R.id.editTextOldPassword);
        EditText editNewPass = dialogView.findViewById(R.id.editTextNewPassword);
        Button btnChange = dialogView.findViewById(R.id.buttonChangePassword);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Đổi mật khẩu")
                .setView(dialogView)
                .setCancelable(true) // đóng hộp thoại băng cách click ra ngoài
                .create();

        btnChange.setOnClickListener(view -> {
            String oldPassword = editOldPass.getText().toString().trim();
            String newPassword = editNewPass.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (changePassword(oldPassword, newPassword)) {
                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Đóng dialog khi đổi mật khẩu thành công
            } else {
                Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    // đổi mật khẩu
    private boolean changePassword(String oldPassword, String newPassword) {
        // Lấy email người dùng hiện tại từ SharedPreferences lưu trũ ở login
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPreferences.getString("user_email", null);
        String currentRole = database.loginUser(currentEmail, oldPassword);

        if (currentRole != null) {
            return database.updatePassword(currentEmail, newPassword); // Cập nhật mật khẩu mới
        }
        return false; // Nếu mật khẩu cũ không đúng
    }
    // logout
    private void logout() {
        Intent intent = new Intent(getContext(), LogginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Kết thúc Activity hiện tại
    }
}