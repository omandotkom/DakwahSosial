package mobile.omandotkom.dakwahsosial.pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageResponse {
    private String file, url, type;
    private String error;
    private String response;
    private final String TAG ="IMAGERESPONSE";
    private boolean isErrorThrown;
    public void setResponse(String response){
        this.response= response;
        parseJSON();
    }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
            }
        }
    }

    public boolean isErrorResponse(){
        return this.isErrorThrown;
    }
}
