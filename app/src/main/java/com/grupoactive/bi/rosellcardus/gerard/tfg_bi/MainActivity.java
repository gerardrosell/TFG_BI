package com.grupoactive.bi.rosellcardus.gerard.tfg_bi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    EditText IDProjecte, IDTasca, Descripcio, NoHores;
    Button Registrar;
    String idP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IDProjecte = (EditText) findViewById(R.id.ProjectID);
        IDTasca = (EditText) findViewById(R.id.TaskID);
        Descripcio = (EditText) findViewById(R.id.Description);
        NoHores = (EditText) findViewById(R.id.HoursNo);
        Registrar = (Button) findViewById(R.id.btn_registre);
        idP = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        new ConsultarUsuariExisteix().execute("http://mynet.grupoactive.es:8042/TFG/checkuserexist.php?MAC="+idP);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(IDProjecte.getText().toString(), "") || Objects.equals(IDTasca.getText().toString(), "") || Objects.equals(Descripcio.getText().toString(), "") || Objects.equals(NoHores.getText().toString(), "")){
                    Toast.makeText(getApplicationContext(), "Fields are incorrect or incomplete", Toast.LENGTH_LONG).show();
                } else {
                    new CarregarDades().execute("http://mynet.grupoactive.es:8042/TFG/Registro.php?ID="+idP+"&quanthores=" + parseInt(NoHores.getText().toString())
                            + "&descrip=" + Descripcio.getText().toString()
                            + "&IDJob=" + IDProjecte.getText().toString()
                            + "&IDTask=" + IDTasca.getText().toString());
                    //new CarregarDades().execute("http://mynet.grupoactive.es:8042/WebService1.asmx/InsertSimple?ID=bbbb&quanthores=2&descrip=ccc&IDJob=kkk&IDTask=jjj");
                    IDProjecte.setText("");
                    IDTasca.setText("");
                    Descripcio.setText("");
                    NoHores.setText("");
                }

            }
        });

    }

    private class ConsultarUsuariExisteix extends AsyncTask<String, Void, String> {
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
            Log.i("Result: ", result);
            if(result.substring(1,2).equals("f")){
                //Anar a l'activitat NewUser
                Intent intent = new Intent(MainActivity.this, NewUser.class);
                intent.putExtra("idP", idP);
                startActivity(intent);
            }
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
            Toast.makeText(getApplicationContext(), "Data saved correctly",
                    Toast.LENGTH_LONG).show();
        }
    }


    private String downloadUrl(String myurl) throws IOException {
        myurl = myurl.replace(" ", "%20");
        InputStream stream = null;
        int len = 500;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ShowList) {
            Intent intent = new Intent(this, HoursList.class);
            intent.putExtra("IDpersona", idP);
            startActivity(intent);
            return true;
        }

        if (id == R.id.ShowProd) {
            Intent intent = new Intent(this, Productivity.class);
            intent.putExtra("IDpersona", idP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
