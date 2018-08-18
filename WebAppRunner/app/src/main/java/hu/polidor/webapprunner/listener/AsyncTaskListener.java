package hu.polidor.webapprunner.listener;

public interface AsyncTaskListener<T>
{
	
	void OnTaskCompleted(T object);
	
}
