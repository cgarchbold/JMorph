import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

public class MeshAnimate extends MeshCanvas implements ActionListener {

    private Point[][] startingPoints;
    private Point[][] endingPoints;

    private Point2D.Double[][] doublePoints;

    private Timer animator;

    private int tween_frames;
    private int frame_counter;

    public MeshAnimate(int numPoints, Point[][] s, Point[][] e, int tween_frames) {
        super(numPoints);
        doublePoints = new Point2D.Double[this.grid_size][this.grid_size];
        this.tween_frames = tween_frames;
        this.setFirsttime(false);
        frame_counter = 1;
        startingPoints = s;
        endingPoints = e;
        animator = new Timer(1000/tween_frames,this);
        for (int i = 0; i < this.grid_size; i++) {
            for (int j = 0; j < this.grid_size; j++) {
                points[i][j] = new Point();
                points[i][j].x = startingPoints[i][j].x;
                points[i][j].y = startingPoints[i][j].y;
                doublePoints[i][j] = new Point2D.Double((double)startingPoints[i][j].x,(double)startingPoints[i][j].y);
            }
        }
        repaint();
        animator.start();
    }

    public void animate(int tween_frames) {
        for (int i = 0; i < this.grid_size; i++) {
            for (int j = 0; j < this.grid_size; j++) {
                int sx = startingPoints[i][j].x;
                int sy = startingPoints[i][j].y;
                int ex = endingPoints[i][j].x;
                int ey = endingPoints[i][j].y;

                double dx = (ex - sx) / (double)tween_frames;
                double dy = (ey - sy) / (double)tween_frames;

                doublePoints[i][j].x += dx;
                doublePoints[i][j].y += dy;

                points[i][j].x = (int) (doublePoints[i][j].x);
                points[i][j].y = (int) (doublePoints[i][j].y);
                repaint();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animator){
            if(frame_counter <= tween_frames) {
                animate(tween_frames);
                frame_counter++;
            }
        }

    }
}
