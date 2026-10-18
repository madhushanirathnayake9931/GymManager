package com.example.gymmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MemberListActivity extends AppCompatActivity {

    ListView listMembers;
    TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        listMembers = findViewById(R.id.listMembers);
        tvEmpty = findViewById(R.id.tvEmpty);

        listMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member member = MemberStorage.members.get(position);
            Intent intent = new Intent(this, MemberDetailsActivity.class);
            intent.putExtra("memberId", member.id);
            startActivity(intent);
        });

        listMembers.setOnItemLongClickListener((parent, view, position, id) -> {
            Member member = MemberStorage.members.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Delete Member")
                    .setMessage("Delete " + member.name + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        MemberStorage.delete(member.id);
                        loadMembers();
                        Toast.makeText(this, "Member deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }

    private void loadMembers() {
        ArrayList<String> displayList = new ArrayList<>();

        for (Member member : MemberStorage.members) {
            displayList.add(member.name + "\n" + member.phone + "\nPlan: " + member.plan);
        }

        if (displayList.isEmpty()) {
            tvEmpty.setVisibility(TextView.VISIBLE);
        } else {
            tvEmpty.setVisibility(TextView.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, displayList);

        listMembers.setAdapter(adapter);
    }
}