package app.kandroid.org.coordinator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends BaseActivity {



    private String myJSON;
    private JSONArray User;

    private EditText edit_id;
    private EditText edit_pwd;

    private String input_id;
    private String input_pwd;

    private boolean isExist = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id = (EditText)findViewById(R.id.login_id);
        edit_pwd = (EditText)findViewById(R.id.login_pwd);
    }



    public void LoginOnClick(View v){


        input_id = edit_id.getText().toString();
        input_pwd = edit_pwd.getText().toString();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        //getData("http://119.205.220.130/getUserData.php");



    }

    public void signupOnClick(View v){

        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);


    }




    public void getData(final String url) {

        class GetDataJSON extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                loading= ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {

                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {

                loading.dismiss();
                myJSON = result;

                CheckisExist();



            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }



    protected void CheckisExist() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            User = jsonObj.getJSONArray("result");


            for (int i = 0; i < User.length(); i++) {
                JSONObject c = User.getJSONObject(i);

                String id = c.getString("u_id");
                String pwd = c.getString("u_pwd");




                if(input_id.equals(id) && input_pwd.equals(pwd)){
                    isExist = true;
                    break;
                }

            }

            if(isExist == false){
                Toast.makeText(getApplicationContext(), "회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isExist == true){

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
