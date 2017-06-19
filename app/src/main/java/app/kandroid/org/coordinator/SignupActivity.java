package app.kandroid.org.coordinator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by isin-yeong on 17. 5. 8..
 */
public class SignupActivity extends BaseActivity{


    private EditText edit_ID;
    private EditText edit_PWD;
    private EditText edit_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edit_ID = (EditText)findViewById(R.id.edit_id);
        edit_PWD = (EditText)findViewById(R.id.edit_pwd);
        edit_Name = (EditText)findViewById(R.id.edit_name);
    }


    public void closeOnClick(View v){
        finish();
    }

    public void signupOnClick(View v){

        String id = edit_ID.getText().toString();
        String pwd = edit_PWD.getText().toString();
        String name = edit_Name.getText().toString();

        insertToDatabase(id,pwd,name);

    }

    private void insertToDatabase(String id,String pwd,String name){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignupActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                if(s.startsWith("s")){

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);

                    finish();
                }
                else {

                    Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_LONG).show();


                }

            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String pwd = (String)params[1];
                    String name = (String)params[2];

                    String link="http://119.205.220.130/user.php";
                    String data  = URLEncoder.encode("u_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("u_pwd", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
                    data += "&" + URLEncoder.encode("u_name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(id, pwd,name);
    }


}
