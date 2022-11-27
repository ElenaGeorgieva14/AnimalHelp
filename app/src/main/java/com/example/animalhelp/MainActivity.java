package com.example.animalhelp;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;

public class MainActivity extends DBActivity {
    protected EditText editName , editPhone, editEmail, editAddress,editProblem;
    protected Button btnInsert, btnChangeLocation, btnViewVets;
    protected ListView oldRequestList, vetListView;

    protected void FillListView() throws Exception{
        final ArrayList<String> oldRequests = new ArrayList<String>();
        SelectSQL(
                "SELECT * FROM ANIMALS",
                null,
                (ID, Name,Phone,Email,Address,Problem) ->{
                    oldRequests.add(ID + "\t" + Name + "\t" +
                            Phone + "\t" +Email + "\t" +Address + "\t" + Problem + "\n");
                }
        );
        oldRequestList.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                oldRequests
        );
        oldRequestList.setAdapter(arrayAdapter);
    }
    protected  void FillVetListView() throws Exception{
        ArrayList<Vet> vets = VetLocation.getNearbyVets();
        if(vets == null){
           Toast.makeText(
                    getApplicationContext(),
                    "There is no vets nearby",
                    Toast.LENGTH_LONG).show();
        }else{
            final ArrayList<String> result = new ArrayList<String>();
            for (Vet vet: vets) {
                result.add("Name: "+vet.getName() + "\n"+ "Latitude: "+vet.getLatitude()+ "\n" +"Longitude" + vet.getLongitude() + "\n" + "Is open:" + vet.getIsOpenNow());
            }
            vetListView.clearChoices();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.activity_list_view,
                    R.id.textView,
                    result
            );
            vetListView.setAdapter(arrayAdapter);
        }

    }
    @Override
    @CallSuper
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        editProblem = findViewById(R.id.editProblem);
        btnInsert = findViewById(R.id.btnInsert);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);
        btnViewVets = findViewById(R.id.btnViewVets);
        oldRequestList = findViewById(R.id.oldRequestList);
        vetListView = findViewById(R.id.vetList);

        oldRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView clickedText = view.findViewById(R.id.textView);
                String selected = clickedText.getText().toString();
                String[] elements = selected.split("\t");
                String ID = elements[0];
                String Name = elements[1];
                String Phone = elements[2];
                String Email = elements[3];
                String Address = elements[4];
                String Problem = elements[5].trim();
                Intent intent = new Intent( MainActivity.this, UpdateDelete.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID",ID);
                bundle.putString("Name",Name);
                bundle.putString("Phone",Phone);
                bundle.putString("Email",Email);
                bundle.putString("Address",Address);
                bundle.putString("Problem",Problem);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200, bundle);
            }
        });

        try {
            initDB();
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnInsert.setOnClickListener(view -> {
            try {
                validation(editEmail,editPhone);
                ExecSQL(
                        "INSERT INTO ANIMALS(Name, Phone ,Email , Address, Problem)" +
                                "VALUES(?,?,?,?,?)",
                        new Object[]{
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editEmail.getText().toString(),
                                editAddress.getText().toString(),
                                editProblem.getText().toString()
                        },
                        () -> Toast.makeText(
                                getApplicationContext(),
                                "The request has been successfully submitted. Thank you!",
                                Toast.LENGTH_LONG).show()
                );
                FillListView();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),
                        "Insert Failed:" + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LocationEditor.class);
                startActivity(intent);
            }
        });

        btnViewVets.setOnClickListener(view -> {
            try {
                FillVetListView();
            } catch (Exception e) {
                System.out.println("Exception: "+e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }
}