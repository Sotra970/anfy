package anfy.com.anfy.Activity.Base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import anfy.com.anfy.R;
import anfy.com.anfy.Service.Injector;
import anfy.com.anfy.Service.ProgressRequestBody;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UplodaImagesActivity extends BaseActivity {


    final public static int MY_PERMISSIONS_REQUEST_STORAGE = 262;
    final  public static int MY_PERMISSIONS_REQUEST_CAMERA  = 263;
    public static final int REQUEST_IMAGE_CAPTURE= 264 ;

    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<File> photos_result_uri_array  = new ArrayList<>();
    public  static   int MAX_IMAGE_COUNT = 5 ;
    onUploadResponse onUploadResponse ;
    public interface  onUploadResponse{
        void onSuccess(ArrayList<String> imgs_urls);
        void onFailure();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  void pick_image_permission(int count , onUploadResponse onUploadResponse  ) {
        this.onUploadResponse = onUploadResponse ;
        photoPaths = new ArrayList<>() ;
        current = -1 ;
        MAX_IMAGE_COUNT = count ;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
        }else {
            pick_img(MAX_IMAGE_COUNT);
        }
    }



    public  void capture_img_permission() {
        capture_img();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode ,permissions , grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pick_img(MAX_IMAGE_COUNT);
                } else {
                    Toast.makeText(this," Storage permission id needed  to get into your images ", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capture_img_permission();
                } else {
                    Toast.makeText(this," Camera permission id needed  to get into your images ", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }





    private void pick_img(int count){
        photoPaths = new ArrayList<>() ;
        photos_result_uri_array = new ArrayList<>() ;
        FilePickerBuilder
                .getInstance()
                .setMaxCount(count)
                .setSelectedFiles(photoPaths)
//                .setActivityTheme(R.style.file_picker_theme)
                .pickPhoto(this);
    }


    String photoURIPath = "" ;
    private void capture_img(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            try {
                File file = createImageFile() ;
                photoURIPath = file.getPath();
                Uri photoURI =
                        FileProvider.getUriForFile(
                                getApplicationContext(), getApplicationContext().getPackageName() + ".provider"
                                , file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /// select from gallery section
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC && resultCode == RESULT_OK && data!=null) {
            super.onActivityResult(requestCode , resultCode , data);
        }
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == RESULT_OK && data!=null) {
            photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            add_to_u_crop();
        }
        else if (requestCode == UCrop.REQUEST_CROP) {

            if (resultCode == Activity.RESULT_OK) {
                handleCropResult(data);
            }
            else if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }
        }else  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photoPaths = new ArrayList<>();
            photoPaths.add(photoURIPath) ;
            add_to_u_crop();
        }

    }
    int current = -1 ;
    private void add_to_u_crop(){
        if (photoPaths.isEmpty())
            return;
        current++ ;

        if (current >= photoPaths.size()){
            // start upload
            current=-1;
            upload_Files();
            return;
        }
        startUcrop(photoPaths.get(current));

    }
    int current_body = 1   ;
    private void upload_Files() {

        current_body = 1 ;

        showProgressDialog(getString(R.string.dialog_msg_loading));


        ProgressRequestBody.UploadCallbacks  UploadCallbacks = new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                Log.e("OkHttp_upload_exp",percentage +"") ;
                updateProgressDialogMessage(percentage+" %");

            }

            @Override
            public void onError() {
                Log.e("OkHttp_upload_exp","error") ;

            }

            @Override
            public void onFinish() {
                Log.e("OkHttp_upload_exp","finish") ;
            }
        };

        String image_key = MAX_IMAGE_COUNT ==1 ? "image[]" : "image[]" ;
        ArrayList< MultipartBody.Part> multipartBodies = new ArrayList<>();
        for (File child : photos_result_uri_array){
            ProgressRequestBody fileBody = new ProgressRequestBody(child, UploadCallbacks , current_body , photos_result_uri_array.size());
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(image_key, child.getName(), fileBody);
            multipartBodies.add(body) ;
            current_body++ ;
        }
        Call<ArrayList<String>> call ;


       call = Injector.UploadApi().upload(
                multipartBodies
        );
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call,
                                   Response<ArrayList<String>> response) {

                if (response.isSuccessful() && response.body() !=null && !response.body().isEmpty()){
                    Log.e("OkHttp_upload_exp", "success");
                    onUploadResponse.onSuccess(response.body());

                }else {
                  onUploadResponse.onFailure();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.e("OkHttp_upload_exp"," error"+ t.getMessage()+"  " + t.toString());
                hideProgressDialog();
                onUploadResponse.onFailure();
               /* showLoading(LoadingDialogActivity.LoadingCases.fail, new LoadingActionClick() {
                    @Override
                    public void OnClick() {
                        upload_Files();
                    }
                });
*/
            }
        });

    }



    private  void startUcrop(final String path){
        Log.e("path" , path) ;
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(90);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.SCALE , UCropActivity.NONE , UCropActivity.ALL);
        options.setFreeStyleCropEnabled(false);
        options.setShowCropFrame(true);
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setToolbarTitle(getString(R.string.edit_img));
        options.setToolbarWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        options.withMaxResultSize(720, 720);


        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(create_jpg_uri()))
                .withOptions(options)
                .useSourceImageAspectRatio()
                .withMaxResultSize(420,420)
                .start(this , UCrop.REQUEST_CROP);
    }

    String TAG = "profile_img_upload";
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(getApplicationContext(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            photos_result_uri_array.add(new File(resultUri.getPath())) ;
            add_to_u_crop();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }


    public File create_jpg_uri()  {
//        File outputDir = Environment.getExternalStorageDirectory(); // context being the Activity pointer
        File outputDir = this.getCacheDir(); // context being the Activity pointer
        File outputFile = null;
        try {
            outputFile = File.createTempFile(currentDateFormat(),".jpg" , outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }



    public ArrayList<File> getPhotos_result_uri_array() {
        return photos_result_uri_array;
    }

    private ProgressDialog progressDialog;
    protected void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    void updateProgressDialogMessage(String message){
        progressDialog.setMessage(message);
    }




}
