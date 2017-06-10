package com.grupoactive.bi.rosellcardus.gerard.tfg_bi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class HoursList extends AppCompatActivity {
    ListView EntryList;
    String IDpersona;
    ArrayList<Entry> list;
    //ArrayList<String> liststrings;
    //EntryAdapter adapter;
    ArrayAdapter<Entry> adapter;
    //ArrayAdapter<String> adapter;
    Entry entrada;
    String[] entrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours_list);

        EntryList = (ListView) findViewById(R.id.EntryList);
        list = new ArrayList<Entry>();
        //liststrings = new ArrayList<String>();

        getExtras();

        new ConsultarDades().execute("http://mynet.grupoactive.es:8042/TFG/llista.php?ID="+IDpersona);

        EntryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CarregarDades().execute("http://mynet.grupoactive.es:8042/TFG/delete.php?IDregistre="+
                String.valueOf(list.get(i).IDregistre));
                list.remove(i);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
                return true;
            }
        });

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
            entrades=result.split("\\}");
            entrades= Arrays.copyOfRange(entrades, 0, entrades.length-1);
            Log.i("nvertstr", "[" + result + "]");

            for(int i=0; i<entrades.length;i++){
                list.add(new Entry(IDpersona, entrades[i]));
                //liststrings.add((new Entry(IDpersona, entrades[i])).getIDJob());
                Log.i("Element:", entrades[i]);
            }

            adapter = new EntryAdapter(HoursList.this,list);
            Log.i("Adapter", "adapter creat");
            EntryList.setAdapter(adapter);
            Log.i("Adapter", "adapter assignat");


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
