package jp.techacademy.masaki.ohmura.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


import static jp.techacademy.masaki.ohmura.autoslideshowapp.R.id.button1;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Timer mTimer;
    double mTimerSec = 0.0;
    Handler mHandler = new Handler();
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Cursor cursor1 = null;
    int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }


        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                } else {
                    Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                }
                break;
            default:
                break;
        }
    }

    public Cursor getCursorInfo() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );
        return cursor;
    }

    private void getContentsInfo() {
        Cursor cursor2 = getCursorInfo();
        cursor2.moveToFirst();
        int fieldIndex = cursor2.getColumnIndex(MediaStore.Images.Media._ID);
        Long id = cursor2.getLong(fieldIndex);
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
        imageVIew.setImageURI(imageUri);
    }

    @Override
    public void onClick(View v) {
        if (cursor1 == null) {
            ContentResolver resolver = getContentResolver();
            cursor1 = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            );
        }

        if (v.getId() == button1) {
            if (cursor1.moveToNext()) {
                int fieldIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor1.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            } else {
                cursor1.moveToFirst();
                int fieldIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor1.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }

        } else if (v.getId() == R.id.button2) {
            if (cursor1.moveToPrevious()) {
                int fieldIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor1.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            } else {
                cursor1.moveToLast();
                int fieldIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor1.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }
        } else if (v.getId() == R.id.button3) {
            mCount++;
            if (mCount % 2 != 0) {
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mTimerSec += 0.1;

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                cursor1.moveToNext();
                                int fieldIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
                                Long id = cursor1.getLong(fieldIndex);
                                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                imageVIew.setImageURI(imageUri);

                                Button button1 = (Button) findViewById(R.id.button1);
                                button1.setEnabled(false);
                                Button button2 = (Button) findViewById(R.id.button2);
                                button2.setEnabled(false);
                                Button button3 = (Button) findViewById(R.id.button3);
                                button3.setText("停止");
                            }
                        });
                    }
                }, 2000, 2000);
            } else if (mCount % 2 == 0) {
                mTimer.cancel();
                mTimer = null;

                Button button1 = (Button) findViewById(R.id.button1);
                button1.setEnabled(true);
                Button button2 = (Button) findViewById(R.id.button2);
                button2.setEnabled(true);
                Button button3 = (Button) findViewById(R.id.button3);
                button3.setText("再生");
            }
        }
    }
}
