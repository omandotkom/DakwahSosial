package mobile.omandotkom.dakwahsosial.pojo;

import android.text.Html;
import android.text.SpannableString;

public class Post {
    private final String STATUS = "publish";
    private final String EXCERPT = "view";
    private String title, content;
    private ImageResponse imageResponse = null;
    private String html;
    public String getSTATUS() {
        return STATUS;
    }

    public String getEXCERPT() {
        return EXCERPT;
    }

    public ImageResponse getImageResponse() {
        if (imageResponse!=null) return imageResponse;
        else return null;
    }

    public void setImageResponse(ImageResponse imageResponse) {
        this.imageResponse = imageResponse;
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
}
