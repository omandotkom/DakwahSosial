package mobile.omandotkom.dakwahsosial;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import mobile.omandotkom.dakwahsosial.data.Article;
import mobile.omandotkom.dakwahsosial.data.ImageMedia;
import mobile.omandotkom.dakwahsosial.network.ImageUploader;
import mobile.omandotkom.dakwahsosial.network.RequestMaker;
import mobile.omandotkom.dakwahsosial.network.UploadStatusListener;

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static android.app.Activity.RESULT_OK;

//import android.util.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposePost extends Fragment {


    private final String TAG = "COMPOSE_POST";
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 10;

    private final int READ_EXTERNAL_STORAGE_PERMISSIONCODE = 1012;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    // private String mCurrentImagePath;
    private ImageView mImageView;
    private ImagePicker imagePicker;
    private ImageMedia imageMedia = null;
    private ImageMedia document = null;
    private MaterialButton nextButton, uploadPhotoButton;
    private EditText editContent, editTitle;
    //private String documentPath=null;
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
                if (editTitle.getText().toString().isEmpty() || editTitle.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), R.string.title_warning, Toast.LENGTH_LONG).show();
                } else if (editContent.getText().toString().isEmpty() || editContent.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), R.string.content_warning, Toast.LENGTH_LONG).show();
                } else {
                    compose();
                }
            }
        });
        uploadPhotoButton = view.findViewById(R.id.browseImage);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // requestPermission();
                String fotoString;
                String fileString;
                if (imageMedia!=null){
                    fotoString = "Hapus Foto";
                    }else{
                    fotoString = "Foto";
                }
                if (document!=null){
                    fileString = "Batalkan File";

                }else{
                    fileString = "File";
                }

                CharSequence[] items = {fotoString, fileString};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Pilih jenis file")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    //berarti pilih foto
                                    //requestPermission();
                                if (fotoString.equals("Hapus Foto")){
                                    //berarti hapus foto
                                        imageMedia = null;
                                        mImageView.setImageResource(android.R.color.transparent);
                                    }
                                    else{
                                    checkPermissionsAndOpenFilePicker(1);
                                    }
                                 }else {
                                    if (fileString.equals("Batalkan File")){
                                        document = null;
                                    }else{
                                    checkPermissionsAndOpenFilePicker(2);}
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                        public void onUploadComplete(String response) {

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
        Log.d(TAG, "RESULT CODE : " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_PICKER_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

                    if (path != null) {
                        document = new ImageMedia();
                        document.setLocalPath(path);
                        Log.d("Path: ", path);
                        Toast.makeText(getContext(), "Picked file: " + path, Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "path is null");
                    }
                }
            }
            default:
                imagePicker.handleActivityResult(resultCode, requestCode, data);
                break;
        }

    }

    private void compose() {
        article = new Article();
        article.setTitle(editTitle.getText().toString());
        article.setContent(editContent.getText().toString());
        article.setImageMedia(imageMedia);
        //check if imageMedia null or not

        if (this.imageMedia != null) {
            article.setImageMedia(imageMedia);
        }
        if (document!=null){

            article.setDocument(document);
        }

        RequestMaker requestMaker = new RequestMaker(article, getContext());
        requestMaker.setArticleUploadStatusListener(new UploadStatusListener() {
            @Override
            public void onUploadComplete(String response, int idRequest) {
                if (idRequest == 300) {
                    //lakukan pembersihan resource
                    Toast.makeText(getContext(), "Berhasil menambahkan post baru.", Toast.LENGTH_LONG).show();
                    editContent.setText("");
                    editTitle.setText("");
                    if (article != null) {
                        article.setImageMedia(null);
                        article = null;

                    }
                    imageMedia = null;
                    document = null;
                    mImageView.setImageResource(android.R.color.transparent);
                }
            }
        });
        requestMaker.upload();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FILE_PICKER_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
                break;
            }
            default: {
                imagePicker.handlePermission(requestCode, grantResults);
            }
        }


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

    private void checkPermissionsAndOpenFilePicker(int TYPE_ID) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, READ_EXTERNAL_STORAGE_PERMISSIONCODE);
            }
        } else {
            if (TYPE_ID == 1) {
                imagePicker.choosePicture(false);
            } else if (TYPE_ID == 2) {
                openFilePicker();

            }
        }
    }

    private void showError() {
        Toast.makeText(getContext(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withCloseMenu(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(false)
                .withTitle("Pilih Dokumen")
                .start();

    }
}




