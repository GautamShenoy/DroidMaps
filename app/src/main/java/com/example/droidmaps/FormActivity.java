package com.example.droidmaps;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FormActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText mName, mHeight, mAge, mColor, mSeason;
    private Button submit_btn;
    private double mLatitude, mLongitude;
    private final String KEY_geoJson="geoJSON";
    private static final String FILE_NAME="droidmaps_";
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
//                validation to prevent empty string in name field
                if (TextUtils.isEmpty(name)) {
                    mName.setError("name is a required field");
                    return;
                }
//                validation  to prevent empty string in color field
                if (TextUtils.isEmpty(color)) {
                    mColor.setError("Email is a required field");
                    return;
                }
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

                final String a = geoJSON.toString().replace("{","{").replace("}","}");
                final Deets deets = new Deets(a);
                reference.add(deets)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
//                                to store the file in local storage
                                FileOutputStream fos=null;
                                try {
                                    fos = openFileOutput(FILE_NAME+documentReference.getId()+".txt", MODE_PRIVATE);
                                    fos.write(a.getBytes());
                                    Toast.makeText(FormActivity.this, "Saved to "+getFilesDir()+"/"+FILE_NAME+documentReference.getId()+".txt", Toast.LENGTH_LONG).show();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }finally {
                                    if(fos!=null){
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
//                                to display the saved text file
//                                FileInputStream fis=null;
//                                try {
//                                    fis=openFileInput(FILE_NAME+documentReference.getId()+".txt");
//                                    InputStreamReader isr=new InputStreamReader(fis);
//                                    BufferedReader br=new BufferedReader(isr);
//                                    StringBuilder sb=new StringBuilder();
//                                    String text;
//                                    while ((text=br.readLine())!=null){
//                                        sb.append(text).append("\n");
//                                    }
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    if(fis!=null){
//                                        try {
//                                            fis.close();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
                            }
                        });
                startActivity(new Intent(FormActivity.this, MapActivity.class));

            }
        });

    }

}