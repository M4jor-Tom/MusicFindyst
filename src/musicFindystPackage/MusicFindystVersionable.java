package musicFindystPackage;

import versionystPackage.Versionable;

public class MusicFindystVersionable extends Versionable
{
	public MusicFindystVersionable()
	{
		super();
		getDependencies().put("ResourcystVersionable", 4);
		getDependencies().put("Versionyst", 4);
	}
	
	@Override
	public Integer getVersionId()
	{
		return 1;
	}
}