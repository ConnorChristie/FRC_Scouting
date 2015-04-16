package me.connor.frcscouting.teams;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import me.connor.frcscouting.matches.TeamStatus;

public class Team implements Parcelable
{
	private int teamNumber;
	private String teamName;
	private String comments;

	private Map<String, Integer> stats;

	public Team(int teamNumber, String teamName, String comments)
	{
		this.teamNumber = teamNumber;
		this.teamName = teamName;
		this.comments = comments;

		stats = new HashMap<>();
	}

	public int getTeamNumber()
	{
		return teamNumber;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public String getComments()
	{
		return comments;
	}

	public Map<String, Integer> getStats()
	{
		return stats;
	}

	public TeamStatus getOffenseScore()
	{
		return TeamStatus.GOOD;
	}

	public TeamStatus getDefenseScore()
	{
		return TeamStatus.AVERAGE;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(teamNumber);
		dest.writeString(teamName);
		dest.writeString(comments);
	}

	public static final Creator<Team> CREATOR = new Creator<Team>()
	{
		@Override
		public Team createFromParcel(Parcel source)
		{
			return new Team(source);
		}

		@Override
		public Team[] newArray(int size)
		{
			return new Team[size];
		}
	};

	private Team(Parcel source)
	{
		teamNumber = source.readInt();
		teamName = source.readString();
		comments = source.readString();
	}
}
