package mobile.omandotkom.dakwahsosial;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

//import android.util.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class compose_post extends Fragment {
    static final int REQUEST_TAKE_PHOTO = 1;
    private final String TAG = "COMPOSE_POST";
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 10;

    String mCurrentPhotoPath;
    private ImageView mImageView;
    private ImagePicker imagePicker;
    public compose_post() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //   TextView textView = new TextView(getActivity());
        // textView.setText(R.string.hello_blank_fragment);

        View view = inflater.inflate(R.layout.ds_fragment_compose_post, container, false);
        mImageView = view.findViewById(R.id.imageView);
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent();
                 requestPermission();
            }
        });
        imagePicker = new ImagePicker(getActivity(),
                this, new OnImagePickedListener() {
            @Override
            public void onImagePicked(Uri imageUri) {
                Log.d(TAG,"as");
            }
        }
        );

        return view;
    }
/*
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "mobile.omandotkom.dakwahsosial.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
       /* if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
          mImageView.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCurrentPhotoPath),120,120));

        }*/
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
       /* switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //dispatchTakePictureIntent();
                    imagePicker.choosePicture(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //dispatchTakePictureIntent();
                Toast.makeText(getContext(),"Ijin akses media penyimpanan diperlukan untuk menyimpan foto.",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }*/

    }
}
