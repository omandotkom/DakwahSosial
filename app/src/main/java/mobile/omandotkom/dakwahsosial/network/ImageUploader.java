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

    public void uploadMultipart(final Context context, String filepath, UploadStatusListener listener) {
        try {
            this.uploadStatusListener = listener;
            UploadNotificationConfig config = new UploadNotificationConfig();
            config.setClearOnActionForAllStatuses(true);
            config.setRingToneEnabled(false);
            config.getCompleted().autoClear = true;
            final String uploadId =
                    new MultipartUploadRequest(context,URLS.IMAGE_UPLOAD_URL)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .setNotificationConfig(config)
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
