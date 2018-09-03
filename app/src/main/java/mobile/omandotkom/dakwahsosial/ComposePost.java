package mobile.omandotkom.dakwahsosial;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.IOException;

import mobile.omandotkom.dakwahsosial.network.ImageUploader;
import mobile.omandotkom.dakwahsosial.network.RequestMaker;
import mobile.omandotkom.dakwahsosial.data.ImageMedia;
import mobile.omandotkom.dakwahsosial.data.Article;

//import android.util.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposePost extends Fragment {


    static final int REQUEST_TAKE_PHOTO = 1;
    private final String TAG = "COMPOSE_POST";
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 10;
   // private String mCurrentImagePath;
    private ImageView mImageView;
    private ImagePicker imagePicker;
    private ImageMedia imageMedia = null;
    private MaterialButton nextButton, uploadPhotoButton;
    private ImageUploader imageUploader = new ImageUploader();
    private EditText editContent, editTitle;
    private Article article;

    public ComposePost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        View view = inflater.inflate(R.layout.ds_fragment_compose_post, container, false);
        editContent = view.findViewById(R.id.editContent);
        mImageView = view.findViewById(R.id.imageView);
        nextButton = view.findViewById(R.id.next_button);
        editTitle = view.findViewById(R.id.titlePost);
        // imageUploader.registerUploadStatusListener(uploadStatusListener);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTitle.getText().toString().isEmpty() || editTitle.getText().toString().length()==0){
                   Toast.makeText(getContext(),R.string.title_warning,Toast.LENGTH_LONG).show();
                }else if(editContent.getText().toString().isEmpty() || editContent.getText().toString().length()==0) {
                    Toast.makeText(getContext(),R.string.content_warning,Toast.LENGTH_LONG).show();
                }else{
                    compose();
                }
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
                    String mCurrentImagePath;
                    mCurrentImagePath = getRealPathFromURI(getContext(), imageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                    mImageView.setImageBitmap(bitmap);
                   imageMedia = new ImageMedia();
                    imageMedia.setLocalPath(mCurrentImagePath);

/*                    imageUploader.uploadMultipart(getContext(), mCurrentImagePath);
                    imageUploader.registerUploadStatusListener(new UploadStatusListener() {
                        @Override
                        public void onImageUploadComplete(String response) {

                            imageMedia.setResponse(response);
                            if (!imageMedia.isErrorResponse()) {
                                Log.d(TAG, "URLNYA adalah : " + imageMedia.getUrl());
                            } else {
                                Log.e(TAG, "an error occured");
                            }

                        }
                    });*/
                    //  new ImageUploader().uploadMultipart(getContext(),mCurrentImagePath);
                } catch (IOException ioe) {
                    Log.e(TAG, ioe.getMessage());
                }
            }
        }
        );
        editTitle.setText("INI HANYA CONTOH JUDUL");
        editContent.setText("Berikut adalah contoh konten \n Begitu juga yang lain ya");
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);

    }

    private void compose() {
        Article article = new Article();
        article.setTitle(editTitle.getText().toString());
        article.setContent(editContent.getText().toString());
        article.setImageMedia(imageMedia);
        //check if imageMedia null or not

        if (this.imageMedia != null) {
            article.setImageMedia(imageMedia);
        }

        RequestMaker requestMaker = new RequestMaker(article,getContext());
        requestMaker.upload();
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
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
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
