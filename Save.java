package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Save implements Serializable{//save class for saving the state of the token when the game in paused in the array lists of the tokens
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int score,length,currency,len_blocks,len_balls,len_walls,len_shields,len_magnets,len_Dblocks,len_coins;
	double snakeX;
	
	List<Ball> ballx=new ArrayList<>(),bally=new ArrayList<>();
	List<Block> blockx=new ArrayList<>(),blocky=new ArrayList<>();
	List<Wall> wallx=new ArrayList<>(),wally=new ArrayList<>();
	List<Shield> shieldx=new ArrayList<>(),shieldy=new ArrayList<>();
	List<Magnet> magnetx=new ArrayList<>(),magnety=new ArrayList<>();
	List<Destroy> Dblockx=new ArrayList<>(),Dblocky=new ArrayList<>();
	List<Coin> coinx=new ArrayList<>(),coiny=new ArrayList<>();
	
	String Name;
	
}
