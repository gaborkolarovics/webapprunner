package hu.polidor.webapprunner.common;

/**
 * Common async task listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.01
 */
public interface AsyncTaskListener<T> {

    /**
     * Callback method
     */
    void OnTaskCompleted(T object);

}
