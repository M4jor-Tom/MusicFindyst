package musicFindystPackage;

import resourcePackage.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleMusicScraper extends DirectWebScraper<MusicResource> implements MusicFindystInterface
{	
	private static final String GOOGLE_STRING_URL = "https://www.google.com";
	private static final String UNSET_ALBUM_NAME = "UNSET_ALBUM_NAME";
	
	private Elements _cachedSongElements;
	
	public GoogleMusicScraper()
	{
		super(GOOGLE_STRING_URL);
		setResources(new ArrayList<>());
	}
	
	public String getUrlForSearchBarQuery(String searchBarQuery)
	{
		return getFetchUrl() + "/search?q=" + searchBarQuery;
	}


	@Override
	public String correctAuthorName(String authorName)
	{
		return correctAuthorName(authorName, false, false);
	}
	
	public String correctAuthorName(String authorName, boolean cacheDocument, boolean albumsPage)
	{
		//Page selection
		String label = albumsPage
			? " ablums"
			: " songs";
		
		//Search potentially typo'd author name on Google
		String stringUrlQuery = getUrlForSearchBarQuery(authorName + label);
		
		//Opening web document
		Document document = getDocument(stringUrlQuery);
		if(document == null)
			return null;
		
		//Acknowledging author name
		Element authorElement = document.select("span[data-elabel]").first();
		
		if(cacheDocument)
			setCachedDocument(document);
		
		if(authorElement == null)
			return null;
		
		return authorElement.html();
	}

	@Override
	public String correctAlbumName(String albumName, String authorName)
	{
		return correctAlbumName(albumName, authorName, false);
	}
	
	public String correctAlbumName(String albumName, String authorName, boolean cacheSongElements)
	{
		String albumDataSelector = ".rl_container";
		String htmlSongContainerSelector = "div[data-attrid=kc:/music/album:songs]";
		
		//Getting album's songs
		Elements albumData = getDocument(getUrlForSearchBarQuery(albumName + " musics"))
			.select(albumDataSelector);
		
		Elements songElements = albumData.select(htmlSongContainerSelector);
		if(songElements.size() == 0 && authorName != null)
		{
			//If no song were found without author's name in query
			albumData = getDocument(getUrlForSearchBarQuery(authorName + " " + albumName + " musics"))
				.select(albumDataSelector);
			songElements = albumData.select(htmlSongContainerSelector);
		}
		
		if(cacheSongElements)
			setCachedSongElements(songElements);
		
		//Getting the two potential album name Elements
		Elements potentialAlbumNameElements = albumData
			.select("div[aria-level=\"2\"]")
			.select("span[data-elabel]");

		//Getting the correct album element's index
		int albumNameLength = albumName.length();
		
		try
		{
			//Finding good album name
			return Math.abs(potentialAlbumNameElements.get(0).html().length() - albumNameLength)
					< Math.abs(potentialAlbumNameElements.get(1).html().length() - albumNameLength)
						? potentialAlbumNameElements.get(0).html()
						: potentialAlbumNameElements.get(1).html();
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	@Override
	public String correctAlbumName(String albumName)
	{
		return correctAlbumName(albumName, null);
	}

	@Override
	public String correctMusicResourceName(String MusicResourceName, String additionalInfo)
	{
		//Google search
		additionalInfo = additionalInfo + " ";
		String stringUrlQuery = getUrlForSearchBarQuery(additionalInfo + MusicResourceName);
		
		//Opening web document
		Document document = getDocument(stringUrlQuery);
		
		if(document == null)
			return null;
		
		//returning content of "showing results for ..." without html beacons
		return Jsoup.parse(document.select("a[id=fprsl]").get(0).html()).text().replace(additionalInfo, "");
	}
	
	@Override
	public List<Album> findAlbums(String authorName)
	{
		return findAlbums(authorName, false);
	}
	
	public List<Album> findAlbums(String authorName, boolean checkOtherAuthors)
	{
		List<Album> albums = new ArrayList<>();
		
		//Correcting author's name
		String correctAuthorName = correctAuthorName(authorName, true, true);

		//Scraping songs from google songs panels
		Elements albumPanels = getCachedDocument().select("a[role=listitem]");
		
		//Presentation of fetching progression
		int foundAlbum = 0;
		System.out.println("Found albums for author: " + correctAuthorName);
		for(Element albumPanel: albumPanels)
		{
			//Getting current album name
			String albumName = albumPanel.attr("aria-label");
			
			//Instantiation of Album object
			albums.add(
				checkOtherAuthors
					? new Album(findAuthors(albumName), albumName)
					: new Album(new Author(correctAuthorName), albumName)
			);
			
			//Printing album's data
			System.out.println("Album #" + ++foundAlbum + ": " + albumName);
		}
		
		//Empty document cache
		emptyCachedDocument();
		
		return albums;
	}

	public List<Author> findAuthors(String albumName)
	{
		return findAuthors(albumName, false);
	}
	
	public List<Author> findAuthors(String albumName, boolean wordAlbum)
	{
		ArrayList<Author> authors = new ArrayList<>();
		
		//Updating fetching URL to match author name
		String stringUrlQuery = getUrlForSearchBarQuery((wordAlbum ? "Album " : "") + albumName + " artist");
		
		//Opening web document
		Document document = getDocument(stringUrlQuery);
		if(document == null)
			//getDocument() didn't work fetching this author
			return new ArrayList<>();
		else
			//Printing fetching URL
			System.out.println("Finding authors on: \"" + stringUrlQuery + "\" ...");
		
		//Getting author
		Elements authorElement = document
				.select("#wp-tabs-container")
				.select("div[data-attrid=kc:/music/album:artist]")
				.select("a[href*=\"search\"]");
		if(authorElement != null)
			//Lone author
			authors.add(new Author(authorElement.html()));
		else if(wordAlbum == false)
			return findAuthors(albumName, true);
		else 
			System.out.println("Couldn't find Album's lone Author");
		
		return authors;
	}


	@Override
	public List<MusicResource> findMusicResourcesByAuthorName(String authorName)
	{
		return findMusicResourcesByAuthorName(authorName, false);
	}
	public List<MusicResource> findMusicResourcesByAuthorName(String authorName, boolean trust)
	{
		for(Album album: findAlbums(authorName))
			getMusicResources().addAll(
				findMusicResources(
					new Author(
						trust
						
							//Trust program's argument
							? authorName
							
							//Author name correction for user Type
							: album.getAuthors().get(0).getName()),
						
						album.getName()
					)
			);
		
		return getMusicResources();
	}

	@Override
	public List<MusicResource> findMusicResourcesByAlbumName(String albumName)
	{
		Author author = findAuthors(albumName).get(0);
		return findMusicResources(author, albumName);
	}

	@Override
	public List<MusicResource> findMusicResources(Author author, String albumName)
	{
		String correctAlbumName = UNSET_ALBUM_NAME;
		if((correctAlbumName = correctAlbumName(albumName, author.getName(), true)) == null)
			System.out.println("Problem reading correct album name, set to " + UNSET_ALBUM_NAME + " instead");
		
		System.out.println("Album \"" + correctAlbumName + "\":");
		
		//Adding new songs to album, and printing informations
		int foundSong = 0;
		for(Element songElement: getCachedSongElements())
		{
			//Getting song's name
			String songName = songElement.select("div.title").html();
			
			//Getting song's google stringUrl
			String songStringUrl = getFetchUrl() + songElement.select("a.rl_item.rl_item_base").attr("href");
			
			//Getting son's youtube URL
			Document googleSongPage = getDocument(songStringUrl);
			String youtubeSongStringUrl = googleSongPage.select("#rcnt").select("a[href*=\"youtube\"]").attr("href");
			
			try
			{
				//Finding out if url exists
				URL youtubeSongUrl = new URL(youtubeSongStringUrl);
				
				//Instantiation of MusicResource object
				//[WARNING] Only the current album is added to the MusicResource
				getMusicResources().add(new MusicResource(youtubeSongUrl, null, songName, new Album(author, correctAlbumName)));
				
				System.out.println("Song #" + ++foundSong + ": " + songName + " (" + youtubeSongUrl + ")");
			}
			catch(MalformedURLException e)
			{
				System.out.println("Unknown string url for " + songName + ": " + youtubeSongStringUrl);
			}
		}
		
		emptyCachedSongElements();
		
		return getMusicResources();
	}
	
	@Override
	public List<MusicResource> getMusicResources()
	{
		return getResources();
	}
	
	public List<Album> getAllAlbums()
	{
		List<Album> albums = new ArrayList<>();
		
		for(MusicResource musicResource: getMusicResources())
			albums.addAll(musicResource.getAlbums());
		
		return albums;
	}

	public List<Author> getAllAuthors()
	{
		List<Author> authors = new ArrayList<>();
		
		for(MusicResource musicResource: getMusicResources())
			authors.addAll(musicResource.getAuthors());
		
		return authors;
	}

	public Elements getCachedSongElements()
	{
		return _cachedSongElements;
	}

	public void setCachedSongElements(Elements cachedSongElements)
	{
		_cachedSongElements = cachedSongElements;
	}
	
	public void cacheSongElements(Elements cachedSongElements)
	{
		if(getCachedSongElements() == null)
			setCachedSongElements(cachedSongElements);
	}
	
	public void emptyCachedSongElements()
	{
		setCachedSongElements(null);
	}
}