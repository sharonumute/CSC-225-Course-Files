/* PixelVertex.java
   CSC 225 - Summer 2017
   Programming Assignment 3 - Pixel Vertex Data Structure


   B. Bird - 07/03/2017
*/
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;

public class PixelVertex{

	private int xcord, ycord;
	private Color value;
	private List<PixelVertex> neighbours;
	private int neighAmount;
	private boolean visited;
	
	/* Add a constructor here (if necessary) */
	public PixelVertex(int x, int y, Color color){
		xcord=x;
		ycord=y;
		value=color;
		neighbours=new ArrayList<PixelVertex>();
		neighAmount=0;
		visited=false;
	}
	
	/* getX()
	   Return the x-coordinate of the pixel corresponding to this vertex.
	*/
	public int getX(){
		return xcord;
	}
	
	/* getY()
	   Return the y-coordinate of the pixel corresponding to this vertex.
	*/
	public int getY(){
		return ycord;
	}
	
	/* getNeighbours()
	   Return an array containing references to all neighbours of this vertex.
	*/
	public PixelVertex[] getNeighbours(){
		return neighbours.toArray(new PixelVertex[neighbours.size()]);
	}
	
	/* addNeighbour(newNeighbour)
	   Add the provided vertex as a neighbour of this vertex.
	*/
	public void addNeighbour(PixelVertex newNeighbour){
		if(neighAmount<4){
			neighbours.add(newNeighbour);
			neighAmount++;
		}
	}
	
	/* removeNeighbour(neighbour)
	   If the provided vertex object is a neighbour of this vertex,
	   remove it from the list of neighbours.
	*/
	public void removeNeighbour(PixelVertex neighbour){
		for(int i=0; i<4; i++){
			if(neighbours.get(i).equals(neighbour)){
				neighbours.remove(i);
				neighAmount--;
			}
		}
	}
	
	/* getDegree()
	   Return the degree of this vertex.
	*/
	public int getDegree(){
		return neighAmount;
	}
	
	/* isNeighbour(otherVertex)
	   Return true if the provided PixelVertex object is a neighbour of this
	   vertex and false otherwise.
	*/
	public boolean isNeighbour(PixelVertex otherVertex){
		if(neighbours.contains(otherVertex)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		PixelVertex temp= (PixelVertex) other;
		if((this.getX() == temp.getX()) && (this.getY() == temp.getY())){
			return true;
		}else{
			return false;
		}
	}
	
	public void markVisited(){
		visited=true;
	}
	
	public boolean wasVisited(){
		return visited;
	}
	
}