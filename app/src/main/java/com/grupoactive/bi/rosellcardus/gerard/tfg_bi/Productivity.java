package com.grupoactive.bi.rosellcardus.gerard.tfg_bi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Productivity extends AppCompatActivity {

    String IDpersona;
    String[] entrades;
    TextView Prod, ProdProp; //todelete
    ProgressBar Prod_bar, ProdApp_bar, ProdProp_bar, ProdPropApp_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity);
        //to delete
        Prod = (TextView) findViewById(R.id.Percentatges);
        ProdProp = (TextView) findViewById(R.id.PercentatgesProp);
        //to delete

        Prod_bar = (ProgressBar) findViewById(R.id.Prod_bar);
        ProdApp_bar = (ProgressBar) findViewById(R.id.ProdApp_bar);
        ProdProp_bar = (ProgressBar) findViewById(R.id.ProdProp_bar);
        ProdPropApp_bar = (ProgressBar) findViewById(R.id.ProdPropApp_bar);

        Prod_bar.setMax(100);
        ProdApp_bar.setMax(100);
        ProdProp_bar.setMax(100);
        ProdPropApp_bar.setMax(100);

        getExtras();

        new ConsultarDades().execute("http://mynet.grupoactive.es:8042/TFG/consultaProd.php?DataAvui=03/06/2017&DataInici=01/01/2017&DataFinal=28/02/2017&IDEmple="+IDpersona);
    }

    private class ConsultarDades extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "URL incorrecta";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            entrades=result.split(",");

            int i = entrades[0].indexOf(":");
            //Prod.setText(entrades[0].substring(i+1)+"%");
            if(Integer.parseInt(entrades[0].substring(i+1))>100) {Prod_bar.setProgress(100);}
            else {Prod_bar.setProgress(Integer.parseInt(entrades[0].substring(i+1)));}

            int j=entrades[1].indexOf(":");
            //ProdApp.setText(entrades[1].substring(i+1)+"%");
            if(Integer.parseInt(entrades[1].substring(j+1))>100) {ProdApp_bar.setSecondaryProgress(100);}
            else {ProdApp_bar.setSecondaryProgress(Integer.parseInt(entrades[1].substring(j+1)));}

            int k=entrades[2].indexOf(":");
            //ProdProp.setText(entrades[2].substring(i+1)+"%");
            if(Integer.parseInt(entrades[2].substring(k+1))>100) {ProdProp_bar.setProgress(100);}
            else {ProdProp_bar.setProgress(Integer.parseInt(entrades[2].substring(k+1)));}


            int l=entrades[3].indexOf(":");
            //ProdPropApp.setText(entrades[3].substring(i+1,entrades[3].indexOf("}"))+"%");
            if(Integer.parseInt(entrades[3].substring(l+1,entrades[3].indexOf("}")))>100) {ProdPropApp_bar.setSecondaryProgress(100);}
            else {ProdPropApp_bar.setSecondaryProgress(Integer.parseInt(entrades[3].substring(l+1,entrades[3].indexOf("}"))));}

            Prod.setText("Registrat "+entrades[0].substring(i+1)+"% - Registrat + App "+entrades[1].substring(j+1)+"%");
            ProdProp.setText("Registrat "+entrades[2].substring(k+1)+"% - Registrat + App "+entrades[3].substring(l+1,entrades[3].indexOf("}"))+"%");

        }
    }

    public class CarregarDades extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return downloadUrl(strings[0]);
            } catch (IOException e) {
                return "URL incorrecta";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Data deleted correctly",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream stream = null;
        int len = 1000;
        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d("reposta", "La resposta es: " + responseCode);
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            //Convertir el InputString a String
            String ContentAsString = readIt(stream, len);
            return ContentAsString;

        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void getExtras() {
        IDpersona = getIntent().getExtras().getString("IDpersona");
    }
}
