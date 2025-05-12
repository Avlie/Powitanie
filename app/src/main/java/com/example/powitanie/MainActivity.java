package com.example.powitanie;

import static android.app.ProgressDialog.show;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.DialogInterface;

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
            public void onClick(View view){
                String podajImie = imie.getText().toString().trim();
                if(podajImie.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Błąd");
                    builder.setMessage("Proszę wpisać swoje imię!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }

                    });
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Potwierdzenie");
                    builder.setMessage("Cześć " + podajImie + "!Czy chcesz otrzymać powiadomienie powitalne?");
                    builder.setPositiveButton("Tak, poproszę", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            sendpowitanie();
                            dialog.dismiss();
                        }

                    });
                    builder.setNegativeButton("Nie, dziękuję", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

                }

            }
        });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        String podajImie = imie.getText().toString().trim();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Witaj!")
                .setContentText("Miło Cię widzieć, " + podajImie + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
