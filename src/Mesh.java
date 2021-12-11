import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageFilter;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeListener;


public class Mesh extends JFrame implements ActionListener{


private MeshCanvas mymeshone;
private MeshCanvas mymeshtwo;

private JPanel controlP;
private JButton clearB;
private JButton warpB;
private JButton preview;
private JButton animate, OpenImage;
private JSlider tween_slider;
private JLabel slider_label;

private JPanel positionP;
private JLabel xposL;
private JLabel yposL;

JMenuBar menuBar;
JMenuItem setImage1;
JMenuItem setImage2;

// constructor: creates GUI
public Mesh() {
	super("Simple Mesh");

	setSize(getPreferredSize());

	menuBar = new JMenuBar();
	JMenu file =  new JMenu("File");
	JMenu settings =  new JMenu("Settings");
	JMenu help =  new JMenu("Help");
	setImage1 = new JMenuItem("Open Left Image File...");
	setImage2 = new JMenuItem("Open Right Image File...");
	setImage1.addActionListener(this);
	setImage2.addActionListener(this);
	file.add(setImage1);
	file.add(setImage2);
	menuBar.add(file);
	menuBar.add(settings);
	menuBar.add(help);
	this.setJMenuBar(menuBar);


	Container c = getContentPane();         // create container
	c.setBackground(new Color(232, 232, 232));
	c.setForeground(new Color(0, 0, 0));
	//c.setLayout(new BorderLayout());

	JPanel panel = new JPanel();
	panel.setPreferredSize(new Dimension(1500, 800));
	panel.setLayout(new FlowLayout(5));
	// new canvas

	mymeshone = new MeshCanvas(6);
	//mymeshone.setImage("test.jpg");
	mymeshtwo = new MeshCanvas(6);
	//mymeshtwo.setImage("test.jpg");
	mymeshone.setSister(mymeshtwo);
	mymeshtwo.setSister(mymeshone);
	panel.add(mymeshone);
	panel.add(mymeshtwo);

	c.add(panel, BorderLayout.CENTER);

	// add control buttons
	clearB = new JButton("Clear");
	clearB.addActionListener(this);
	preview = new JButton("Preview");
	preview.addActionListener(this);
	warpB = new JButton("Warp");
	warpB.addActionListener(this);
	tween_slider = new JSlider(10,99,10);
	tween_slider.setPaintLabels(true);
	tween_slider.setPaintTicks(true);
	tween_slider.setMajorTickSpacing(10);
	tween_slider.setMinorTickSpacing(5);
	slider_label = new JLabel("Tween Frames:");


	controlP = new JPanel(new FlowLayout());
	controlP.setForeground(new Color(0, 0, 0));
	controlP.add(clearB);
	controlP.add(warpB);
	controlP.add(preview);
	controlP.add(slider_label);
	controlP.add(tween_slider);
	c.add(controlP, BorderLayout.SOUTH);

	// add position labels
	positionP = new JPanel(new FlowLayout());

	xposL = new JLabel("X: ");		// xposL used temporarily
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);
	xposL = new JLabel("000");		// real xposL
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);

	yposL = new JLabel("  Y: ");	// yposL used temporarily
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);
	yposL = new JLabel("000");		// real yposL
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);

	c.add(positionP, BorderLayout.NORTH);

	// keep track of mouse position
	mymeshone.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
		}
		public void mouseDragged(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
		}
	} );

	// allow use of "X" button to exit
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent winEvt) {
			setVisible(false);
			System.exit(0);
		}
	} );

} // Mesh ()


// capture button actions
public void actionPerformed(ActionEvent evt) {
	Object src = evt.getSource();

	// reset the original image and show the mesh
	if(src == clearB) {
		mymeshone.clear();
		mymeshtwo.clear();
	}
	if(src == warpB) {
		mymeshone.makeWarp();
	}
	if(src == preview) {
		JFrame f = new JFrame();
		MeshAnimate anim = new MeshAnimate(6, mymeshone.getPoints(), mymeshtwo.getPoints(), tween_slider.getValue());
		f.add(anim);
		JPanel bottom = new JPanel(new FlowLayout());
		f.setSize(800,800);
		f.setVisible(true);
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	if(src == setImage1){
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(this);
		if(option == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			//label.setText("File Selected: " + file.getName());
		}else{
			//label.setText("Open command canceled");
		}
	}

} // actionPerformed()


// main: creates new instance of Mesh object
public static void main(String args[]) {
	Mesh pl;
	pl = new Mesh();
	pl.setSize(pl.getPreferredSize().width, pl.getPreferredSize().height);
	pl.setVisible(true);
} // main()


}
