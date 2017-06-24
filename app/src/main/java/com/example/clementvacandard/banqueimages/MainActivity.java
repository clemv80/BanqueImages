package com.example.clementvacandard.banqueimages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button rechercher_btn;
    private TextView log_tv;
    private EditText numeroImage_tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        String numero = numeroImage_tf.getText();
        log_tv.setText("Recherche de l'image "+numero);

        String src = "http://192.168.1.15:8888/image.php?face="+numero;



        afficherImage();
    }

    public void afficherImage(){

        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.setImageResource(R.drawable.grenouille);

    }


}