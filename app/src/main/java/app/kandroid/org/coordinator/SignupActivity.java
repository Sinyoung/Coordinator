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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        edit_ID = (EditText)findViewById(R.id.edit_id);
        edit_PWD = (EditText)findViewById(R.id.edit_pwd);
    }


    public void closeOnClick(View v){
        finish();
    }

    public void signupOnClick(View v){

        String phone = edit_ID.getText().toString();
        String pwd = edit_PWD.getText().toString();

        insertToDatabase(phone,pwd);

    }

    private void insertToDatabase(String phone,String pwd){

        class InsertData extends AsyncTask<String, String, String> {
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

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                    String phone = (String)params[0];
                    String pwd = (String)params[1];


                    String link="http://220.230.119.159/user.php";
                    String data  = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                    data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");


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
        task.execute(phone, pwd);
    }


}
