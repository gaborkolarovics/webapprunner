package hu.polidor.webapprunner.nfc;

import java.io.Serializable;

public class TagRecord implements Serializable
{
	
	private String mimeType;

	private String data;

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getData()
	{
		return data;
	}

}

