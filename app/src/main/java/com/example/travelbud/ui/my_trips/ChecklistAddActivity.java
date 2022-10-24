package com.example.travelbud.ui.my_trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelbud.R;

public class ChecklistAddActivity extends AppCompatActivity {
    EditText editName;
    EditText editAmount;
    Spinner spinner;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Item Checklist");

        editName = findViewById(R.id.add_name);
        editAmount = findViewById(R.id.add_amount);
        spinner = findViewById(R.id.add_category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position,
                                       long id) {
                category = spinner.getSelectedItem().toString();
//                Log.i("checklist", "current: " + category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void onSubmit(View v) {
        if (this.editName.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please input the name.", Toast.LENGTH_SHORT).show();
            return;
        } else if (this.editAmount.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please input the amount.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent result = new Intent();
        result.putExtra("name", editName.getText().toString());
        result.putExtra("amount", editAmount.getText().toString());
        result.putExtra("category", category);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
