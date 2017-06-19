package app.kandroid.org.coordinator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by isin-yeong on 17. 5. 20..
 */
public class HomeActivity extends BaseActivity {


    private static final int MY_REQUEST_CODE=0;
    private static final int TAKE_CAMERA = 1; // 카메라 리턴 코드값 설정
    private static final int TAKE_GALLERY = 2; // 앨범선택에 대한 리턴 코드값 설정


    private ImageView Imageview;
    private  Uri mImageCapturUri;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView Imageview = (ImageView) findViewById(R.id.imgview);
        Button btncamera = (Button) findViewById(R.id.btncamera);
        Button btnlist = (Button) findViewById(R.id.btnlist);

        btncamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //doTakePhotoAction();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,TAKE_CAMERA);

            }
        });
        btnlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, TAKE_GALLERY);
            }
        });
    }


    private void doTakePhotoAction(){

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //String url = "tmp_"+String.valueOf(System.currentTimeMillis()) + ".jpg";


       // FileProvider.getUriForFile(HomeActivity.this,"app.kandroid.org.coordinator.provider",url);

        //photoURI = FileProvider.getUriForFile(HomeActivity.this, BuildConfig.APPLICATION_ID+".provider", createImageFile());
        //mImageCapturUri =  Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCapturUri);
        startActivityForResult(intent, TAKE_CAMERA);
    }

    private void doTakeAlbumAction(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, TAKE_GALLERY);

    }


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;
        if (sizes==null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h; // Find size//
         for (Camera.Size size : sizes) {
             double ratio = (double) size.width / size.height;
             if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
             if (Math.abs(size.height - targetHeight) < minDiff) {
                 optimalSize = size;
                 minDiff = Math.abs(size.height - targetHeight);
             }
         } if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_CAMERA) {
                if (data != null) {
                    Log.e("Test", "result = " + data);
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    if (thumbnail != null) {
                        ImageView Imageview = (ImageView) findViewById(R.id.imgview);
                        Imageview.setImageBitmap(thumbnail);
                    }
                }

            } else if (requestCode == TAKE_GALLERY) {
                if (data != null) {
                    Log.e("Test", "result = " + data);

                    Uri thumbnail = data.getData();
                    if (thumbnail != null) {
                        ImageView Imageview = (ImageView) findViewById(R.id.imgview);
                        Imageview.setImageURI(thumbnail);
                    }
                }
            }

        }

    }
}