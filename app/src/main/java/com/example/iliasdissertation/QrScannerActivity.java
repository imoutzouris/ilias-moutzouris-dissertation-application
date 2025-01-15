package com.example.iliasdissertation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QrScannerActivity extends AppCompatActivity {

    // Register the launcher and result handler
    private ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(QrScannerActivity.this, "Scan Unsuccessful. Please try again.", Toast.LENGTH_LONG).show();
                    qrScannerStart();
                } else {
                        try {
                            String id = result.getContents().toString();
                            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("restaurant", id);
                            editor.apply();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("restaurants").child(id);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(QrScannerActivity.this, "Scan Successful. Menu is loading.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(QrScannerActivity.this, Categories.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(QrScannerActivity.this, "Wrong Restaurant id. Please try again.", Toast.LENGTH_LONG).show();
                                        qrScannerStart();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(QrScannerActivity.this, "Problem with loading Menu. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(QrScannerActivity.this, "Unexpected QR content. Please try again.", Toast.LENGTH_LONG).show();
                            qrScannerStart();
                        }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        qrScannerStart();
    }

    public void qrScannerStart(){
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan restaurant QR code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }
}