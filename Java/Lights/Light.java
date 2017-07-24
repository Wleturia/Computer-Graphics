/*
 * Created on 19/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author ting
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * Este programa demonstra as funcionalidades
 *  de iluminacao suportadas pelo OpenGL.
 * Duas fontes luminosas, 0 (vermelha) e 1 (azul), sao
 * indicadas na imagem por dois pontos azul e vermelho,
 * respectivamente.
 *
 * Dois 
 * objetos geométricos são iluminados por estas duas
 * fontes: tetraedro e cubo.
 *
 *   x,X: +/- 5.0;
 *   y,Y: +/- 5.0;
 *   z,Z: +/- 5.0;
 *   d : toggle difuso;
 *   e : toggle especular;
 *   a : toggle ambiente;
 *   f : toggle fontes móveis;
 *   0 : toggle light 0 (vermelha);
 *   1 : toggle light 1 (azul);
 *   r : reset;
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.*;

public class Light extends JFrame implements GLEventListener, 
KeyListener {
	public static final Dimension PREFERRED_FRAME_SIZE = new Dimension(800, 500);

	/* Fonte Luminosa */
	/* posicao da fonte */
	static float[] light_position=new float[4];
	/* intensidade ambiente (Ia) */
	static float[] light_ambient=new float[4];
	/* intensidade lambertiana/difusa/incoerente (Id) */
	static float[] light_diffuse=new float[4];
	/* intensidade especular (Is) */
	static float[] light_specular=new float[4];
	/* direcao da fonte spot (d) */
	static float[] spot_direction=new float[3];
	/* expoente de decaimento da intensidade (c) */
	static float spot_exponent;
	/* angulo de abrangencia da fonte spot (gamma) */
	static float spot_cutoff;
	/* coeficiente do termo constante do fator de atenuacao (c_1) */
	static float fat_constant;
	/* coeficiente do termo linear do fator de atenuacao (c_2) */
	static float fat_linear;
	/* coeficiente do termo quadratico do fator de atenuacao (c_3) */
	static float fat_quadratic;
	/* luz do ambiente */
	static float[] lmodel_ambient={0.2f,0.2f,0.2f,1.0f};

	/* Material do Objeto */
	static float[] mat_ambient=new float[4];   /* Ka*/
	static float[] mat_diffuse=new float[4];   /* Kd */
	static float[] mat_specular=new float[4];  /* Ks */
	static float mat_shininess;                            /* n */
	static float[] mat_emission=new float[4];

	static int rotateObjectLight = 1;        /* rotate light and object
	                                               simultaneously */

	static int especular=1, difuso=1, ambiente=1;

	static int light0=1, light1=1;

	static float dX = 0.0f, dY = 0.0f, dZ = 0.0f;
	static float rotX = 0.0f, rotY = 0.0f, rotZ = 0.0f;

	static float c = 1.73205080756887729f, length;

	static float w;


	/** Constructor.
	 */
	public Light() {
		// init JFrame
		super ("Light - JMDM version ");
		System.out.println("Constructor");
	}

	/** We'd like to be 800x500, please.
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

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();

		gl.glTranslatef (0.0f, 0.0f, -15.0f);

		/* Desenhar a piramide */
		length = c * 3.0f;

		/* Habilitar as fontes */
		if (light0==1) gl.glEnable(GL.GL_LIGHT0);
		else gl.glDisable(GL.GL_LIGHT0);
		if (light1==1) gl.glEnable(GL.GL_LIGHT1);
		else gl.glDisable (GL.GL_LIGHT1);

		if (rotateObjectLight==0) Light1(drawable);

		gl.glPushMatrix();
		gl.glTranslatef (3.0f, 0.0f, 0.0f);
		gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
		if (rotateObjectLight==1) Light1(drawable);
		/* Definir o material da piramide */
		MaterialPiramide(drawable);
		DrawTetrahedron(drawable);
		gl.glPopMatrix();

		/* Desenhar o cubo solido iluminado pela fonte <0> */
		w = 3.0f/2.f;

		if (rotateObjectLight==0) Light0(drawable);

		gl.glPushMatrix();
		gl.glTranslatef (-3.0f, 0.0f, 0.0f);
		gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
		if (rotateObjectLight==1) Light0(drawable);
		/* Definir o material do cubo */
		MaterialCube(drawable);
		DrawCube(drawable);
		gl.glPopMatrix();

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

		/* Setar o modo de tonalizacao em Gouraud */
		gl.glShadeModel(GL.GL_SMOOTH);
		/* Habilitar a iluminacao */
		gl.glEnable(GL.GL_LIGHTING);
		/* setar o modo de renderizacao das faces. Ha tres alternativas:
		 GL_POINT (somente vertices), GL_LINE (somente linhas) e GL_FILL
		 (faces preenchidas) */
		gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
		/* setar a convencao de orientacao. Ha duas alternativas: GL_CCW
		 (anti-horario) e GL_CW (horario) */
		gl.glFrontFace (GL.GL_CCW);
		/* Habilitar face culling */
		gl.glEnable (GL.GL_CULL_FACE);
		/* definir as faces a serem discardadas. Ha tres alternativas: GL_FRONT,
		 GL_BACK e GL_FRONT_AND_BACK */
		gl.glCullFace (GL.GL_BACK);
		
		/* setar uma luz de ambiente */
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
	
		/* setar o modo como a componente especular é calculada */
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, 0);
		
		/* setar o modo como a componente especular é integrada à intensidade */
		gl.glLightModeli(GL.GL_LIGHT_MODEL_COLOR_CONTROL, GL.GL_SINGLE_COLOR);
		
		/* setar os lados para os quais deve se aplicar o modelo de iluminacao */
		gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, 0);
		
		c /= (float)Math.sqrt(3.f * c * c);

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
		gl.glOrtho (-7.0f, 7.0f, -7.0f, 7.0f, 0.0f, 30.0f);
		/* chavear para a pilha de transformacao de modelos */
		gl.glMatrixMode (GL.GL_MODELVIEW);   	
	}

	/*
	 * OUR HELPER METHODS
	 */
	// Definir Light0
	public static void Light0(GLAutoDrawable drawable) {
		  GL gl = drawable.getGL();

		  /****************** Fonte 0  (Vermelha) *****************/
		  light_position[0] = -w-1.0f; light_position[1] = -w-1.0f; light_position[2] = -w-1.0f; light_position[3] = 1.0f;
		  light_ambient[0] = 1.0f; light_ambient[1] = 0.2f; light_ambient[2] = 0.2f; light_ambient[3] = 1.0f;
		  light_diffuse[0] = 1.0f; light_diffuse[1] = 0.2f; light_diffuse[2] = 0.2f; light_diffuse[3] = 1.0f;
		  light_specular[0] = 1.0f; light_specular[1] = 1.0f; light_specular[2] = 1.0f; light_specular[3] = 1.0f;
		  spot_direction[0] = 1.f/(float)Math.sqrt(3.f); 
		  spot_direction[1] = 1.f/(float)Math.sqrt(3.f); 
		  spot_direction[2] = 1.f/(float)Math.sqrt(3.f);

		  spot_exponent = 5.0f; /* defulat: 0 */
		  spot_cutoff = 45.0f;  /* angulo entre 0 a 90 */
		  fat_constant = 1.0f;  /* default: 1.0 */
		  fat_linear = 0.0f;    /* default: 0.0 */
		  fat_quadratic = 0.0f; /* deafult: 0.0 */

		  /* Definir a posicao da luz */
		  gl.glLightfv (GL.GL_LIGHT0, GL.GL_POSITION, light_position, 0);

		  /* Definir as intensidades ambiente, difusa e especular */
		  gl.glLightfv (GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
		  gl.glLightfv (GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
		  gl.glLightfv (GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);

		  /* Caso for fonte spot, definir a direcao, o expoente de decaimento e angulo */
		  gl.glLightfv (GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, spot_direction, 0);
		  gl.glLightf (GL.GL_LIGHT0, GL.GL_SPOT_EXPONENT, spot_exponent);
		  gl.glLightf (GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, spot_cutoff);

		  /* Definir o fator de atenuacao da intensidade da fonte */
		  gl.glLightf (GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, fat_constant);
		  gl.glLightf (GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, fat_linear);
		  gl.glLightf (GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, fat_quadratic);

		  gl.glDisable(GL.GL_LIGHTING); /* desabilitar o modelo de iluminacao para
		                               colorir o ponto com a "cor" especificada */
		  /* desenhar a posicao da fonte */
		  gl.glColor3f(1.0f, 0.0f, 0.0f);
		  gl.glPointSize(4.0f);
		  gl.glBegin(GL.GL_POINTS);
		  gl.glVertex3f(light_position[0],light_position[1],light_position[2]);
		  gl.glEnd();
		  gl.glPointSize(1.0f);

		  /* desenhar a direcao do feixe */
		  gl.glColor3f(1.0f, 1.0f, 1.0f);
		  gl.glBegin(GL.GL_LINES);
		  gl.glVertex3f(light_position[0],light_position[1],light_position[2]);
		  gl.glVertex3f(light_position[0]+spot_direction[0],light_position[1]+spot_direction[1],
		                 light_position[2]+spot_direction[2]);
		  gl.glEnd();

		  gl.glEnable(GL.GL_LIGHTING); /* reabilitar o modelo de iluminacao */
	}
	
	// Definir Light1
	public static void Light1(GLAutoDrawable drawable) {
		  GL gl = drawable.getGL();

		  /****************** Fonte 1  (Azul) *****************/
		  light_position[0] = length+1.0f; light_position[1] = length+1.0f; light_position[2] = length+1.0f; light_position[3] = 1.0f;
		  light_ambient[0] = 0.2f; light_ambient[1] = 0.2f; light_ambient[2] = 1.0f; light_ambient[3] = 1.0f;
		  light_diffuse[0] = 0.2f; light_diffuse[1] = 0.2f; light_diffuse[2] = 1.0f; light_diffuse[3] = 1.0f;
		  light_specular[0] = 1.0f; light_specular[1] = 1.0f; light_specular[2] = 1.0f; light_specular[3] = 1.0f;
		  spot_direction[0] = -1.f/(float)Math.sqrt(3.f); 
		  spot_direction[1] = -1.f/(float)Math.sqrt(3.f); 
		  spot_direction[2] = -1.f/(float)Math.sqrt(3.f);

		  spot_exponent = 0.0f; /* defulat: 0 */
		  spot_cutoff = 60.0f;  /* angulo entre 0 a 90 */
		  fat_constant = 1.0f;  /* default: 1.0 */
		  fat_linear = 0.0f;    /* default: 0.0 */
		  fat_quadratic = 0.0f; /* deafult: 0.0 */

		  /* Definir a posicao da luz */
		  gl.glLightfv (GL.GL_LIGHT1, GL.GL_POSITION, light_position, 0);

		  /* Definir as intensidades ambiente, difusa e especular */
		  gl.glLightfv (GL.GL_LIGHT1, GL.GL_AMBIENT, light_ambient, 0);
		  gl.glLightfv (GL.GL_LIGHT1, GL.GL_DIFFUSE, light_diffuse, 0);
		  gl.glLightfv (GL.GL_LIGHT1, GL.GL_SPECULAR, light_specular, 0);

		  /* Caso for fonte spot, definir a direcao, o expoente de decaimento e angulo */
		  gl.glLightfv (GL.GL_LIGHT1, GL.GL_SPOT_DIRECTION, spot_direction, 0);
		  gl.glLightf (GL.GL_LIGHT1, GL.GL_SPOT_EXPONENT, spot_exponent);
		  gl.glLightf (GL.GL_LIGHT1, GL.GL_SPOT_CUTOFF, spot_cutoff);

		  /* Definir o fator de atenuacao da intensidade da fonte */
		  gl.glLightf (GL.GL_LIGHT1, GL.GL_CONSTANT_ATTENUATION, fat_constant);
		  gl.glLightf (GL.GL_LIGHT1, GL.GL_LINEAR_ATTENUATION, fat_linear);
		  gl.glLightf (GL.GL_LIGHT1, GL.GL_QUADRATIC_ATTENUATION, fat_quadratic);

		  gl.glDisable(GL.GL_LIGHTING); /* desabilitar o modelo de iluminacao para
		                               colorir o ponto com a "cor" especificada */
		  /* desenhar a posicao da fonte */
		  gl.glColor3f(0.0f, 0.0f, 1.0f);
		  gl.glPointSize(4.0f);
		  gl.glBegin(GL.GL_POINTS);
		  gl.glVertex3f(light_position[0],light_position[1],light_position[2]);
		  gl.glEnd();
		  gl.glPointSize(1.0f);

		  /* desenhar a direcao do feixe */
		  gl.glColor3f(1.0f, 1.0f, 1.0f);
		  gl.glBegin(GL.GL_LINES);
		  gl.glVertex3f(light_position[0],light_position[1],light_position[2]);
		  gl.glVertex3f(light_position[0]+spot_direction[0],light_position[1]+spot_direction[1],
		                 light_position[2]+spot_direction[2]);
		  gl.glEnd();

		  gl.glEnable(GL.GL_LIGHTING); /* reabilitar o modelo de iluminacao */
		
	}
	
	// Definir o material do cubo
	public static void MaterialCube (GLAutoDrawable drawable) {

		  GL gl = drawable.getGL();
		  mat_emission[0] = 0.0f; mat_emission[1] = 0.0f; mat_emission[2] = 0.0f; mat_emission[3] = 1.0f;

		 if (especular==1) {
		   mat_specular[0] = 1.0f; mat_specular[1] = 1.0f; mat_specular[2] = 1.0f; mat_specular[3] = 1.0f;
		   mat_shininess = 50.0f;  /* default : 0.0 */
		 } else {
		   mat_specular[0] = mat_specular[1] = mat_specular[2] = mat_specular[3] = 0.0f;
		 }

		 if (ambiente==1) {
		    mat_ambient[0] = 0.2f; mat_ambient[1] = 0.2f; mat_ambient[2] = 0.2f; mat_ambient[3] = 1.0f;
		 } else {
		    mat_ambient[0] = mat_ambient[1] = mat_ambient[2] = mat_ambient[3] = 0.0f;
		 }

		 if (difuso==1) {
		      mat_diffuse[0] = 0.8f; mat_diffuse[1] = 0.8f; mat_diffuse[2] = 0.8f; mat_diffuse[3] = 1.0f;
		 } else {
		      mat_diffuse[0] = mat_diffuse[1] = mat_diffuse[2] = mat_diffuse[3] = 0.0f;
		 }

		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
		 gl.glMaterialf (GL.GL_FRONT, GL.GL_SHININESS, mat_shininess);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
	}
	
	// Definir o material do tetraedro
	public static void MaterialPiramide (GLAutoDrawable drawable) {
		
		  GL gl = drawable.getGL();
		  mat_emission[0] = 0.0f; mat_emission[1] = 0.0f; mat_emission[2] = 0.0f; mat_emission[3] = 1.0f;

		 if (especular==1) {
		   mat_specular[0] = 1.0f; mat_specular[1] = 1.0f; mat_specular[2] = 1.0f; mat_specular[3] = 1.0f;
		   mat_shininess = 50.0f;
		 } else {
		   mat_specular[0] = mat_specular[1] = mat_specular[2] = mat_specular[3] = 0.0f;
		 }

		 if (ambiente==1) {
		    mat_ambient[0] = 0.0f; mat_ambient[1] = 0.2f; mat_ambient[2] = 0.2f; mat_ambient[3] = 1.0f;
		 } else {
		    mat_ambient[0] = mat_ambient[1] = mat_ambient[2] = mat_ambient[3] = 0.0f;
		 }

		 if (difuso==1) {
		      mat_diffuse[0] = 0.0f; mat_diffuse[1] = 0.8f; mat_diffuse[2] = 0.8f; mat_diffuse[3] = 1.0f;
		 } else {
		      mat_diffuse[0] = mat_diffuse[1] = mat_diffuse[2] = mat_diffuse[3] = 0.0f;
		 }

		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
		 gl.glMaterialf (GL.GL_FRONT, GL.GL_SHININESS, mat_shininess);
		 gl.glMaterialfv (GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
	}
	
	// Desenhar o tetraedro
	public static void DrawTetrahedron(GLAutoDrawable drawable) {
		
		  double l;
		  float[] v1=new float[3], v2=new float[3], cp=new float[3];
		  double norm, x, y, z;

		  GL gl = drawable.getGL();

		  /* Desenhar um tetraedron inscrito numa esfera
		     de raio <length/c>. Observe que cada vértice
		     tem 3 normais definidos */

		  /* Face 1 */
		  v1[0] = -2.f * length; v1[1] = -2.f * length; v1[2] = 0.0f;
		  v2[0] = 2.f * length; v2[1] = 0.0f; v2[2] = -2.f * length;
		  /* calculo da normal normalizada da face */
		  cp[0] = v1[1]*v2[2] - v2[1]*v1[2];
		  cp[1] = v1[2]*v2[0] - v2[2]*v1[0];
		  cp[2] = v1[0]*v2[1] - v2[0]*v1[1];
		  norm = (float)Math.sqrt(cp[0]*cp[0] + cp[1]*cp[1] + cp[2]*cp[2]);
		  cp[0] /= norm; cp[1] /= norm; cp[2] /= norm;
		  /* especificar a geometria da face: sequencia de vertices, associando
		     a todos eles a normal da face */
		  gl.glBegin (GL.GL_TRIANGLES);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, -length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, -length, -length);
		  gl.glEnd();

		  /* Face 2 */
		  v1[0] = 2.f * length; v1[1] = 0.0f; v1[2] = -2.f * length;
		  v2[0] = 0.0f; v2[1] = -2.f * length; v2[2] = -2.f * length;
		  /* calculo da normal normalizada da face */
		  cp[0] = v1[1]*v2[2] - v2[1]*v1[2];
		  cp[1] = v1[2]*v2[0] - v2[2]*v1[0];
		  cp[2] = v1[0]*v2[1] - v2[0]*v1[1];
		  norm = (float)Math.sqrt(cp[0]*cp[0] + cp[1]*cp[1] + cp[2]*cp[2]);
		  cp[0] /= norm; cp[1] /= norm; cp[2] /= norm;
		  /* especificar a geometria da face: sequencia de vertices, associando
		     a todos eles a normal da face */
		  gl.glBegin (GL.GL_TRIANGLES);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, length, -length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, -length, -length);
		  gl.glEnd();

		  /* Face 3 */
		  v1[0] = 2.f * length; v1[1] = 2.f * length; v1[2] = 0.0f;
		  v2[0] = -2.f * length; v2[1] = 0.0f; v2[2] = -2.f * length;
		  /* calculo da normal normalizada da face */
		  cp[0] = v1[1]*v2[2] - v2[1]*v1[2];
		  cp[1] = v1[2]*v2[0] - v2[2]*v1[0];
		  cp[2] = v1[0]*v2[1] - v2[0]*v1[1];
		  norm = (float)Math.sqrt(cp[0]*cp[0] + cp[1]*cp[1] + cp[2]*cp[2]);
		  cp[0] /= norm; cp[1] /= norm; cp[2] /= norm;
		  /* especificar a geometria da face: sequencia de vertices, associando
		     a todos eles a normal da face */
		  gl.glBegin (GL.GL_TRIANGLES);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, -length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, length, -length);
		  gl.glEnd();

		  /* Face 4 */
		  v1[0] = -2.f * length; v1[1] = 0.0f; v1[2] = 2.f * length;
		  v2[0] = 0.0f; v2[1] = 2.f * length; v2[2] = -2.f * length;
		  /* calculo da normal normalizada da face */
		  cp[0] = v1[1]*v2[2] - v2[1]*v1[2];
		  cp[1] = v1[2]*v2[0] - v2[2]*v1[0];
		  cp[2] = v1[0]*v2[1] - v2[0]*v1[1];
		  norm = (float)Math.sqrt(cp[0]*cp[0] + cp[1]*cp[1] + cp[2]*cp[2]);
		  cp[0] /= norm; cp[1] /= norm; cp[2] /= norm;
		  /* especificar a geometria da face: sequencia de vertices, associando
		     a todos eles a normal da face */
		  gl.glBegin (GL.GL_TRIANGLES);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (length, -length, -length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, -length, length);
		    gl.glNormal3f(cp[0],cp[1],cp[2]);
		    gl.glVertex3f (-length, length, -length);
		  gl.glEnd ();
}
	
	// Desenhar o Cubo
	public static void DrawCube(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();

		/* definir as normais e os vértices das 6 faces */
	    double[][] n = {
	        {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {1.0, 0.0, 0.0},
	        {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, -1.0}
	    };
	    int[][] faces = {
	        { 0, 1, 2, 3 }, { 3, 2, 6, 7 }, { 7, 6, 5, 4 },
	        { 4, 5, 1, 0 }, { 6, 2, 1, 5 }, { 3, 7, 4, 0 }
	    };

	    double[][] v=new double[8][3];
	    int i;

	    /* Desenhar um cubo de lado <2*w> centrado na origem.
	       Novamente, observe que em cada vértice há 3 normais
	       definidos. */

	    v[0][0] = v[1][0] = v[2][0] = v[3][0] = -w;
	    v[4][0] = v[5][0] = v[6][0] = v[7][0] = w;
	    v[0][1] = v[1][1] = v[4][1] = v[5][1] = -w;
	    v[2][1] = v[3][1] = v[6][1] = v[7][1] = w;
	    v[0][2] = v[3][2] = v[4][2] = v[7][2] = -w;
	    v[1][2] = v[2][2] = v[5][2] = v[6][2] = w;

	    for (i = 0; i < 6; i++) {
	        gl.glBegin(GL.GL_POLYGON);
	        gl.glNormal3d(n[i][0],n[i][1],n[i][2]);
	        gl.glVertex3d(v[faces[i][0]][0],v[faces[i][0]][1],v[faces[i][0]][2]);
	        gl.glNormal3d(n[i][0],n[i][1],n[i][2]);
	        gl.glVertex3d(v[faces[i][1]][0],v[faces[i][1]][1],v[faces[i][1]][2]);
	        gl.glNormal3d(n[i][0],n[i][1],n[i][2]);
	        gl.glVertex3d(v[faces[i][2]][0],v[faces[i][2]][1],v[faces[i][2]][2]);
	        gl.glNormal3d(n[i][0],n[i][1],n[i][2]);
	        gl.glVertex3d(v[faces[i][3]][0],v[faces[i][3]][1],v[faces[i][3]][2]);
	        gl.glEnd();
	    }
}
	
	// Methods required for the implementation of KeyListener

    public void keyPressed(KeyEvent e){
		switch(e.getKeyChar())
		{
		case 'x':
	         /* Incremento de 5 no angulo de rotacao em torno de x */
	    	rotX += 5.f;
			break;
		case 'y':
	         /* Incremento de 5 no angulo de rotacao em torno de y */
	    	rotY += 5.f;
			break;
		case 'z':
	         /* Incremento de 5 no angulo de rotacao em torno de z */
	    	rotZ += 5.f;
			break;
		case 'X':
	         /* Decremento de 5 no angulo de rotacao em torno de x */
	    	rotX -= 5.f;
			break;
		case 'Y':
	         /* Decremento de 5 no angulo de rotacao em torno de y */
	    	rotY -= 5.f;
			break;
		case 'Z':
	         /* Decremento de 5 no angulo de rotacao em torno de z */
	    	rotZ -= 5.f;
			break;
		case 'd':
	    	if (difuso==1) difuso=0; else difuso = 1;
			break;
		case 'e':
	    	if (especular==1) especular=0; else especular = 1;
			break;
		case 'a':
	    	if (ambiente==1) ambiente=0; else ambiente = 1;
			break;
		case 'f':
	    	if (rotateObjectLight==1) rotateObjectLight=0; else rotateObjectLight=1;
			break;
		case '0':
	    	if (light0==1) light0=0; else light0=1;
			break;
		case '1':
	    	if (light1==1) light1=0; else light1=1;
			break;
		case 'r':
	         dX = dY = dZ = 0.0f;
	         rotX = rotY = rotZ = 0.0f;
	         ambiente = difuso = especular = 1;
	         rotateObjectLight = 1;
			break;
		case 27:
			System.exit(0);
	        return;
    	}
    	/* Forca o redesenho */
	    ((GLCanvas)(this.getContentPane().getComponentAt(0,0))).display();
    }

    public void keyReleased(KeyEvent e){}
    
    public void keyTyped(KeyEvent e){}
        
    /** main creates and shows a Cube-JFrame
     */
    public static void main(String[] args){

    	Light g = new Light();    
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