package com.example.androidmusyst.MusicFindyst.src.musicFindystPackage;

import com.example.androidmusyst.Versionyst.src.versionystPackage.*;

public class MusicFindystVersionable extends Versionable
{
	public MusicFindystVersionable()
	{
		super();
		getDependencies().put("ResourcystVersionable", 5);
		getDependencies().put("Versionyst", 5);
	}
	
	@Override
	public Integer getVersionId()
	{
		return 9;
	}

	@Override
	protected String getPackageName()
	{
		return "MusicFindystVersionable";
	}
}