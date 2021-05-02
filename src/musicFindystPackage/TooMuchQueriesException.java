package musicFindystPackage;

public class TooMuchQueriesException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6119185287638500993L;

	@Override
	public String getMessage()
	{
		return "Too much queries sent, please wait an hour or change your Ip";
	}
}