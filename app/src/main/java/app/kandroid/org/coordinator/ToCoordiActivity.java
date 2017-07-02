package app.kandroid.org.coordinator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isin-yeong on 17. 5. 25..
 */
public class ToCoordiActivity extends BaseActivity {


    public  static final int RequestPermissionCode  = 1 ;

    private boolean isCanceled;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    ImageView img;
    TextView textView;
    ProgressDialog progressDialog ;
    Bitmap bitmap;
    boolean check = true;

    String GetImageNameFromEditText = "temp";

    String ImageNameFieldOnServer = "phone" ;

    String ImagePathFieldOnServer = "image_path" ;

    String ImageUploadPathOnSever ="http://220.230.119.159/image_to_server.php" ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        Intent intent = getIntent();

        String photoPath = intent.getStringExtra("strParamName");

        ImagePathFieldOnServer = photoPath;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bitmap = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);


        img = (ImageView) findViewById(R.id.curPicture);
        img.setImageBitmap(adjustedBitmap);


        textView = (TextView)findViewById(R.id.toCoordi);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageUploadToServerFunction();

                /*

                //  Specify the progress dialog is not canceled
                isCanceled = false;
                // Initialize a new instance of progress dialog
                final ProgressDialog pd = new ProgressDialog(ToCoordiActivity.this);

                // Set progress dialog style horizontal
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                // Set the progress dialog title and message
                pd.setTitle("CoordiNator");
                pd.setMessage("코디하는중.........");

                // Set the progress dialog background color
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

                pd.setIndeterminate(false);
                /*
                    Set the progress dialog non cancelable
                    It will disallow user's to cancel progress dialog by clicking outside of dialog
                    But, user's can cancel the progress dialog by cancel button
                 */

                /*
                pd.setCancelable(false);

                pd.setMax(100);
                // Put a cancel button in progress dialog
                pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    // Set a click listener for progress dialog cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss the progress dialog
                        pd.dismiss();
                        // Tell the system about cancellation
                        isCanceled = true;
                    }
                });

                // Finally, show the progress dialog
                pd.show();

                // Set the progress status zero on each button click
                progressStatus = 0;
                // Start the lengthy operation in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressStatus < pd.getMax()) {
                            // If user's click the cancel button from progress dialog
                            if (isCanceled) {
                                // Stop the operation/loop
                                break;
                            }
                            // Update the progress status
                            progressStatus += 1;

                            // Try to sleep the thread for 200 milliseconds
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Update the progress status
                                    pd.setProgress(progressStatus);

                                    // If task execution completed
                                    if (progressStatus == pd.getMax()) {
                                        // Dismiss/hide the progress dialog
                                        pd.dismiss();

                                    }
                                }
                            });
                        }
                    }
                }).start(); // Start the operation

                */

            }
        });



    }



    // Upload captured image online on server function.
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(ToCoordiActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(ToCoordiActivity.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                img.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                StringBuilder stringBuilder = new StringBuilder();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);

                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);

                try {

                    URL url =  new URL(ImageUploadPathOnSever);
                    String data  = null;

                    data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(GetImageNameFromEditText, "UTF-8");
                    data += "&" + URLEncoder.encode("image_path", "UTF-8") + "=" + URLEncoder.encode(ConvertImage, "UTF-8");


                    HttpURLConnection httpURLConnectionObject ;
                    OutputStream OutPutStream;
                    BufferedWriter bufferedWriterObject ;
                    BufferedReader bufferedReaderObject ;
                    int RC ;

                    httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                    httpURLConnectionObject.setReadTimeout(19000);

                    httpURLConnectionObject.setConnectTimeout(19000);

                    httpURLConnectionObject.setRequestMethod("POST");

                    httpURLConnectionObject.setDoInput(true);

                    httpURLConnectionObject.setDoOutput(true);

                    OutPutStream = httpURLConnectionObject.getOutputStream();

                    bufferedWriterObject = new BufferedWriter(

                            new OutputStreamWriter(OutPutStream, "UTF-8"));

                    bufferedWriterObject.write(data);

                    bufferedWriterObject.flush();

                    bufferedWriterObject.close();

                    OutPutStream.close();

                    RC = httpURLConnectionObject.getResponseCode();

                    if (RC == HttpsURLConnection.HTTP_OK) {

                        bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                        stringBuilder = new StringBuilder();

                        String RC2;

                        while ((RC2 = bufferedReaderObject.readLine()) != null){

                            stringBuilder.append(RC2);
                            break;
                        }
                    }

                    return stringBuilder.toString();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ToCoordiActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(ToCoordiActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }




}
