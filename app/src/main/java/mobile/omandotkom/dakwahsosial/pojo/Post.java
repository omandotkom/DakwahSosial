package mobile.omandotkom.dakwahsosial.pojo;

public class Post {
private String title, content;
private String imagePath;
private final String STATUS = "publish";
private final String EXCERPT = "view";

    public String getSTATUS() {
        return STATUS;
    }

    public String getEXCERPT() {
        return EXCERPT;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    private String toHtml(){
    return "";
}
}
