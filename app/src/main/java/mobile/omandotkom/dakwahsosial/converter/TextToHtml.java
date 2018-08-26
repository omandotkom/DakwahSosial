package mobile.omandotkom.dakwahsosial.converter;
import android.support.annotation.Nullable;

import mobile.omandotkom.dakwahsosial.pojo.ImageResponse;
import mobile.omandotkom.dakwahsosial.pojo.Post;


public class TextToHtml {

    public static String getHtml(Post post){
StringBuilder builder = new StringBuilder();
        //replaces all break to <br>
       post.setContent(post.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
        //checks if image response null or not
        if (post.getImageResponse()!=null){
            //if not null
            if (!post.getImageResponse().isErrorResponse()){
                //if the imageResponse not error, so we add the image to html
            builder.append("<p style=\"text-align: left;\"><img class=\"size-medium wp-image-25 aligncenter\" src=\"" + post.getImageResponse().getUrl() + "\" alt=\"\" /></p>");
            }
        }
        builder.append("<p style=\"text-align: left;\">" + post.getContent() + "</p>");
        return builder.toString();
    }
}
