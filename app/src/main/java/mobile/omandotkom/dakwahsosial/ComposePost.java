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

import mobile.omandotkom.dakwahsosial.converter.TextToHtml;
import mobile.omandotkom.dakwahsosial.network.ImageUploader;
import mobile.omandotkom.dakwahsosial.network.UploadStatusListener;
import mobile.omandotkom.dakwahsosial.pojo.ImageResponse;
import mobile.omandotkom.dakwahsosial.pojo.Post;

//import android.util.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposePost extends Fragment {


    static final int REQUEST_TAKE_PHOTO = 1;
    private final String TAG = "COMPOSE_POST";
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 10;
    private String mCurrentImagePath;
    private ImageView mImageView;
    private ImagePicker imagePicker;
    private ImageResponse imageResponse;
    private MaterialButton nextButton, uploadPhotoButton;
    private ImageUploader imageUploader = new ImageUploader();
    private EditText editContent, editTitle;

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
                //cek jika judul kosong
                if (editTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.title_warning, Toast.LENGTH_LONG).show();
                }
                if (editContent != null & editContent.getText().length() != 0) {
                    Post post = new Post();
                    if (mCurrentImagePath != null) {
                        if (mCurrentImagePath.isEmpty()) {
                            Log.d(TAG, "tida ada gambar");
                            compose();
                        } else {
                            //tidak kosong
                            Log.d(TAG, "ada gambar");
                            compose();
                        }
                    } else {
                        //mCurrentImagePath di sini null
                        Log.d(TAG, "tida ada gambar");
                        compose();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.content_warning, Toast.LENGTH_LONG).show();
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
                    mCurrentImagePath = getRealPathFromURI(getContext(), imageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                    mImageView.setImageBitmap(bitmap);

                    imageUploader.uploadMultipart(getContext(), mCurrentImagePath);
                    imageUploader.registerUploadStatusListener(new UploadStatusListener() {
                        @Override
                        public void onImageUploadComplete(String response) {
                            if (imageResponse == null) {
                                imageResponse = new ImageResponse();
                            }
                            imageResponse.setResponse(response);
                            if (!imageResponse.isErrorResponse()) {
                                Log.d(TAG, "URLNYA adalah : " + imageResponse.getUrl());
                            } else {
                                Log.e(TAG, "an error occured");
                            }

                        }
                    });
                    //  new ImageUploader().uploadMultipart(getContext(),mCurrentImagePath);
                } catch (IOException ioe) {
                    Log.e(TAG, ioe.getMessage());
                }
            }
        }
        );

        //TODO : NANTI DIHAPUS, ini dummy data
        mCurrentImagePath = "http://192.168.43.32/dakwah-wp/wordpress/wp-content/uploads/2018/08/668137b7518f9b1f48954cfaf77389c7.jpg";
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
        Post post = new Post();
        post.setTitle(editTitle.getText().toString());
        post.setContent(editContent.getText().toString());
        //TODO : nanti di hapus, ini dummy saja
        // ImageResponse imageResponse = new ImageResponse();
        //imageResponse.setUrl("http://192.168.43.32/dakwah-wp/wordpress/wp-content/uploads/2018/08/668137b7518f9b1f48954cfaf77389c7.jpg");


        Log.d(TAG, TextToHtml.getHtml(post));
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
