package rs.direktnoizbaste.dizb.app;

/**
 * Created by 1 on 1/15/2016.
 */
public class AppConfig {
    // Server login url
    public static String URL_LOGIN_POST = "http://direktnoizbaste.rs/parametri.php";
    //"action=povuciPodatkeAndroidKorisnik&email=pera@gjsd.com&p=miki";

    public static String URL_LOGIN_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciPodatkeAndroidKorisnik&tag=%1$s&email=%2$s&p=%3$s";
    // Server register url
    public static String URL_REGISTER_POST = "http://direktnoizbaste.rs/parametri.php";
    //action=registrujAndroid&email=pera@gjsd.com&sifra=miki&komitentime=miki&komitentprezime=miki";
    public static String URL_REGISTER_GET = "http://direktnoizbaste.rs/parametri.php?action=registrujAndroid&tag=%1$s&email=%2$s&sifra=%3$s&komitentime=%4$s&komitentprezime=%5$s";
    //http://direktnoizbaste.rs/parametri.php?action=povuciSenzorUid&id=1
    public static String URL_SENSOR_LIST_GET = "http://direktnoizbaste.rs/parametri.php?action=povuciSensorUid&id=%1$s";
}
