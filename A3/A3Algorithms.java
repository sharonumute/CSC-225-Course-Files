/* A5Algorithms.java
   CSC 225 - Summer 2017
   Programming Assignment 3 - Image Algorithms


   B. Bird - 07/03/2017
*/
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;

public class A3Algorithms{

	/* FloodFillDFS(v, viewer, fillColour)
	   Traverse the component the vertex v using DFS and set the colour
	   of the pixels corresponding to all vertices encountered during the
	   traversal to fillColour.

	   To change the colour of a pixel at position (x,y) in the image to a
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void FloodFillDFS(PixelVertex v, ImageViewer225 viewer, Color fillColour){
		List<PixelVertex> temp=new ArrayList<PixelVertex>();
		temp.add(v);
		while(!temp.isEmpty()){
			v=temp.get(temp.size()-1);
			temp.remove(temp.size()-1);
			if(!v.wasVisited()){
				v.markVisited();
				viewer.setPixel(v.getX(),v.getY(),fillColour);
				for(PixelVertex tempV: v.getNeighbours()){
					temp.add(tempV);
				}
			}
		}
	}

	/* FloodFillBFS(v, viewer, fillColour)
	   Traverse the component the vertex v using BFS and set the colour
	   of the pixels corresponding to all vertices encountered during the
	   traversal to fillColour.

	   To change the colour of a pixel at position (x,y) in the image to a
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void FloodFillBFS(PixelVertex v, ImageViewer225 viewer, Color fillColour){
		Deque<PixelVertex> temp = new ArrayDeque<PixelVertex>();
		temp.add(v);
		while(!temp.isEmpty()){
			v=temp.remove();
			if(!v.wasVisited()){
				v.markVisited();
				viewer.setPixel(v.getX(),v.getY(),fillColour);
				for(PixelVertex tempV: v.getNeighbours()){
					temp.add(tempV);
				}
			}
		}
	}

	/* OutlineRegionDFS(v, viewer, outlineColour)
	   Traverse the component the vertex v using DFS and set the colour
	   of the pixels corresponding to all vertices with degree less than 4
	   encountered during the traversal to outlineColour.

	   To change the colour of a pixel at position (x,y) in the image to a
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void OutlineRegionDFS(PixelVertex v, ImageViewer225 viewer, Color outlineColour){
		List<PixelVertex> temp=new ArrayList<PixelVertex>();
		temp.add(v);
		while(!temp.isEmpty()){
			v=temp.get(temp.size()-1);
			temp.remove(temp.size()-1);
			if(!v.wasVisited()){
				v.markVisited();
				if(v.getDegree() < 4){
					viewer.setPixel(v.getX(),v.getY(),outlineColour);
				}
				for(PixelVertex tempV: v.getNeighbours()){
					temp.add(tempV);
				}
			}
		}
	}

	/* OutlineRegionBFS(v, viewer, outlineColour)
	   Traverse the component the vertex v using BFS and set the colour
	   of the pixels corresponding to all vertices with degree less than 4
	   encountered during the traversal to outlineColour.

	   To change the colour of a pixel at position (x,y) in the image to a
	   colour c, use
			viewer.setPixel(x,y,c);
	*/
	public static void OutlineRegionBFS(PixelVertex v, ImageViewer225 viewer, Color outlineColour){
		Deque<PixelVertex> temp = new ArrayDeque<PixelVertex>();
		temp.add(v);
		while(!temp.isEmpty()){
			v=temp.remove();
			if(!v.wasVisited()){
				v.markVisited();
				if(v.getDegree() < 4){
					viewer.setPixel(v.getX(),v.getY(),outlineColour);
				}
				for(PixelVertex tempV: v.getNeighbours()){
					temp.add(tempV);
				}
			}
		}
	}

	/* CountComponents(G)
	   Count the number of connected components in the provided PixelGraph
	   object.
	*/
	public static int CountComponents(PixelGraph G){
		int count=0;
		for(int i=0;i<G.getHeight();i++){
			for(int j=0;j<G.getWidth();j++){
				PixelVertex v=G.getPixelVertex(i,j);
				if(!v.wasVisited()){
						List<PixelVertex> temp=new ArrayList<PixelVertex>();
						temp.add(v);
						while(!temp.isEmpty()){
							v=temp.get(temp.size()-1);
							temp.remove(temp.size()-1);
							if(!v.wasVisited()){
								v.markVisited();
								for(PixelVertex tempV: v.getNeighbours()){
									temp.add(tempV);
								}
							}
						}
						count++;
				}
			}
		}
		return count;
	}
}
