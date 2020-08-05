package hu.polidor.webapprunner;

/**
 * Constant holder class
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.11.14
 */
public final class Const {

    /**
     * Private constructor with exception because is a util class
     */
    private Const() {
        throw new IllegalStateException("Constant holder class!");
    }

    /**
     * Tag for application logging
     */
    public static final String TAG = "_WEBAPP_";

    /**
     * Config: Screen orientation properties
     */
    public static final String CONF_SIGN_ORIENTATION = "signorientation";

    /**
     * FCM: Application chanel name
     */
    public static final String FCM_CHANEL_ID = "default";
}
