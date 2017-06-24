package com.example.clementvacandard.banqueimages;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    AIDLServiceInterface service;
    AIDLServiceConnection connection;

    private Button rechercher_btn;
    private TextView log_tv;
    private EditText numeroImage_tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

       rechercher_btn = (Button) findViewById(R.id.rechercher_btn);
        log_tv = (TextView) findViewById(R.id.log_tv);
        numeroImage_tf = (EditText) findViewById(R.id.numeroImage_tf);

        rechercher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechercherImage(v);
            }
        });
    }

    class AIDLServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = AIDLServiceInterface.Stub.asInterface((IBinder) boundService);
            Log.i(TAG, "onServiceConnected(): Connected");
            Toast.makeText(MainActivity.this, "AIDLExample Service connected", Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.i(TAG, "onServiceDisconnected(): Disconnected");
            Toast.makeText(MainActivity.this, "AIDLExample Service Connected", Toast.LENGTH_LONG).show();
        }
    }

    public void rechercherImage(View v) {
        String numero = numeroImage_tf.getText().toString();
        log_tv.setText("Recherche de l'image "+numero);

        Bitmap bitmap = null;
        try {
            bitmap = service.getImage(3);
        } catch (RemoteException e) {
            Log.i(TAG, "Data fetch failed with: " + e);
            e.printStackTrace();
        }

        afficherImage(bitmap);
    }

    public void afficherImage(Bitmap bitmap){
        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.setImageBitmap(bitmap);
    }



    private void initService() {
        Log.i(TAG, "initService()" );
        connection = new AIDLServiceConnection();
        Intent i = new Intent();
        i.setClassName("com.example.clementvacandard.banqueimages", com.example.clementvacandard.banqueimages.ImageService.class.getName());
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "initService() bound value: " + ret);
    }


    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }



}