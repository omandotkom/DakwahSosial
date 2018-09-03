package mobile.omandotkom.dakwahsosial.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageMedia {
    private final String TAG = "IMAGERESPONSE";
    private String file, url, type;
    private String localPath;
    private boolean disableCheck;
    private boolean isErrorThrown;
    private String response;
    private String error;

    public boolean isDisableCheck() {
        return disableCheck;
    }

    public void setDisableCheck(boolean disableCheck) {
        this.disableCheck = disableCheck;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isErrorThrown() {
        return isErrorThrown;
    }

    public void setErrorThrown(boolean errorThrown) {
        isErrorThrown = errorThrown;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        parseJSON();
    }

    private void parseJSON(){
        if (!response.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject(response);
                this.file = jsonObject.getString("file");
                this.url = jsonObject.getString("url");
                this.type = jsonObject.getString("type");
            }catch(JSONException jsoe){
                //berarti ada error
                isErrorThrown = true;
                Log.e(TAG,jsoe.getMessage());
            setError(jsoe.getMessage());
            }
        }
    }

    public boolean isErrorResponse() {
        return this.isErrorThrown;
    }

}
