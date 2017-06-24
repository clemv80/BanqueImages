package com.example.clementvacandard.banqueimages;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    private Button rechercher_btn;
    private TextView log_tv;
    private EditText numeroImage_tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void rechercherImage(View v) {
        String numero = numeroImage_tf.getText().toString();
        log_tv.setText("Recherche de l'image "+numero);

        String src = "http://192.168.1.15:8888/image.php?face="+numero;
        System.out.println(src);
        //String src = "http://www.androidbegin.com/wp-content/uploads/2013/07/HD-Logo.gif";
        Bitmap bitmap = null;

        try{
            java.net.URL url = new java.net.URL(src);
            InputStream input = (InputStream) url.getContent();
            bitmap = BitmapFactory.decodeStream(input);
        }catch (IOException e) {
            log_tv.setText(e.getMessage());
            System.out.println(e.getMessage());
        }

        afficherImage(bitmap);
    }

    public void afficherImage(Bitmap bitmap){
        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.setImageBitmap(bitmap);
    }


}