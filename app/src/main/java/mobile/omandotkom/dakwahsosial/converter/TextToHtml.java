package mobile.omandotkom.dakwahsosial.converter;

import mobile.omandotkom.dakwahsosial.data.Article;


public class TextToHtml {

    public static String getHtml(Article article){
StringBuilder builder = new StringBuilder();
        //replaces all break to <br>
       article.setContent(article.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
        //checks if image response null or not
        if (article.getImageMedia()!=null){
            //if not null
            if (!article.getImageMedia().isErrorResponse()){
                //if the imageResponse not error, so we add the image to html
            builder.append("<p style=\"text-align: left;\"><img class=\"size-medium wp-image-25 aligncenter\" src=\"" + article.getImageMedia().getUrl() + "\" alt=\"\" /></p>");
            }
        }
        builder.append("<p style=\"text-align: left;\">" + article.getContent() + "</p>");
        if (article.getDocument()!=null){
            if (!article.getDocument().isErrorResponse()){
                builder.append("<br><a href=\"" + article.getDocument().getUrl() + "\" download>unduh dokumen di sini.</a>");
            }
        }
        return builder.toString();
    }
}
