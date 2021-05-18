package com.example.androidmusyst.MusicFindyst.src.testPackage;

import java.util.HashMap;

import com.example.androidmusyst.MusicFindyst.src.musicFindystPackage.*;
import com.example.androidmusyst.Resourcyst.src.resourcePackage.*;
import com.example.androidmusyst.Versionyst.src.versionystPackage.*;


public class testClass
{
	public static void versionsCheck()
	{
		//Variables initialization
		HashMap<String, Integer> existingDependencies = new HashMap<>();
		Versionable[] versionables =
		{
			new MusicFindystVersionable(),
			new ResourcystVersionable(),
			new Versionyst()
		};
		
		//Versions setting
		existingDependencies.put("ResourcystVersionable", versionables[1].getVersionId());
		existingDependencies.put("Versionyst", versionables[2].getVersionId());

		//Versions checking
		for(Versionable versionable: versionables)
			versionable.checkSubPackagesVersions(existingDependencies);
	}
	
	public static void main(String[] args)
	{
		versionsCheck();
		
		String
			typoSongName = "smok and mirors",
			typoAlbumName = "smoke + mirors",
			typoAuthorName = "dafte pounk";
		
		MusicFindystInterface musicFindystInterface = new GoogleMusicScraper();

		try
		{
			//Unit tests
			System.out.println(
				typoSongName + " correct name: " + musicFindystInterface.correctMusicResourceName(typoSongName, "Imagine dragons") + "\n" +
				typoAlbumName + " correct name: " + musicFindystInterface.correctAlbumName(typoAlbumName) + "\n" +
				typoAuthorName + " correct name: " + musicFindystInterface.correctAuthorName(typoAuthorName)
			);

				musicFindystInterface.findMusicResourcesByAlbumName(typoAlbumName);

			System.out.println(
				"Correct album name: " + musicFindystInterface.getFoundMusicResources().get(0).getAlbums().get(0)
				+ "\nRather than: " + typoAlbumName
			);

			musicFindystInterface.findMusicResourcesByAuthorName(typoAuthorName);
			System.out.println(
				"Correct author name: " + musicFindystInterface.getFoundMusicResources().get(musicFindystInterface.getFoundMusicResources().size() - 1).getAuthors().get(0)
				+ "\nRather than: " + typoAuthorName
			);
		}
		catch(TooMuchQueriesException e)
		{
			System.out.println(e.getMessage());
		}
	}
}