// Mesh and Warp Canvas


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
//import com.sun.image.codec.jpeg.*;


public class MeshCanvas extends JPanel
             implements MouseListener, MouseMotionListener {

private int x;
private int y;
public int grid_size;

public Point[][] points;
private Point selected;

private boolean firsttime;
private boolean showwarp=false;
private BufferedImage bim=null;
private BufferedImage bimwarp=null;
private Triangle S, T;
private int xsize, ysize;

private int highlighted_x;
private int highlighted_y;

private MeshCanvas sister;

private final int THRESHOLD_DISTANCE=10;

// constructor: creates an empty mesh point
public MeshCanvas (int numPoints) {
	sister = null;
	setSize(getPreferredSize());
	addMouseListener(this);
	addMouseMotionListener(this);
	this.grid_size = numPoints+1;
	points = new Point[grid_size][grid_size];
	selected = null;
	firsttime=true;
	setPreferredSize(new Dimension(700,700));
}

public void setSister(MeshCanvas s){
	sister = s;
}
public void setHighlighted(int x, int y){
	highlighted_x = x;
	highlighted_y=y;
}

public void setFirsttime(boolean ft){firsttime=ft;}

public Point[][] getPoints(){return points;}

// resets mesh point to center
public void clear() {
	selected = null;
	firsttime=true;
	showwarp=false;
	drawMesh();
}

public void mouseClicked(MouseEvent mevt) {
}
public void mouseEntered(MouseEvent mevt) {
}
public void mouseExited(MouseEvent mevt) {
}

// checks for user selecting the mesh control point within a threshold
// distance of the point

public void mousePressed(MouseEvent E) {
	int curx, cury;

	curx = E.getX();
	cury = E.getY();

	for(int i = 1; i < grid_size-1; i++) {
		for (int j = 1; j < grid_size-1; j++) {
			Point center = points[i][j];

			double distance = Math.sqrt((curx-center.x)*(curx-center.x)+(cury-center.y)*(cury-center.y));
			if (distance < THRESHOLD_DISTANCE){
				selected=center;
				firsttime=false;
				setHighlighted(i,j);
				repaint();
				if(sister!=null){
					sister.setHighlighted(i,j);
					sister.repaint();
				}
			}
		}
	}


}


// if a point is being dragged, the point is released
// otherwise, adds/removes a point at current position
public void mouseReleased(MouseEvent E) {

	// if a point was selected, it was just released
	if(selected!= null) {
		selected = null;
	}
	//makeWarp();
}


// if a point is selected, drag it
public void mouseDragged(MouseEvent E) {

	// if a point is selected, it's being moved
	// redraw (rubberbanding)
	if(selected!= null) {
		selected.x=E.getX();
		selected.y=E.getY();
                // Can show warp dynamically by unstubbing here
                // This will apply the warp during rubberbanding
	        //makeWarp();
		drawMesh();
	}
}

public void mouseMoved(MouseEvent mevt) {
}


// draws the mesh on top of the background image
public void drawMesh() {
	repaint();
}

private BufferedImage readImage (String file) {

   Image image = Toolkit.getDefaultToolkit().getImage(file);
   MediaTracker tracker = new MediaTracker (new Component () {});
   tracker.addImage(image, 0);
   try { tracker.waitForID (0); }
   catch (InterruptedException e) {}
      BufferedImage bim = new BufferedImage 
          (image.getWidth(this), image.getHeight(this), 
          BufferedImage.TYPE_INT_RGB);
   Graphics2D big = bim.createGraphics();
   big.drawImage (image, 0, 0, this);
   return bim;
}

public void setImage(String file) {
   bim = readImage(file);
   Dimension d = new Dimension(bim.getWidth(), bim.getHeight());
   System.out.println(d);
   setSize(d);
   System.out.println(this.getSize());
   this.repaint();
}

public void makeWarp() {

	if (bimwarp == null)
           bimwarp = new BufferedImage (bim.getWidth(this), 
                         bim.getHeight(this), 
                         BufferedImage.TYPE_INT_RGB);

	//Instead I want to define many triangles by an indeterminant amount of points
	//

	// Divide the image into 4 triangles defined by the one point
	// out in the image somewhere.
	// MorphTools has to be set up NOT to clear the destination image
	// each time it is called.

	S = new Triangle (0, 0, xsize/2, ysize/2, xsize, 0);
	T = new Triangle (0, 0, x, y, xsize, 0);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (0, 0, 0, ysize, xsize/2, ysize/2);
	T = new Triangle (0, 0, 0, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (0, ysize, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (0, ysize, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (xsize, 0, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (xsize, 0, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	showwarp=true;
	this.repaint();
}

// Over-ride update method 
public void update(Graphics g) {
	paint(g);
}

// paints the polyline
public void paint (Graphics g) {

	// draw lines from each corner of canvas to the mesh point
	// with a circle at the mesh point

	xsize=getWidth();
	ysize=getHeight();

	if (firsttime ) {
		//set points here
		x = xsize/2;
		y = ysize/2;


		int x_grid_dist = xsize/(grid_size-1);
		int y_grid_dist = ysize/(grid_size-1);

		for(int i = 0; i < grid_size; i++){
			for(int j = 0; j < grid_size; j++){
				points[i][j]= new Point(i*x_grid_dist,j*y_grid_dist);
			}
		}
		firsttime=false;
	}

	Graphics2D big = (Graphics2D) g;
	if (showwarp)
           big.drawImage(bimwarp, 0, 0, this);
	else {

		big.drawImage(bim, 0, 0, this);
		for(int i = 0; i < grid_size; i++) {
			for (int j = 0; j < grid_size; j++) {
				Point center = points[i][j];
				if(!( i+1 == grid_size)){
					Point right = points[i+1][j];
					g.drawLine(center.x,center.y,right.x,right.y);
				}
				if(!(j+1 == grid_size)) {
					Point left = points[i][j + 1];
					g.drawLine(center.x,center.y,left.x,left.y);
				}
				if(!(j+1 == grid_size || i+1 == grid_size)) {
					Point diag = points[i+1][j+1];
					g.drawLine(center.x,center.y,diag.x,diag.y);
				}
				if(i != 0 && j!=0 && !(j+1 == grid_size || i+1 == grid_size)) {
					if(i==highlighted_x && highlighted_y ==j)
						g.setColor(Color.RED);
					g.fillOval(center.x - 6, center.y - 6, 12, 12);
					g.setColor(Color.WHITE);
					g.fillOval(center.x - 5, center.y - 5, 10, 10);
					g.setColor(Color.BLACK);
				}
			}}
	}
} // paint()


}
