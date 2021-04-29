package com.example.androidmusyst.MusicFindyst.src.musicFindystPackage;

import java.util.List;

import com.example.androidmusyst.Resourcyst.src.resourcePackage.*;

public interface MusicFindystInterface
{
	//Corrects an Author name based on Google search
	public String correctAuthorName(String authorName);
	
	//Corrects an Album name based on a Google search (and an Author name ?)
	public String correctAlbumName(String albumName, String authorName);
	public String correctAlbumName(String albumName);
	
	//Corrects a MusicResource name based on an Album/Author name and a Google search
	public String correctMusicResourceName(String MusicResourceName, String additionalInfo);
	
	//Finds on Google existing Albums for a given Author's name
	public List<Album> findAlbums(String authorName);
	
	//Finds on Google existing musics as MusicResources for a given Album's name
	public List<MusicResource> findMusicResourcesByAlbumName(String albumName);

	//Finds on Google existing musics as MusicResources for a given Author's name
	public List<MusicResource> findMusicResourcesByAuthorName(String authorName);
	
	//Finds on Google existing musics as MusicResources for a given Author object and Album's name
	public List<MusicResource> findMusicResources(Author author, String albumName);
	
	//Mandatory getter
	public List<MusicResource> getMusicResources();
}