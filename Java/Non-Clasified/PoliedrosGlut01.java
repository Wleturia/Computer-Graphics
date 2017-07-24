/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
 //RECALL TO change the package

package ejercicio;

import com.sun.opengl.util.GLUT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import net.letskit.redbook.glskeleton;

/**
 *
 * @author Walter
 */
public class PoliedrosGlut01
        extends glskeleton//
        implements GLEventListener//
        ,
         KeyListener//
        ,
         MouseListener//
// , MouseMotionListener//
// , MouseWheelListener//
{
    //private GL gl;
    private GLU glu;
    private GLUT glut;


    //
    public PoliedrosGlut01() {
    }

    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities();
        GLJPanel canvas = new GLJPanel(caps);
        PoliedrosGlut01 demo = new PoliedrosGlut01();
        demo.setCanvas(canvas);
        canvas.addGLEventListener(demo);
        canvas.addKeyListener(demo);
        canvas.addMouseListener(demo);

//        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("movelight");
        
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        
        glu.gluLookAt(5.0, 5.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        
        gl.glScalef(1.5f, 2.0f, 1.0f);
        glut.glutWireCube(1.0f);
        
        gl.glScalef(0.8f, 0.5f, 0.8f);
        gl.glTranslatef(-6.0f, -5.0f, 0.0f);
        glut.glutWireDodecahedron();
        
        gl.glTranslatef(6.6f, 6.6f, 2.0f);
        glut.glutWireTetrahedron();
        
        gl.glTranslatef(-3.0f, -1.0f, 0.0f);
        glut.glutWireOctahedron();
        
        gl.glScalef(0.8f, 0.6f, 1.0f);
        gl.glTranslatef(4.3f, -2.0f, 0.5f);
        glut.glutWireIcosahedron();
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glFrustum(-1.0f, 1.0f, -1.0f, 1.0f, 2.0f, 20.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
    
    }

    public void keyReleased(KeyEvent key) {
    }

    public void mouseClicked(MouseEvent mouse) {
    }

    public void mousePressed(MouseEvent mouse) {
    }

    public void mouseReleased(MouseEvent mouse) {
    }

    public void mouseEntered(MouseEvent mouse) {
    }

    public void mouseExited(MouseEvent mouse) {
    }
}
