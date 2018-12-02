package Game;
import java.io.*;
public class Data implements Serializable{//data class for leaderboard entries
	 /**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	String name;
	 int score;
	 String date;

	Data(String name, int score, String date) {//data constructor
		this.name = name;
		this.score = score;
		this.date = date;
	}
}