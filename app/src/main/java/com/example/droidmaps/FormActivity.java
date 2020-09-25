package com.example.droidmaps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

public class FormActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText mName, mHeight, mAge, mColor, mSeason;
    private Button submit_btn;
    private double mLatitude, mLongitude;
    CollectionReference reference = db.collection("DATA");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();

        mLatitude = intent.getDoubleExtra("Latitude", 0);
        mLongitude = intent.getDoubleExtra("Longitude", 0);
        mName = findViewById(R.id.text_name);
        mAge = findViewById(R.id.text_age);
        mHeight = findViewById(R.id.text_height);
        mColor = findViewById(R.id.text_colorOfLeaf);
        mSeason = findViewById(R.id.text_seasonOfBearing);
        submit_btn = findViewById(R.id.btn_submit);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Editable name = mName.getText();
                final Editable age = mAge.getText();
                final Editable height = mHeight.getText();
                final Editable color = mColor.getText();
                final Editable season = mSeason.getText();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("Name", name);
                    obj.put("Age", age);
                    obj.put("Height", height);
                    obj.put("Color", color);
                    obj.put("Season", season);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Point point = new Point(mLatitude, mLongitude);
                Feature feature = new Feature(point);
                feature.setProperties(obj);
                JSONObject geoJSON = null;
                try {
                    geoJSON = feature.toJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("GIS", geoJSON.toString());

                String a = geoJSON.toString().replace("{", "{");
                String b = a.toString().replace("}", "}");
                Deets deets = new Deets(b);
                reference.add(deets)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(FormActivity.this, "Data successfully added to firestore!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                startActivity(new Intent(FormActivity.this, MapActivity.class));

            }
        });

    }

}