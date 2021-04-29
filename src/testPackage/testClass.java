package testPackage;

import java.util.HashMap;

import musicFindystPackage.DataNotFoundException;
import musicFindystPackage.GoogleMusicScraper;
import musicFindystPackage.MusicFindystInterface;
import musicFindystPackage.MusicFindystVersionable;
import resourcePackage.ResourcystVersionable;
import versionystPackage.Versionable;
import versionystPackage.Versionyst;

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
			typoAlbumName = "smoke + mirors",
			typoAuthorName = "dafte pounk";
		
		MusicFindystInterface musicFindystInterface = new GoogleMusicScraper();
		
		try
		{
			System.out.println(
				typoAlbumName + " correct name: " + musicFindystInterface.correctAlbumName(typoAlbumName) + "\n" +
				typoAuthorName + " correct name: " + musicFindystInterface.correctAuthorName(typoAuthorName)
			);
		}
		catch(DataNotFoundException e)
		{
			System.out.println("data not found");
		}
		
		musicFindystInterface.findMusicResourcesByAlbumName(typoAlbumName);
		
		System.out.println(
			"Correct album name: " + musicFindystInterface.getMusicResources().get(0).getAlbums().get(0)
			+ "\nRather than: " + typoAlbumName
		);
		
		musicFindystInterface.findMusicResourcesByAuthorName(typoAuthorName);
		System.out.println(
			"Correct author name: " + musicFindystInterface.getMusicResources().get(musicFindystInterface.getMusicResources().size() - 1).getAuthors().get(0)
			+ "\nRather than: " + typoAuthorName
		);
	}
}