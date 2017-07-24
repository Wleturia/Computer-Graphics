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
import static java.lang.System.exit;
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
public class Nurb01
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

    //GLUnurbsObj *Nurb1 ;
    boolean solido = false;
    float girax = 0, giray = 90, zoom = 40;

    float ptosctrl[][][]
            = {
                {{-8, -8, -8}, {-2.8f, 0, 0}, {-8, -8, -8}},
                {{1, 9, 8}, {-1, -2, 0}, {1, 9, -8}},
                {{-1, 9, 8}, {1, -2, 0}, {-1, 9, -8}},
                {{8, -8, 8}, {2.8f, 0, 0}, {8, -8, -8}}}; //

    //FloatBuffer ptosctrl_f = FloatBuffer.wrap(ptosctrl);
    public Nurb01() {
    }

    public static void main(String[] args) {
        GLCapabilities caps = new GLCapabilities();
        GLJPanel canvas = new GLJPanel(caps);
        Nurb01 demo = new Nurb01();
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

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        int u, v;
        float nodosu[] = {0.0f, 1.0f, 1.0f, 1.0f, 2.0f, 2.0f, 2.0f, 3.0f};
        float nodosv[] = {0.0f, 1.0f, 1.0f, 2.0f, 2.0f, 3.0f};

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        move(drawable);
        gl.glPointSize(9.0f);
        gl.glColor3f(0.0f, 0.7f, 0.0f);
        gl.glBegin(GL.GL_POINTS);
        for (u = 0; u < 4; u++) {
            for (v = 0; v < 3; v++) {
                //gl.glVertex3fv(ptosctrl[u][v]);
            }
        }
        gl.glEnd();
        gl.glColor3f(0.0f, 0.0f, 1.0f);

        if (solido == false) {
            //glu.gluNurbsProperty(Nurb1, GLU.GLU_DISPLAY_MODE, GLU_OUTLINE_POLYGON);
        } else {
            //glu.gluNurbsProperty(Nurb1, GLU_DISPLAY_MODE, GLU_FILL);
        }

        //glu.gluBeginSurface(Nurb1);
        //glu.gluNurbsSurface(Nurb1, 8, nodosu, 6, nodosv, 3 * 3, 3,  & ptosctrl[0][0][0], 4, 3, GL_MAP2_VERTEX_3);
        //glu.gluEndSurface(Nurb1);
        gl.glPopMatrix();

        //glut.glutSwapBuffers();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case 27:
                //if () {
                //glu.gluDeleteNurbsRenderer(/*Nurb1*/);
                //}
                exit(0);
            case KeyEvent.VK_I:
                zoom += 2;
                break;
            case KeyEvent.VK_O:
                zoom -= 2;
                break;
            case KeyEvent.VK_S:
                solido = !solido;
            case KeyEvent.VK_LEFT:
                giray -= 15;
                break;
            case KeyEvent.VK_RIGHT:
                giray += 15;
                break;
            case KeyEvent.VK_UP:
                girax -= 15;
                break;
            case KeyEvent.VK_DOWN:
                girax += 15;
            default:
                break;
        }
        //glut.glutPostRedisplay();

    }

    @Override
    public void keyReleased(KeyEvent key) {
    }

    @Override
    public void mouseClicked(MouseEvent mouse) {
    }

    @Override
    public void mousePressed(MouseEvent mouse) {
    }

    @Override
    public void mouseReleased(MouseEvent mouse) {
    }

    @Override
    public void mouseEntered(MouseEvent mouse) {
    }

    @Override
    public void mouseExited(MouseEvent mouse) {
    }

    private void move(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glTranslated(0, 0, zoom);
        gl.glRotatef(girax, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(giray, 0.0f, 1.0f, 0.0f);
    }
}
