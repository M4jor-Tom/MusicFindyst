package testPackage;

import musicFindystPackage.GoogleMusicScraper;
import musicFindystPackage.MusicFindystInterface;

public class testClass
{
	public static void main(String[] args)
	{
		String
			typoAlbumName = "smoke + mirors",
			typoAuthorName = "dafte pounk";
		
		MusicFindystInterface musicFindystInterface = new GoogleMusicScraper();
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