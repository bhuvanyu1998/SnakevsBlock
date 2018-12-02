package Game;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.scene.*;

public abstract class Animation implements Serializable{ //class for defining all tokens

	 transient Node view;
	int t=1;
	//protected Image img;
	 transient Point2D velocity=new Point2D(0,0);
	private boolean alive=true;
	double tranX=0,tranY=0;
	
	public Animation(Node view)
	{ //constructor
		this.view = view;
	}
	
	  public void update() { //method for the updation of each and every token
	        tranX=view.getTranslateX() + velocity.getX();
	        tranY=view.getTranslateY() + velocity.getY();
	        view.setTranslateY(tranY);
	        view.setTranslateX(tranX);
	        if(t==1)
	        {
	        	velocity=velocity.add(0.015	, 0.1);
	        	t=0;
	        }
	        else
	        {
	        	velocity=velocity.add(-0.015, 0.1);
	        	t=1;
	        }
	        	
	    }
	  
	  public void textupdate() { //method to update the text value, specifically for the ball and the block
		  view.setTranslateY(view.getTranslateY() + velocity.getY()-10);
	  }


	  
	  public Point2D getVelocity() { //method returning the token's velocity
	        return velocity;
	    }

	    public Node getView() { //method returning the token's, that is, the node's view
	        return view;
	    }

	    public boolean isAlive() { //method returning whether or not the node/token is alive
	        return alive;
	    }

	    public boolean isDead() { //method returning whether or not the node/token is dead
	        return !alive;
	    }

	    public void setAlive(boolean alive) { //method to set node alive
	        this.alive = alive;
	    }
	    
	    public void setVelocity(Point2D vel) { //method to set velocity
	        this.velocity = vel;
	    }
	    
	    public boolean isColliding(Animation other) { //method to detect the collision of two node's views, that is, the tokens
	        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	    }
	  
}



 class Block extends Animation implements Serializable{ //block token class extending parent class animation	 

	int val = (int) (1 + Math.random() * 15);
	transient Text value;
	transient SnakeGame sg;

	public static Color changecol() {//method to change block's colours, for different blocks to have different colours as they appear in the game
		double tempval = Math.random();
		if (tempval <= 0.2)
			return Color.AQUA;
		else if (tempval <= 0.4)
			return Color.GREENYELLOW;
		else if (tempval <= 0.6)
			return Color.GREEN;
		else if (tempval <= 0.8)
			return Color.ORANGE;
		else
			return Color.RED;
	}

	public int getVal() {//method returning the block's value
		return val;
	}

	Block(SnakeGame s) {//block's constructor1

		super(new Rectangle(50, 50, changecol()));
		sg=s;

	}

	Block(ImageView img, SnakeGame s) {//block's constructor2
		super(img);
		sg=s;
	}

	public void update() {
		if(sg.len<=10)
			{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(tranY);
			}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}

	public void textupdate() {
		
		
		value.setText(val+"");
		if(sg.len<=10)
		value.setTranslateY(getView().getTranslateY() + 2);
		else
		value.setTranslateY(getView().getTranslateY() + 0.3*sg.len);

}
 }

 
 
 
 
class Destroy extends Block implements Serializable{//destroy blocks token class extending parent class animation
		Destroy(ImageView img, SnakeGame s) {

			super(img,s);
			sg=s;
		}

		public void update() {
			if(sg.len<=10)
			{
				tranY=getView().getTranslateY() + 2;
				getView().setTranslateY(getView().getTranslateY() + 2);
			}
			else
			{
				tranY=getView().getTranslateY() + 0.3*sg.len;
				getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
		}
	}


class Wall extends Animation implements Serializable{//wall token class extending parent class animation
	transient SnakeGame sg;
	
	Wall(SnakeGame s) {
		super(new Rectangle(5, 75 + Math.random() * 100, Color.WHITE));
		sg=s;
	}

	public void update() {

		if(sg.len<=10)
		{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
		}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}
}

class Shield extends Animation implements Serializable{//shield token class extending parent class animation
	transient SnakeGame sg;
	Shield(ImageView img,SnakeGame s) {

		super(img);
		sg=s;
	}

	public void update() {

		if(sg.len<=10)
			{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
			}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}
}




class Magnet extends Animation implements Serializable{//magnet token class extending parent class animation
	transient SnakeGame sg;
	Magnet(ImageView img,SnakeGame s) {

		super(img);
		sg=s;
	}

	public void update() {
		if(sg.len<=10)
		{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
		}
		else	
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}
}

class Ball extends Animation implements Serializable{//ball token class extending parent class animation
	transient SnakeGame sg;
	int val = (int) (1 + Math.random() * 6);
	transient Text value;

	Ball(SnakeGame s) {
		super(new Circle(10, 10, 10, Color.YELLOW));
		sg=s;
	}

	public void update() {

		if(sg.len<=10)
		{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
		}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}

	public void textupdate() {
		if(sg.len<=10)
		value.setTranslateY(getView().getTranslateY() + 2);
		else
			value.setTranslateY(getView().getTranslateY() + 0.3*sg.len);
	}

}

class Coin extends Animation implements Serializable{//coin token class extending parent class animation
	transient SnakeGame sg;
	Coin(ImageView img,SnakeGame s) {

		super(img);
		sg=s;
	}

	public void update() {
		if(sg.len<=10)
		{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
		}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}
}


class Danger extends Animation implements Serializable{//danger token class extending parent class animation
	transient SnakeGame sg;
	Danger(ImageView img,SnakeGame s) {

		super(img);
		sg=s;
	}

	public void update() {
		if(sg.len<=10)
		{
			tranY=getView().getTranslateY() + 2;
			getView().setTranslateY(getView().getTranslateY() + 2);
		}
		else
			{
			tranY=getView().getTranslateY() + 0.3*sg.len;
			getView().setTranslateY(getView().getTranslateY() + 0.3*sg.len);
			}
	}
}

