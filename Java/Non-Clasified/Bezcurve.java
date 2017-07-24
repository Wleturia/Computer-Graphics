/*
 * Copyright (c) 1993-1997, Silicon Graphics, Inc.
 * ALL RIGHTS RESERVED 
 * Permission to use, copy, modify, and distribute this software for 
 * any purpose and without fee is hereby granted, provided that the above
 * copyright notice appear in all copies and that both the copyright notice
 * and this permission notice appear in supporting documentation, and that 
 * the name of Silicon Graphics, Inc. not be used in advertising
 * or publicity pertaining to distribution of the software without specific,
 * written prior permission. 
 */

/*
 * Created on 21/02/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * 
 * Bezcurve.java			
 *  This program renders a lighted, filled Bezier surface,
 *  using two-dimensional evaluators.
 *
 * @converted to Java by Wu Shin - Ting
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.media.opengl.*;

public class Bezcurve extends JFrame implements GLEventListener, 
						KeyListener {
    public static final Dimension PREFERRED_FRAME_SIZE = new Dimension(500, 500);
	
    private float[] ctrlpoints = {-4.0f, -4.0f, 0.0f,-2.0f,4.0f, 0.0f, 
				  2.0f, -4.0f, 0.0f, 4.0f, 4.0f, 0.0f};
	
    /** Constructor.
     */
    public Bezcurve() {
	// init JFrame
	super ("Bezcurve - JMDM version ");
	System.out.println("Constructor");
    }

    /** We'd like to be 500x500, please.
     */
    public Dimension getPreferredSize(){
	return PREFERRED_FRAME_SIZE;

    }

    /*
     * METHODS DEFINED BY GLEventListener
     */

    /** Called by drawable to initiate drawing. 
     */
    public void display(GLAutoDrawable drawable) {
	GL gl = drawable.getGL();
				
	gl.glClear(GL.GL_COLOR_BUFFER_BIT);
	gl.glColor3f(1.0f, 1.0f, 1.0f);
	gl.glBegin(GL.GL_LINE_STRIP);
	for (int i = 0; i <= 30; i++) 
	    gl.glEvalCoord1f((float)i/30.0f);
	gl.glEnd();
	/* The following code displays the control points as dots. */
	gl.glPointSize(5.0f);
	gl.glColor3f(1.0f, 1.0f, 0.0f);
	gl.glBegin(GL.GL_POINTS);
	for (int i = 0; i < 4; i++) {
	    gl.glVertex3fv(ctrlpoints,i*3);
	}
	gl.glEnd();
	gl.glFlush();
    }

    /** Called by drawable to indicate mode or device has changed.
     */
    public void displayChanged(GLAutoDrawable drawable, 
			       boolean modeChanged, 
			       boolean deviceChanged){}

    /** Called after OpenGL is init'ed
     */
    public void init(GLAutoDrawable drawable) {

    	System.out.println("init()");

    	GL gl = drawable.getGL();

    	System.err.println("INIT GL IS: " + gl.getClass().getName());
    	System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
    	System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
    	System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

    	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    	gl.glShadeModel(GL.GL_FLAT);
    	gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, ctrlpoints, 0); 
    	gl.glEnable(GL.GL_MAP1_VERTEX_3);
    	
    	drawable.addKeyListener(this);
    }

    /** Called to indicate the drawing surface has been moved and/or resized.
     */
    public void reshape(GLAutoDrawable drawable, 
			int x, int y, 
			int width, int height){

    	System.out.println("reshape()");

    	GL gl = drawable.getGL();

    	gl.glViewport(0, 0, width, height); 
    	gl.glMatrixMode (GL.GL_PROJECTION);
    	gl.glLoadIdentity ();
    	if (width <= height)
	    gl.glOrtho(-5.0f, 5.0f, -5.0f*(float)height/(float)width, 
  	               5.0f*(float)height/(float)width, -5.0f, 5.0f);
	else
	    gl.glOrtho(-5.0f*(float)width/(float)height, 
  	               5.0f*(float)width/(float)height, -5.0f, 5.0f, -5.0f, 5.0f);
    	gl.glMatrixMode(GL.GL_MODELVIEW);
    	gl.glLoadIdentity();
    }

    /*
     * OUR HELPER METHODS
     */

    // Methods required for the implementation of KeyListener

      
    public void keyPressed(KeyEvent e){
        System.out.println("Key pressed");
      	if (e.getKeyChar() == 27){
	    System.exit(0);
      	}
    }

    public void keyReleased(KeyEvent e){}
      
    public void keyTyped(KeyEvent e){}
          
    
    /** main creates and shows a Bezcurve-JFrame
     */
    public static void main(String[] args){

    	Bezcurve g = new Bezcurve();
    	//Set frame location
    	g.setLocation(100,100);
    	GLCapabilities gl_c = new GLCapabilities();
    	// Disable double buffer
    	gl_c.setDoubleBuffered(false);
     	// get a GLCanvas
    	GLCanvas canvas = new GLCanvas(gl_c);
   	// add a GLEventListener, which will get called when the
    	// canvas is resized or needs a repaint
    	canvas.addGLEventListener(g);
    	// now add the canvas to the JFrame.  Note we use BorderLayout.CENTER
    	// to make the canvas stretch to fill the container (ie, the frame)
    	g.getContentPane().add(canvas, BorderLayout.CENTER);

    	g.pack();
    	g.setVisible(true);

    }
}
