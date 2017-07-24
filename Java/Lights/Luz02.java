/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 //RECALL TO change the package

package luz;

import com.sun.opengl.util.GLUT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.FloatBuffer;
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
public class Luz02//
        extends glskeleton//
        implements GLEventListener//
        ,
         KeyListener//
        ,
         MouseListener//
// , MouseMotionListener//
// , MouseWheelListener//
{

    private GLU glu;
    private GLUT glut;

    //
    public Luz02() {
    }

    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities();
        GLJPanel canvas = new GLJPanel(caps);
        Luz02 demo = new Luz02();
        demo.setCanvas(canvas);
        canvas.addGLEventListener(demo);
        canvas.addKeyListener(demo);
        canvas.addMouseListener(demo);

//        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Luz02");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        //

        float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        FloatBuffer mat_specular_f = FloatBuffer.wrap(mat_specular);
        float mat_shininess[] = {50.0f};
        FloatBuffer mat_shininess_f = FloatBuffer.wrap(mat_shininess);

        float light_position[] = {1.0f, 1.0f, 1.0f, 0.0f};
        FloatBuffer light_position_f = FloatBuffer.wrap(light_position);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH);

        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular_f);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess_f);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position_f);

        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_DEPTH_TEST);
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        //
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        glut.glutSolidSphere(1.0, 20, 16);
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL gl = drawable.getGL();
        //
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if (w <= h) {
            gl.glOrtho(-1.5,
                    1.5,
                    -1.5 * (float) h / (float) w,
                    1.5 * (float) h / (float) w,
                    -10.0,
                    10.0
            );
        } else {
            gl.glOrtho(-1.5 * (float) w / (float) h,
                    1.5 * (float) w / (float) h,
                    -1.5,
                    1.5,
                    -10.0,
                    10.0
            );
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
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

