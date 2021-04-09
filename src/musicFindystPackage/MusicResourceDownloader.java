package musicFindystPackage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Connection;

import resourcePackage.MusicResource;

public abstract class MusicResourceDownloader extends DirectWebScraper<MusicResource>
{
	private final String DOWNLOAD_FOLDER_PATH = "D:\\Download\\";
	
	public MusicResourceDownloader(String fetchUrl)
	{
		super(fetchUrl);
		
		try
		{
			Files.createDirectories(Paths.get(DOWNLOAD_FOLDER_PATH));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract Connection.Response getResponse(String resourceStringUrl) throws IOException;
	
	public void download(String downloadStringSubDirectory, String fileStringPath, String resourceStringUrl)
	{
		try
		{
			//Getting download data from the Internet
			Connection.Response response = getResponse(resourceStringUrl);
			
			//Constructing download sub-directory
			Path downloadPath = Paths.get(DOWNLOAD_FOLDER_PATH + downloadStringSubDirectory);
			Files.createDirectories(downloadPath);
			
			//Creating local File
			File file = new File(
				downloadPath + "\\" + (
					fileStringPath
						.replace("/", "+")
						.replace("&amp;", "&")
						.replace("?", "")
				)
			);
			
			//Extracting data from Connection's response
	        BufferedInputStream bufferedInputStream = response.bodyStream();
	        
	        //Preparing to put data into new File
	        FileOutputStream fileOutputStream = new FileOutputStream(file);
	        byte[] buffer = new byte[1024];
	        
	        //Putting data into new File
	        int length = 0;
	        while((length = bufferedInputStream.read(buffer)) != -1)
	        	fileOutputStream.write(buffer, 0, length);
	        
	        //Closing streams
	        bufferedInputStream.close();
	        fileOutputStream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}