package com.example.powitanie;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText imie;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        createNotificationChannel();

        imie = findViewById(R.id.editTextImie);
        Button wysli = findViewById(R.id.buttonPowitanie);

        wysli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String podajImie = imie.getText().toString().trim();

                if (podajImie.isEmpty()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Błąd")
                            .setMessage("Proszę wpisać swoje imię!")
                            .setPositiveButton("OK", (dialog, i) -> dialog.dismiss())
                            .create().show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Potwierdzenie")
                            .setMessage("Cześć " + podajImie + "! Czy chcesz otrzymać powiadomienie powitalne?")
                            .setPositiveButton("Tak, poproszę", (dialog, i) -> {
                                sendpowitanie();
                                dialog.dismiss();
                            })
                            .setNegativeButton("Nie, dziękuję", (dialog, i) -> {
                                Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .create().show();
                }
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kanał powiadomień";
            String description = "Opis kanału powiadomień";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

        public void sendpowitanie() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS},1);
                    return;
                }
            }

            String podajImie = imie.getText().toString().trim();



            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Witaj!")
                    .setContentText("Miło Cię widzieć, " + podajImie + "!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Toast.makeText(this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }
}