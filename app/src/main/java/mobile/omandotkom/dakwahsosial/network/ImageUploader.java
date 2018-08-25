package mobile.omandotkom.dakwahsosial.network;

import android.content.Context;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import mobile.omandotkom.dakwahsosial.URLS;

public class ImageUploader{
    private UploadStatusListener uploadStatusListener;

    private final String TAG = "IMAGEUPLOADER";
    public void registerUploadStatusListener(UploadStatusListener listener){
        this.uploadStatusListener = listener;
    }
    public void uploadMultipart(final Context context, String filepath) {
        try {
            final String uploadId =
                    new MultipartUploadRequest(context,URLS.SERVER_URL + "/ImageUploader.php")
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .setNotificationConfig(new UploadNotificationConfig())
                            .addFileToUpload(filepath,"file")
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {

                                }

                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                                }

                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Log.d(TAG, serverResponse.getBodyAsString());
                                    uploadStatusListener.onImageUploadComplete(serverResponse.getBodyAsString());
                                 }

                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {

                                }
                            })
                            .startUpload();

        } catch (Exception exc) {
            Log.e(TAG, exc.getMessage(), exc);
        }
    }

}
