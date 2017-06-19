package app.kandroid.org.coordinator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by isin-yeong on 17. 5. 25..
 */
public class ToCoordiActivity extends BaseActivity {

    private boolean isCanceled;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    ImageView img;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Intent intent = getIntent();

        String photoPath = intent.getStringExtra("strParamName");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        img = (ImageView) findViewById(R.id.curPicture);
        img.setImageBitmap(adjustedBitmap);

        textView = (TextView)findViewById(R.id.toCoordi);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                pd.setCancelable(false);

                pd.setMax(100);
                // Put a cancel button in progress dialog
                pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                    // Set a click listener for progress dialog cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int which){
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
                        while(progressStatus < pd.getMax()){
                            // If user's click the cancel button from progress dialog
                            if(isCanceled)
                            {
                                // Stop the operation/loop
                                break;
                            }
                            // Update the progress status
                            progressStatus +=1;

                            // Try to sleep the thread for 200 milliseconds
                            try{
                                Thread.sleep(200);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Update the progress status
                                    pd.setProgress(progressStatus);

                                    // If task execution completed
                                    if(progressStatus == pd.getMax()){
                                        // Dismiss/hide the progress dialog
                                        pd.dismiss();

                                    }
                                }
                            });
                        }
                    }
                }).start(); // Start the operation

            }
        });

    }

}
