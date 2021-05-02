package musicFindystPackage;

import java.util.List;

import resourcePackage.Album;
import resourcePackage.Author;
import resourcePackage.MusicResource;

public interface MusicFindystInterface
{
	//Corrects an Author name based on Google search
	public String correctAuthorName(String authorName) throws TooMuchQueriesException;
	
	//Corrects an Album name based on a Google search (and an Author name ?)
	public String correctAlbumName(String albumName, String authorName) throws TooMuchQueriesException;
	public String correctAlbumName(String albumName) throws TooMuchQueriesException;
	
	//Corrects a MusicResource name based on an Album/Author name and a Google search
	public String correctMusicResourceName(String MusicResourceName, String additionalInfo) throws TooMuchQueriesException;
	
	//Finds on Google existing Albums for a given Author's name
	public List<Album> findAlbums(String authorName) throws TooMuchQueriesException;
	
	//Finds on Google existing musics as MusicResources for a given Album's name
	public List<MusicResource> findMusicResourcesByAlbumName(String albumName) throws TooMuchQueriesException;

	//Finds on Google existing musics as MusicResources for a given Author's name
	public List<MusicResource> findMusicResourcesByAuthorName(String authorName) throws TooMuchQueriesException;
	
	//Finds on Google existing musics as MusicResources for a given Author object and Album's name
	public List<MusicResource> findMusicResources(Author author, String albumName) throws TooMuchQueriesException;
	
	//Mandatory getter
	public List<MusicResource> getMusicResources();
}