package mobile.omandotkom.dakwahsosial.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Article {
    private final String STATUS = "publish";
    private final String EXCERPT = "view";
    private final String TAG = "POSTRESPONSE";
    private String title, content;
    private ImageMedia imageMedia = null;
    private String html;
    private String JSONResponse;
    private String link;

    public boolean isErrorThrown() {
        return isErrorThrown;
    }

    public void setErrorThrown(boolean errorThrown) {
        isErrorThrown = errorThrown;
    }

    private boolean isErrorThrown;
    public String getSTATUS() {
        return STATUS;
    }

    public String getEXCERPT() {
        return EXCERPT;
    }

    public ImageMedia getImageMedia() {
        if (imageMedia != null) return imageMedia;
        else return null;
    }

    public void setImageMedia(ImageMedia imageMedia) {
        this.imageMedia = imageMedia;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setJSONResponse(String JSONResponse) {
        this.JSONResponse = JSONResponse;
        parseJSON();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void parseJSON() {
        if (!JSONResponse.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(JSONResponse);
                setLink(jsonObject.getString("link"));
                setTitle(jsonObject.getJSONObject("title").getString("rendered"));
            } catch (JSONException jsoe) {
                //berarti ada error
                setErrorThrown(true);
                Log.e(TAG, jsoe.getMessage());
            }
        }
    }
}
