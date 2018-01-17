package asay.asaymobile.fetch;

/**
 * Created by s123725 on 18/10/2017.
 */

public interface AsyncTaskCompleteListener<T> {
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    void onTaskComplete(T result);}
