/* PixelGraph.java
   CSC 225 - Summer 2017
   Programming Assignment 3 - Pixel Graph Data Structure

   B. Bird - 07/03/2017
*/

import java.awt.Color;

public class PixelGraph{

	PixelVertex[][] mainGraph;
	int row, column;

	/* PixelGraph constructor
	   Given a 2d array of colour values (where element [x][y] is the colour
	   of the pixel at position (x,y) in the image), initialize the data
	   structure to contain the pixel graph of the image.
	*/
	public PixelGraph(Color[][] imagePixels){
		row=imagePixels.length;
		column=imagePixels[0].length;
		mainGraph=new PixelVertex[row][column];

		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				PixelVertex temp = new PixelVertex(i, j, imagePixels[i][j]);
				mainGraph[i][j]=temp;
			}
		}

		setEdges(mainGraph,row,column, imagePixels);
	}

	private void setEdges(PixelVertex[][] vertices, int row, int column, Color[][] imagePixels){
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				if((i+1< row) && (imagePixels[i][j].equals(imagePixels[i+1][j]))){
					vertices[i][j].addNeighbour(vertices[i+1][j]);
				}
				if((i-1>=0) && (imagePixels[i][j].equals(imagePixels[i-1][j]))){
					vertices[i][j].addNeighbour(vertices[i-1][j]);
				}
				if((j+1< column) && (imagePixels[i][j].equals(imagePixels[i][j+1]))){
					vertices[i][j].addNeighbour(vertices[i][j+1]);
				}
				if((j-1>=0) && (imagePixels[i][j].equals(imagePixels[i][j-1]))){
					vertices[i][j].addNeighbour(vertices[i][j-1]);
				}
			}
		}
	}

	/* getPixelVertex(x,y)
	   Given an (x,y) coordinate pair, return the PixelVertex object
	   corresponding to the pixel at the provided coordinates.
	   This method is not required to perform any error checking (and you may
	   assume that the provided (x,y) pair is always a valid point in the
	   image).
	*/
	public PixelVertex getPixelVertex(int x, int y){
		return mainGraph[x][y];
	}

	/* getWidth()
	   Return the width of the image corresponding to this PixelGraph
	   object.
	*/
	public int getWidth(){
		return mainGraph[0].length;
	}

	/* getHeight()
	   Return the height of the image corresponding to this PixelGraph
	   object.
	*/
	public int getHeight(){
		return mainGraph.length;
	}

}
