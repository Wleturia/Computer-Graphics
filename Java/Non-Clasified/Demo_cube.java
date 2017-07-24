/*
 * Created on 08/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author ting
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

public class Demo_cube extends JFrame implements GLEventListener, 
					    KeyListener {
    public static final Dimension PREFERRED_FRAME_SIZE = new Dimension(500, 500);

    static float v=0.577350269f;
    
	static float[][] vdata = {
	   {-1.0f, -1.0f, -1.0f}, {1.0f, -1.0f, -1.0f}, {1.0f, 1.0f, -1.0f}, {-1.0f, 1.0f, -1.0f},
	   {-1.0f, -1.0f, 1.0f}, {1.0f, -1.0f, 1.0f}, {1.0f, 1.0f, 1.0f}, {-1.0f, 1.0f, 1.0f}
	   };

	static float[][] ndata = {
	   {-v, -v, -v}, {v, -v, -v}, {v,v,-v}, {-v, v, -v}, 
	   {-v, -v, v}, {v, -v, v}, {v,v,v}, {-v, v, v} 
	   };

	static int[][] vindices = {
	   {0,3,2,1}, {2,3,7,6}, {0,4,7,3},
	   {1,2,6,5}, {4,5,6,7}, {0,1,5,4}
	   };

	static float[] m = {
	   1.0f,0.0f,0.0f,0.0f, 
	   0.0f,1.0f,0.0f,0.0f, 
	   0.0f,0.0f,1.0f,0.0f, 
	   0.0f,0.0f,0.0f,1.0f
	   };

	static float spin_x=0.0f;
	
    /** Constructor.
     */
    public Demo_cube() {
	// init JFrame
	super ("Demo_cube - JMDM version ");
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
	GLU glu = new GLU();
	GLUT glut = new GLUT();

	/* "CLEAR" o display */
	gl.glClear (GL.GL_COLOR_BUFFER_BIT);
	/* setar a cor de desenho em branco (1,1,1) */
	gl.glColor3f (1.0f, 1.0f, 1.0f);
	/* carregar a matriz de identidade na pilha de matriz de transformacao */
	gl.glLoadIdentity();
	/* empilhar a matriz de transformacao de camera  */
	glu.gluLookAt (0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	/* definir matriz de transformacao */
	m[0] = 1.0f; m[4] = 0.0f;  m[8] = 0.0f;  m[12] = .0f;
	m[1] = 0.0f; m[5] = 1.0f;  m[9] = 0.0f;  m[13] = .0f;
	m[2] = 0.0f; m[6] = 0.0f;  m[10] = 1.0f;  m[14] = 0.0f;
	m[3] = 0.0f; m[7] = 0.0f;  m[11] = 0.0f;  m[15] = 1.0f;
	/* concatenar a matriz com a do topo da pilha */
	gl.glMultMatrixf(m,0);

	/* concatenar a matriz de translacao com a do topo da pilha */
	//gl.glTranslatef(0.5f,0.5f,0.5f);
	   
	/* concatenar a matriz de rotacao com a do topo da pilha */
	gl.glRotatef(spin_x,1.0f,0.0f,0.0f);
	  
	/* concatenar a matriz de mudanca de escala com a do topo da pilha */
	//glScalef(0.5f,0.5f,0.5f);

	/* desenhar o cubo */
	for (int i=0; i<6; i++) {
	     gl.glBegin(GL.GL_POLYGON);
	     gl.glColor3f (1.0f, 0.0f, 0.0f);
	     gl.glNormal3f(ndata[vindices[i][0]][0],ndata[vindices[i][0]][1],ndata[vindices[i][0]][2]);
	     gl.glVertex3f(vdata[vindices[i][0]][0],vdata[vindices[i][0]][1],vdata[vindices[i][0]][2]);
	     gl.glColor3f (0.0f, 1.0f, 0.0f);
	     gl.glNormal3f(ndata[vindices[i][1]][0],ndata[vindices[i][1]][1],ndata[vindices[i][1]][2]);
	     gl.glVertex3f(vdata[vindices[i][1]][0],vdata[vindices[i][1]][1],vdata[vindices[i][1]][2]);
	     gl.glColor3f (0.0f, 0.0f, 1.0f);
	     gl.glNormal3f(ndata[vindices[i][2]][0],ndata[vindices[i][2]][1],ndata[vindices[i][2]][2]);
	     gl.glVertex3f(vdata[vindices[i][2]][0],vdata[vindices[i][2]][1],vdata[vindices[i][2]][2]);
	     gl.glColor3f (0.0f, 1.0f, 1.0f);
	     gl.glNormal3f(ndata[vindices[i][3]][0],ndata[vindices[i][3]][1],ndata[vindices[i][3]][2]);
	     gl.glVertex3f(vdata[vindices[i][3]][0],vdata[vindices[i][3]][1],vdata[vindices[i][3]][2]);
	     gl.glEnd();
	   } 
	   //glut.glutWireCube (2.0f);
	   /* forcar a execucao dos comandos enviados a OpenGL */
	   gl.glFlush ();	   

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

    	/* definir a cor em preto (0,0,0) como cor de "CLEAR" */
    	gl.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
    	/* setar o tipo de tonalizacao. Ha duas alternativas
    	      GL_FLAT (constante) e GL_SMOOTH (interpolacao linear
    	      entre os vertices) */
    	gl.glShadeModel (GL.GL_SMOOTH);
    	/* setar o tamanho do ponto em pixels */
    	gl.glPointSize(5.0f);
    	/* setar a largura da linha em pixels */
    	gl.glLineWidth(3.0f);
    	/* setar o modo de renderizacao das faces. Ha tres alternativas:
    	      GL_POINT (somente vertices), GL_LINE (somente linhas) e GL_FILL
    	      (faces preenchidas) */
    	gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
    	/* setar a convencao de orientacao. Ha duas alternativas: GL_CCW (anti-horario)      e GL_CW (horario) */
    	gl.glFrontFace (GL.GL_CCW);
    	/* Habilitar face culling */
    	gl.glEnable (GL.GL_CULL_FACE);
    	/* definir as faces a serem discardadas. Ha tres alternativas: GL_FRONT,
    	      GL_BACK e GL_FRONT_AND_BACK */
    	gl.glCullFace (GL.GL_FRONT);
    	
        /* habilitar os tratadores de eventos oriundos do teclado */
    	drawable.addKeyListener(this);    	
    }

    /** Called to indicate the drawing surface has been moved and/or resized.
     */
    public void reshape(GLAutoDrawable drawable, 
			int x, int y, 
			int width, int height){

    	System.out.println("reshape()");

    	GL gl = drawable.getGL();

    	/*definir o tamanho da janela no display, em pixels */
    	gl.glViewport(0, 0, width, height); 
    	/* chavear para a pilha de transformacao de projecao */ 
    	gl.glMatrixMode (GL.GL_PROJECTION);
    	gl.glLoadIdentity ();
    	gl.glOrtho (-2.0f, 2.0f, -2.0f, 2.0f, -20.0f, 20.0f);
    	//gl.glFrustum (-1.0f, 1.0f, -1.0f, 1.0f, 1.5f, 20.0f);
    	/* chavear para a pilha de transformacao de modelos */
    	gl.glMatrixMode (GL.GL_MODELVIEW);   	
    }

    /*
     * OUR HELPER METHODS
     */

    // Methods required for the implementation of KeyListener

    public void keyPressed(KeyEvent e){
    	System.out.println("Evento do teclado");
		switch(e.getKeyChar())
		{
		case 'x':
	    	spin_x += 10.f;
	    	/* Forca o redesenho */
    	    ((GLCanvas)(this.getContentPane().getComponentAt(0,0))).display();
			break;
		case 27:
			System.exit(0);
	    	break;
    	}
    }

    public void keyReleased(KeyEvent e){}
    
    public void keyTyped(KeyEvent e){}
        
    /** main creates and shows a Cube-JFrame
     */
    public static void main(String[] args){

    	Demo_cube g = new Demo_cube();    
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

