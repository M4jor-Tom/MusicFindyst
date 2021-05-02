package com.example.androidmusyst.MusicFindyst.src.musicFindystPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.androidmusyst.Resourcyst.src.resourcePackage.Resource;

public abstract class DirectWebScraper<ResourceType extends Resource>
{
	private List<ResourceType> _resources;
	private String _fetchUrl;
	protected final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";
	
	private Document _cachedDocument;
	
	public DirectWebScraper(String fetchUrl)
	{
		//Initialization of Resources and Cookies to null
		setResources(new ArrayList<>());
		
		//Setting data
		setFetchUrl(fetchUrl);
		setCachedDocument(null);
	}

	public static Document getDocument(String stringUrl) throws TooMuchQueriesException
	{
		try
		{
			return Jsoup.connect(stringUrl).get();
		}
		catch (IOException e)
		{
			if(e.toString().contains("Status=429"))
				throw new TooMuchQueriesException();
			else
				e.printStackTrace();
		}
		return null;
	}

	public List<ResourceType> getResources()
	{
		//Returning actual resources
		return _resources;
	}
	
	public void setResources(List<ResourceType> resources)
	{
		_resources = resources;
	}
	
	public String getFetchUrl()
	{
		return _fetchUrl;
	}
	
	public void setFetchUrl(String fetchUrl)
	{
		_fetchUrl = fetchUrl;
	}

	protected Document getCachedDocument()
	{
		return _cachedDocument;
	}

	protected void setCachedDocument(Document cachedDocument)
	{
		_cachedDocument = cachedDocument;
	}
	
	protected void cacheDocument(String stringUrl) throws TooMuchQueriesException
	{
		if(getCachedDocument() == null)
			setCachedDocument(getDocument(stringUrl));
	}
	
	protected void emptyCachedDocument()
	{
		setCachedDocument(null);
	}
}