package com.example.gymmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemberDetailsActivity extends AppCompatActivity {

    EditText etName, etPhone, etMonthlyFee, etMonths;
    TextView tvDetails;
    Button btnUpdate, btnDelete;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etMonthlyFee = findViewById(R.id.etMonthlyFee);
        etMonths = findViewById(R.id.etMonths);
        tvDetails = findViewById(R.id.tvDetails);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        int memberId = getIntent().getIntExtra("memberId", -1);
        member = MemberStorage.getById(memberId);

        if (member == null) {
            Toast.makeText(this, "Member not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName.setText(member.name);
        etPhone.setText(member.phone);
        etMonthlyFee.setText(String.valueOf(member.monthlyFee));
        etMonths.setText(String.valueOf(member.months));

        showDetails();

        btnUpdate.setOnClickListener(v -> updateMember());

        btnDelete.setOnClickListener(v -> deleteMember());
    }

    private void showDetails() {
        tvDetails.setText(
                "Plan: " + member.plan +
                        "\nStart Date: " + member.startDate +
                        "\nExpiry Date: " + member.expiryDate +
                        "\nTrainer: " + (member.trainer ? "Yes" : "No") +
                        "\nDiscount: " + member.discount + "%" +
                        "\nTotal Fee: Rs. " + String.format("%.2f", member.totalFee));
    }

    private void updateMember() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String feeText = etMonthlyFee.getText().toString().trim();
        String monthText = etMonths.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || feeText.isEmpty() || monthText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            member.name = name;
            member.phone = phone;
            member.monthlyFee = Double.parseDouble(feeText);
            member.months = Integer.parseInt(monthText);

            member.totalFee = member.monthlyFee * member.months;

            if (member.trainer) {
                member.totalFee += 1000;
            }

            double discountAmount = member.totalFee * member.discount / 100;
            member.totalFee -= discountAmount;

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date startDate = format.parse(member.startDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MONTH, member.months);
            member.expiryDate = format.format(calendar.getTime());

            MemberStorage.update(member);
            showDetails();
            Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMember() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Member")
                .setMessage("Are you sure you want to delete this member?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    MemberStorage.delete(member.id);
                    Toast.makeText(this, "Member deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}