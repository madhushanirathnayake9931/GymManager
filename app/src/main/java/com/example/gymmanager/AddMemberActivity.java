package com.example.gymmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {

    EditText etName, etPhone, etMonthlyFee, etMonths, etStartDate;
    RadioGroup rgPlan;
    RadioButton rbMonthly, rbQuarterly, rbAnnual;
    CheckBox cbTrainer;
    SeekBar seekDiscount;
    TextView tvDiscount, tvResult;
    Button btnCalculate, btnSave;

    double totalFee = 0;
    String expiryDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etMonthlyFee = findViewById(R.id.etMonthlyFee);
        etMonths = findViewById(R.id.etMonths);
        etStartDate = findViewById(R.id.etStartDate);
        rgPlan = findViewById(R.id.rgPlan);
        rbMonthly = findViewById(R.id.rbMonthly);
        rbQuarterly = findViewById(R.id.rbQuarterly);
        rbAnnual = findViewById(R.id.rbAnnual);
        cbTrainer = findViewById(R.id.cbTrainer);
        seekDiscount = findViewById(R.id.seekDiscount);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvResult = findViewById(R.id.tvResult);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);

        seekDiscount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDiscount.setText("Discount: " + progress + "%");
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnCalculate.setOnClickListener(v -> calculateFee());

        btnSave.setOnClickListener(v -> saveMember());
    }

    private String getSelectedPlan() {
        if (rbQuarterly.isChecked()) return "Quarterly";
        if (rbAnnual.isChecked()) return "Annual";
        return "Monthly";
    }

    private void calculateFee() {
        String feeText = etMonthlyFee.getText().toString().trim();
        String monthText = etMonths.getText().toString().trim();
        String dateText = etStartDate.getText().toString().trim();

        if (feeText.isEmpty() || monthText.isEmpty() || dateText.isEmpty()) {
            Toast.makeText(this, "Please fill fee, months and start date", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double monthlyFee = Double.parseDouble(feeText);
            int months = Integer.parseInt(monthText);
            int discount = seekDiscount.getProgress();
            double trainerCharge = cbTrainer.isChecked() ? 1000 : 0;

            double basicFee = monthlyFee * months;
            double discountAmount = (basicFee + trainerCharge) * discount / 100;
            totalFee = basicFee + trainerCharge - discountAmount;

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date startDate = format.parse(dateText);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MONTH, months);
            expiryDate = format.format(calendar.getTime());

            tvResult.setText(
                    "Plan: " + getSelectedPlan() +
                            "\nStart Date: " + dateText +
                            "\nExpiry Date: " + expiryDate +
                            "\nMonthly Fee: Rs. " + monthlyFee +
                            "\nTrainer Charge: Rs. " + trainerCharge +
                            "\nDiscount: " + discount + "%" +
                            "\nTotal Fee: Rs. " + String.format("%.2f", totalFee));

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMember() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalFee <= 0 || expiryDate.isEmpty()) {
            Toast.makeText(this, "Calculate fee before saving", Toast.LENGTH_SHORT).show();
            return;
        }

        Member member = new Member(
                0,
                name,
                phone,
                getSelectedPlan(),
                etStartDate.getText().toString().trim(),
                Integer.parseInt(etMonths.getText().toString().trim()),
                Double.parseDouble(etMonthlyFee.getText().toString().trim()),
                cbTrainer.isChecked(),
                seekDiscount.getProgress(),
                totalFee,
                expiryDate
        );

        MemberStorage.add(member);
        Toast.makeText(this, "Member saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
