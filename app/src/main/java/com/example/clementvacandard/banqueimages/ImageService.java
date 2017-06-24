package com.example.clementvacandard.banqueimages;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageService extends Service {

    public ImageService() {
    }

    private static final String TAG = "ImageService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new AIDLServiceInterface.Stub() {
            /**
             * In the AIDL file we just add the declaration of the function
             * here is the real implementation of the add() function below
             */
            public int add(int ValueFirst, int valueSecond) throws RemoteException {
                Log.i(TAG, String.format("AddService.add(%d, %d)", ValueFirst, valueSecond));
                return (ValueFirst + valueSecond);
            }

            public Bitmap getImage(int index) {


                Bitmap Image = Bitmap.createBitmap(100, 10, Bitmap.Config.RGB_565);


                String src = "http://192.168.1.15:8888/image.php?face="+index;
                System.out.println(src);
                //String src = "http://www.androidbegin.com/wp-content/uploads/2013/07/HD-Logo.gif";
                Bitmap bitmap = null;

                try{
                    java.net.URL url = new java.net.URL(src);
                    InputStream input = (InputStream) url.getContent();
                    bitmap = BitmapFactory.decodeStream(input);
                }catch (IOException e) {

                    System.out.println(e.getMessage());
                }

                return bitmap;
            }

        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
