package musicFindystPackage;

import java.util.ArrayList;

import resourcePackage.Album;
import resourcePackage.Author;
import resourcePackage.MusicResource;

public interface MusicFindystInterface
{
	//Finds on Google existing Albums for a given Author's name
	public ArrayList<Album> findAlbums(String authorName);
	
	//Finds on Google existing musics as MusicResources for a given Album's name
	public ArrayList<MusicResource> findMusicResourcesByAlbumName(String albumName);

	//Finds on Google existing musics as MusicResources for a given Author's name
	public ArrayList<MusicResource> findMusicResourcesByAuthorName(String authorName);
	
	//Finds on Google existing musics as MusicResources for a given Author object and Album's name
	public ArrayList<MusicResource> findMusicResources(Author author, String albumName);
	
	//Mandatory getter
	public ArrayList<MusicResource> getMusicResources();
}