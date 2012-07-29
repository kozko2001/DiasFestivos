package net.coscolla.android.modules;

public class Errorable<T> {

	private T data;
	private boolean error;
	private String  error_message;

	private Exception error_exception;
	
	
	public Errorable(T data)
	{
		this.data = data;
		this.error = false;
	}
	
	public Errorable(Exception e)
	{
		this.error = true;
		this.error_exception = e;
	}
	
	public void setErrorMessage(String message)
	{
		this.error = true;
		this.error_message = message;
	}
	
	public boolean getError()
	{
		return this.error;
	}
	
	public Exception getException()
	{
		return this.error_exception;
	}
	public T getData()
	{
		if( ! error )
			return data;
		else
		{
			if( this.error_exception != null)
				throw new RuntimeException( this.error_exception);
			else if ( this.error_message != null )
				throw new RuntimeException(this.error_message);
			else 
				throw new RuntimeException();
		}
	}
}
