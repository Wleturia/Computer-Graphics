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
public class Luz05 extends glskeleton//
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

    private static int spin = 0;
    private static float diffuseMaterial[] = {0.5f, 0.5f, 0.5f, 1.0f};
    FloatBuffer diffuseMaterial_f = FloatBuffer.wrap(diffuseMaterial);
    private boolean diffuseColorChanged = false;

    //
    public Luz05() {
    }

    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities();
        GLJPanel canvas = new GLJPanel(caps);
        Luz05 demo = new Luz05();
        demo.setCanvas(canvas);
        canvas.addGLEventListener(demo);
        canvas.addKeyListener(demo);
        canvas.addMouseListener(demo);

//        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Luz04");
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
        //
        float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        FloatBuffer mat_specular_f = FloatBuffer.wrap(mat_specular);

        float light_position[] = {1.0f, 1.0f, 1.0f, 0.0f};
        FloatBuffer light_position_f = FloatBuffer.wrap(light_position);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diffuseMaterial_f);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular_f);
        gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 25.0f);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position_f);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);

        gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        //
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        if (diffuseColorChanged) {
            gl.glColor4fv(diffuseMaterial, 0);
            diffuseColorChanged = !diffuseColorChanged;
        }
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
            gl.glOrtho(-1.5, 1.5,
                    -1.5 * (float) h / (float) w, 1.5 * (float) h / (float) w,
                    -10.0, 10.0
            );
        } else {
            gl.glOrtho(-1.5 * (float) w / (float) h, 1.5 * (float) w / (float) h,
                    -1.5, 1.5,
                    -10.0, 10.0);
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

        switch (mouse.getButton()) {
            case MouseEvent.BUTTON1:
                diffuseMaterial[0] += 0.1;
                if (diffuseMaterial[0] > 1.0) {
                    diffuseMaterial[0] = 0.0f;
                }
                diffuseColorChanged = true;

                break;
            case MouseEvent.BUTTON2:
                diffuseMaterial[1] += 0.1;
                if (diffuseMaterial[1] > 1.0) {
                    diffuseMaterial[1] = 0.0f;
                }
                diffuseColorChanged = true;
                break;
            case MouseEvent.BUTTON3:
                diffuseMaterial[2] += 0.1;
                if (diffuseMaterial[2] > 1.0) {
                    diffuseMaterial[2] = 0.0f;
                }
                diffuseColorChanged = true;
                break;
            default:
                break;
        }

        super.refresh();
    }

    public void mouseReleased(MouseEvent mouse) {
    }

    public void mouseEntered(MouseEvent mouse) {
    }

    public void mouseExited(MouseEvent mouse) {
    }
}
