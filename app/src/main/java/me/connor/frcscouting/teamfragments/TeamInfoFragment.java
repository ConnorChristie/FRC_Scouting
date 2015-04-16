package me.connor.frcscouting.teamfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.connor.frcscouting.R;

public class TeamInfoFragment extends Fragment
{
	public TeamInfoFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_team_info, container, false);



		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public String toString()
	{
		return "Info";
	}
}
