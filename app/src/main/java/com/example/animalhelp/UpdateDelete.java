package com.example.animalhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDelete extends DBActivity {
    protected EditText editName , editPhone, editEmail, editAddress,editProblem;
    protected Button btnUpdate, btnDelete;
    protected String ID;

    private void returnToMain(){
        finishActivity(200);
        Intent intent = new Intent(UpdateDelete.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        editProblem = findViewById(R.id.editProblem);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            ID = bundle.getString("ID");
            editName.setText(bundle.getString("Name"));
            editPhone.setText(bundle.getString("Phone"));
            editEmail.setText(bundle.getString("Email"));
            editAddress.setText(bundle.getString("Address"));
            editProblem.setText(bundle.getString("Problem"));
        }
        btnDelete.setOnClickListener(view ->{
            try {
                ExecSQL("DELETE FROM ANIMALS WHERE ID = ?",
                        new Object[]{ID},
                        () -> Toast.makeText(
                                getApplicationContext(),
                                "You delete this request successfully!",
                                Toast.LENGTH_LONG).show()
                        );
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Something went wrong: "+ex.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                returnToMain();
            }
        });

        btnUpdate.setOnClickListener(view ->{
            try {
                validation(editEmail,editPhone);
                ExecSQL("UPDATE ANIMALS SET "+
                                "Name = ?, " +
                                "Phone = ?, " +
                                "Email = ?, " +
                                "Address = ?," +
                                "Problem = ? " +
                                "WHERE ID = ?",
                        new Object[]{
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editAddress.getText().toString(),
                                editProblem.getText().toString(),
                                ID},
                        () -> Toast.makeText(
                                getApplicationContext(),
                                "You update this request successfully!",
                                Toast.LENGTH_LONG).show()
                );
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Something went wrong: "+ex.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                returnToMain();
            }
        });
    }
}