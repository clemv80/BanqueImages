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
import java.util.Arrays;
import java.util.List;

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

        return new AIDLServiceInterface.Stub(){



            public Bitmap getImage(int index) {

                String src = "http://192.168.1.64:80/image.php?face="+index;
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

            @Override
            public int[] getListImage() throws RemoteException {

                //faire le new int[] par rappor au nombre d'image du cache
                int[] intArray = new int[3];

                // ajouter avec un for les index du cache
                intArray[0] = 1;
                intArray[1] = 2;
                intArray[2] = 3;


                return intArray;
            }


        };
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
