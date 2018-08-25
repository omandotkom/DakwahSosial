package mobile.omandotkom.dakwahsosial;

import android.app.Application;

import net.gotev.uploadservice.UploadService;

public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UploadService.NAMESPACE = "mobile.omandotkom.dakwahsosial";
    }
}
