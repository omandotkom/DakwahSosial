package mobile.omandotkom.dakwahsosial;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.IOException;

import mobile.omandotkom.dakwahsosial.network.ImageUploader;
import mobile.omandotkom.dakwahsosial.network.UploadStatusListener;
import mobile.omandotkom.dakwahsosial.pojo.ImageResponse;

//import android.util.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class compose_post extends Fragment{




    static final int REQUEST_TAKE_PHOTO = 1;
    private final String TAG = "COMPOSE_POST";
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 10;
    private String mCurrentImagePath;
    private ImageView mImageView;
    private ImagePicker imagePicker;
    private ImageResponse imageResponse;
    private MaterialButton nextButton,uploadPhotoButton;
    private ImageUploader imageUploader = new ImageUploader();
            public compose_post() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //   TextView textView = new TextView(getActivity());
        // textView.setText(R.string.hello_blank_fragment);

        View view = inflater.inflate(R.layout.ds_fragment_compose_post, container, false);
/*        mImageView = view.findViewById(R.id.imageView);
        nextButton = view.findViewById(R.id.next_button);
    //    imageUploader.registerUploadStatusListener(uploadStatusListener);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   }
        });
        uploadPhotoButton = view.findViewById(R.id.browseImage);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();

            }
        });
        imagePicker = new ImagePicker(getActivity(),
                this, new OnImagePickedListener() {
            @Override
            public void onImagePicked(final Uri imageUri) {
                try {
                    mCurrentImagePath = getRealPathFromURI(getContext(),imageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap,100,100);
                    mImageView.setImageBitmap(bitmap);

                    imageUploader.uploadMultipart(getContext(),mCurrentImagePath);
                    imageUploader.registerUploadStatusListener(new UploadStatusListener() {
                        @Override
                        public void onImageUploadComplete(String response) {
                            if (imageResponse == null) {
                                imageResponse = new ImageResponse();
                            }
                            imageResponse.setResponse(response);
                            if (!imageResponse.isErrorResponse()){
                                Log.d(TAG, "URLNYA adalah : " + imageResponse.getUrl());
                        }else{
                                Log.e(TAG,"an error occured");
                            }

                        }
                    });
                    //  new ImageUploader().uploadMultipart(getContext(),mCurrentImagePath);
                }catch (IOException ioe){
                Log.e(TAG,ioe.getMessage());
                }
            }
        }
        );

  */      return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);

    }


    //chekcs the write to device permissionn
    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            // Permission has already been granted
            Log.d(TAG, "Permission granted");
            //dispatchTakePictureIntent();
            imagePicker.choosePicture(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);


    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
