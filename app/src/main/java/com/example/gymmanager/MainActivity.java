package com.example.gymmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvTotalMembers,tvTotalIncome;
    Button btnAddMember,btnViewMembers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnViewMembers = findViewById(R.id.btnViewMembers);

        btnAddMember.setOnClickListener(v ->
               startActivity(new Intent(this,AddMemberActivity.class)) );

        btnViewMembers.setOnClickListener(v ->
                startActivity(new Intent(this, MemberListActivity.class)));

    }

    protected void onResume(){
        super.onResume();
        tvTotalMembers.setText("Total Members: " + MemberStorage.members.size());
        tvTotalIncome.setText("Total Income: Rs. " + String.format("%.2f", MemberStorage.getTotalIncome()));
    }
}