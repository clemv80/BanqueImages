package com.example.clementvacandard.banqueimages;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    AIDLServiceInterface service;
    AIDLServiceConnection connection;
    private LruCache<String, Bitmap> memoryCache;

    private Button rechercher_btn;
    private Button vider_btn;
    private Spinner index_lst;
    private CheckBox dispo_cb;

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

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

       rechercher_btn = (Button) findViewById(R.id.rechercher_btn);
        vider_btn = (Button) findViewById(R.id.vider_btn);
        index_lst = (Spinner) findViewById(R.id.index_lst);
        dispo_cb = (CheckBox) findViewById(R.id.dispo_cb);

        setIndexLst();

        rechercher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechercherImage(v);
            }
        });

        vider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryCache.evictAll();
                setIndexLst();
            }
        });

        dispo_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setIndexLst();
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

        String index = (String)index_lst.getSelectedItem();
        Bitmap bitmap = getBitmapFromMemCache(index);

        if(bitmap==null) {
            try {
                bitmap = service.getImage(Integer.parseInt(index));
                addBitmapToMemoryCache(index, bitmap);
            } catch (RemoteException e) {
                Log.i(TAG, "Data fetch failed with: " + e);
                e.printStackTrace();
            }
        }

        afficherImage(bitmap);
    }

    public void afficherImage(Bitmap bitmap){
        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.setImageBitmap(bitmap);
    }

    public void setIndexLst(){
        ArrayList<String> data;
        if(dispo_cb.isChecked()){
            data = getIndexLstFromMemCache();
        }else {
            data = new ArrayList<String>();
            for (int i = 0; i < 50; i++) {
                data.add(Integer.toString(i));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        index_lst.setAdapter(adapter);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    public ArrayList<String> getIndexLstFromMemCache(){
        ArrayList<String> cache_index_lst = new ArrayList<String>();
        for(int i = 0; i<50; i++){
            String key = Integer.toString(i);
            if (getBitmapFromMemCache(key) != null) {
                cache_index_lst.add(key);
            }
        }
        return cache_index_lst;
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