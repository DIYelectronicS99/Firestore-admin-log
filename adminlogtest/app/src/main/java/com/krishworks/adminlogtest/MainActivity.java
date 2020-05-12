package com.krishworks.adminlogtest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {


    private static final String mac = "mac";
    private static final String space = "space";
    private  static final String timestamp = "timestamp";
    private static final String imei = "imei";
    //private static final String id = "id";
    private static final String datefield = "date";
    public  long devID;

    private String wifiInfo;
    private String deviceID;
    private  String availspace;
    Timestamp timestamptest;



    private Long  availableBlocks;
    private String format,date;

    private Date date01;




    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbref = db.collection("admin02");
    private DocumentReference stats = db.collection("admin02").document("stats");


    private myAdapter adapter;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setview();

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo = wifiMgr.getConnectionInfo().toString();


        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
         /*
         * getDeviceId() returns the unique device ID.
         * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */



        deviceID = telephonyManager.getDeviceId();


        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        Long blockSize = stat.getBlockSizeLong();
        availableBlocks = stat.getAvailableBlocksLong();

        availspace = Long.toString(availableBlocks);


        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy'@'hh:mm:ss");
        SimpleDateFormat dateID = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //timestamptest = new Timestamp(currentTimeMillis());

        format = s.format(new Date());
        date = dateID.format(new Date());


        try {
            date01 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        saveNote();


    }

    private  void setview(){

        Query query = dbref.orderBy(datefield, Query.Direction.DESCENDING);

        //Query query = dbref;
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new myAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rclv1);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void saveNote()
    {
        Map<String, Object> note = new HashMap<>();

        Map<String, Object> ID = new HashMap<>();

        dbref.document("stats").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    devID = (long) documentSnapshot.get("id");
                    //  devID = Integer.parseInt(testID);

                }

            }
        });

        //devID += 1;

        //note.put("id", devID);

         //dbref.document("stats").update("id", devID);
        //dbref.document(format).set(ID, SetOptions.merge());




        note.put(mac, wifiInfo);
        note.put(space,availspace);
        note.put(imei, deviceID);
        note.put(timestamp, String.valueOf(date01));
        //note.put("id", devID);
        note.put(datefield, date01 );



        dbref.document(format).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Device state saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Cannot connect !", Toast.LENGTH_SHORT).show();

            }
        });

        //int devID = Integer.parseInt(String.valueOf(dbref.document("stats").get()));
        //dbref.orderBy("timestamp", Query.Direction.DESCENDING);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbref.document("stats").update("id", devID+1);

        adapter.stopListening();
    }


}
