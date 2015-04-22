package me.connor.frcscouting.teamfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.connor.frcscouting.R;
import me.connor.frcscouting.teaminfo.TeamInfoActivity;

public class TeamCommentsFragment extends Fragment
{
	public TeamCommentsFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_team_comments, container, false);
		final TeamInfoActivity teamInfo = (TeamInfoActivity) getActivity();

		((EditText) view.findViewById(R.id.comments)).setText(teamInfo.getTeam().getComments());

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public String toString()
	{
		return "Comments";
	}
}