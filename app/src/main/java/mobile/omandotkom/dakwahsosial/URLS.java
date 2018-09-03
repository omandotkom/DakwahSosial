package mobile.omandotkom.dakwahsosial;

public class URLS {
    public static final String SERVER_URL ="http://192.168.43.32/dakwah-wp/wordpress";
    public static String UPLOAD_URL = SERVER_URL + "/index.php/wp-json/wp/v2/posts";
    public static String NOTIFICATION_URL = SERVER_URL + "/push/sender.php";
    public static String IMAGE_UPLOAD_URL = SERVER_URL + "/ImageUploader.php";
    public static String AUTH_URL = SERVER_URL + "/index.php/wp-json/jwt-auth/v1/token";
}
