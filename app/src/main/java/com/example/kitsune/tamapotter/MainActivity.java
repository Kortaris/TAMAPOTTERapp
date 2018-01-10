package com.example.kitsune.tamapotter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView IStart;
    private ImageView Ingame;
    private ImageView Iogame;
    private ImageView ISova;
    private ImageView IEat;
    private ImageView IShower;
    private ImageView ISleep;
    private ProgressBar pbHp;
    private ProgressBar pbHangry;
    private ProgressBar pbClean;
    private ProgressBar pbEnergy;
    private TextView Tlv;
    private Chronometer Time;
    private int progress = 0;
    private int x = 3;
    private int lv = 1;
    private int max = 100;
    private int t = 0;
    private int hu=0;
    private int cl=0;
    private int en=0;
    private String mes;
    final Random random = new Random();

    Timer timer = new Timer();
    private MediaPlayer Sknop, Seda, Sdush, Shrap, Sbg;

    private static final int NOTIFY_ID = 101;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LEVEL = "lv";
    public static final String APP_PREFERENCES_PROGRESS = "progress";
    public static final String APP_PREFERENCES_HUNGRY = "hg";
    public static final String APP_PREFERENCES_CLEAN = "cl";
    public static final String APP_PREFERENCES_ENERGY = "en";
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        IStart = findViewById(R.id.start);
        Ingame = findViewById(R.id.ngame);
        Iogame = findViewById(R.id.ogame);
        ISova = findViewById(R.id.sova);
        IEat = findViewById(R.id.eat);
        IShower = findViewById(R.id.shower);
        ISleep = findViewById(R.id.sleep);
        Time = findViewById(R.id.chrono);
        pbHp = findViewById(R.id.HP);
        pbHangry = findViewById(R.id.hungry);
        pbClean = findViewById(R.id.clean);
        pbEnergy = findViewById(R.id.energy);
        Tlv = findViewById(R.id.lv);
        getSupportActionBar().hide();

        Sknop = MediaPlayer.create(this, R.raw.knop);
        Seda = MediaPlayer.create(this, R.raw.eda);
        Sdush = MediaPlayer.create(this, R.raw.dush);
        Shrap = MediaPlayer.create(this, R.raw.hrap);
        Sbg = MediaPlayer.create(this, R.raw.bg0);
        Sbg.setLooping(true);
        Sbg.start();
    }

    //начать новую
    public void onClickNew(View view) {
        pbHp.setMax(max);
        pbHp.setProgress(0);
        pbHangry.setProgress(50);
        pbClean.setProgress(50);
        pbEnergy.setProgress(50);
        PlaySound(Sknop);
        IStart.setVisibility(View.GONE);
        Ingame.setVisibility(View.GONE);
        Iogame.setVisibility(View.GONE);
        TimeToEvent();
    }

    //продолжить
    public void onClickStart(View view) {
        PlaySound(Sknop);
        IStart.setVisibility(View.GONE);
        Ingame.setVisibility(View.GONE);
        Iogame.setVisibility(View.GONE);
        pbHp.setMax(max);
        pbHp.setProgress(0);
        pbHangry.setProgress(50);
        pbClean.setProgress(50);
        pbEnergy.setProgress(50);

        if (mSettings.contains(APP_PREFERENCES_LEVEL)) {
            lv = mSettings.getInt(APP_PREFERENCES_LEVEL, 0);
            pbHangry.setProgress((mSettings.getInt(APP_PREFERENCES_HUNGRY, 0)));
            pbClean.setProgress((mSettings.getInt(APP_PREFERENCES_CLEAN, 0)));
            pbEnergy.setProgress((mSettings.getInt(APP_PREFERENCES_ENERGY, 0)));
        }
        Tlv.setText("LV " + String.valueOf(lv));
        x = 3 * lv;
        max = 100 * lv + 100 * (lv - 2);
        pbHp.setMax(max);
        progress = (mSettings.getInt(APP_PREFERENCES_PROGRESS, 0));
        pbHp.setProgress(progress);
        switch (lv){
            case 1:
            case 2:
                ISova.setImageResource(R.drawable.s);
                break;
            case 3:
            case 4:
                ISova.setImageResource(R.drawable.s1);
                break;
            case 5:
            case 6:
                ISova.setImageResource(R.drawable.s2);
                break;
            case 7:
            case 8:
                ISova.setImageResource(R.drawable.s3);
                break;
            default:
                ISova.setImageResource(R.drawable.s4);
                break;
        }
        TimeToEvent();
    }


    public void onClickSova(View view) {

        if (progress >= pbHp.getMax()) {
            lv = lv + 1;
            Tlv.setText("LV " + String.valueOf(lv));
            if (lv >= 3) {
                max = 100 * lv + 100 * (lv - 2);
            } else {
                max = 200;
            }
            pbHp.setMax(max);
            progress = 0;
            switch (lv){
                case 3:
                    ISova.setImageResource(R.drawable.s1);
                    x = 3 * lv;
                    break;
                case 5:
                    ISova.setImageResource(R.drawable.s2);
                    x = 3 * lv;
                    break;
                case 7:
                    ISova.setImageResource(R.drawable.s3);
                    x = 3 * lv;
                    break;
                case 9:
                    ISova.setImageResource(R.drawable.s4);
                    x = 3 * lv;
                    break;
            }

        } else {
            progress = progress + x;
            pbHp.setProgress(progress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sbg.stop();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_LEVEL, lv);
        editor.putInt(APP_PREFERENCES_PROGRESS, pbHp.getProgress());
        editor.putInt(APP_PREFERENCES_HUNGRY, pbHangry.getProgress());
        editor.putInt(APP_PREFERENCES_CLEAN, pbClean.getProgress());
        editor.putInt(APP_PREFERENCES_ENERGY, pbEnergy.getProgress());
        editor.apply();
        setValues();

    }

    public void TimeToEvent() {
        t = random.nextInt((4999) + 1999);
        Time.setBase(SystemClock.elapsedRealtime());
        Time.start();
        Time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - Time.getBase();
                if (elapsedMillis > t) {
                    switch (random.nextInt(3) + 1) {
                        case 1:
                            IEat.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ISleep.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            IShower.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                if (elapsedMillis > t + 1000) {
                    IEat.setVisibility(View.GONE);
                    IShower.setVisibility(View.GONE);
                    ISleep.setVisibility(View.GONE);
                    TimeToEvent();
                }
            }
        });
    }


    public void onClickEat(View view) {
            PlaySound(Seda);
            progress = progress + x * 4;
            pbHp.setProgress(progress);
            pbHangry.setProgress(pbHangry.getProgress()+10);
    }

    public void onClickShower(View view) {
            PlaySound(Sdush);
            progress = progress + x * 5;
            pbHp.setProgress(progress);
            pbClean.setProgress(pbClean.getProgress()+10);
    }

    public void onClickSleep(View view) {
            PlaySound(Shrap);
            progress = progress + x * 6;
            pbHp.setProgress(progress);
            pbEnergy.setProgress(pbEnergy.getProgress()+10);
    }

    private void setValues() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (mSettings.getInt(APP_PREFERENCES_HUNGRY, 0)>0 &
                                    mSettings.getInt(APP_PREFERENCES_CLEAN, 0)>0 &
                                    mSettings.getInt(APP_PREFERENCES_ENERGY, 0)>0) {
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_HUNGRY, mSettings.getInt(APP_PREFERENCES_HUNGRY, 0)-20);
                                editor.putInt(APP_PREFERENCES_CLEAN, mSettings.getInt(APP_PREFERENCES_CLEAN, 0)-10);
                                editor.putInt(APP_PREFERENCES_ENERGY, mSettings.getInt(APP_PREFERENCES_ENERGY, 0) - 5);
                                editor.apply();
                                mes = "Показатели уменьшились";
                                Message(mes);
                            }

                            if (mSettings.getInt(APP_PREFERENCES_HUNGRY, 0)<30 ||
                                    mSettings.getInt(APP_PREFERENCES_CLEAN, 0)<20 ||
                                    mSettings.getInt(APP_PREFERENCES_ENERGY, 0)<10) {
                                mes = "Мне очень плохо без тебя";
                                Message(mes);
                            }

                            if (mSettings.getInt(APP_PREFERENCES_HUNGRY, 0)<=0 ||
                                    mSettings.getInt(APP_PREFERENCES_CLEAN, 0)<=0 ||
                                    mSettings.getInt(APP_PREFERENCES_ENERGY, 0)<=0) {
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_LEVEL, 1);
                                editor.putInt(APP_PREFERENCES_PROGRESS, 0);
                                editor.putInt(APP_PREFERENCES_HUNGRY,50);
                                editor.putInt(APP_PREFERENCES_CLEAN, 50);
                                editor.putInt(APP_PREFERENCES_ENERGY, 50);
                                editor.apply();
                                mes = "Ваш питомец умер, придется начинать заново";
                                Message(mes);
                            }



                        }
                    });
                }
            }, 3600000, 3600000);
    }


    public void PlaySound (MediaPlayer sound){
        sound.start();
    }

    public void Message(String mes){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.bteat)
                .setContentTitle("Напоминание")
                .setContentText(mes)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.bteat))
                .setTicker("Поиграй со мной!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }
}
