#include <GL/glut.h>

GLsizei winWidth = 500, winHeight = 500;

void init(void){
    glClearColor(1.0, 1.0, 1.0, 0.0);
}

void display(void){
    glClear(GL.GL_COLOR_BUFFER_BIT);
    glColor3f(0.0, 0.0, 1.0);
    gluLookAt(5.0, 5.0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
    glScalef(1.5, 2.0, 1.0);
    glutWireCube(1.0);
    glScalef(0.8, 0.5, 0.8);
    glTranslatef(-6.0, -5.0, 0.0);
    glutWireDodecahedron();
    glTranslatef(6.6, 6.6, 2.0);
    glutWireTetrahedron();
    glTranslatef(-3.0, -1.0, 0.0);
    glutWireOctahedron();
    glScalef(-3.0, -1.0, 0.0);
    glTranslatef(4.3, -2.0, 0.5);
    glutWireIcosahedron();
    glFlush();
}

void reshape(GLint newWidth, GLint newHeight){
    glViewport(0, 0, newWidth, newHeight);
    glMatrixMode(GL.GL_PROJECTION);
    glFrustum(-1.0, 1.0, -1.0, 1.0, 2.0, 20.0);
    glMatrixMode(GL.GL_MODELVIEW);
    glClear(GL.GL_COLOR_BUFFER_BIT);
}

void idle(void){
    glutPostRedisplay();
}

int main(int argc, char** argv){
    glutInit (&argc, argv);
    glutInitWindowSize(winWidth, winHeight);
    glutInitWindowPosition(100, 100);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutCreateWindow("Poliedros en modelo alámbrico");
    init();
    glutReshapeFunc(reshape);
    glutDisplayFunc(display);
    glutMainLoop();
    return EXIT_SUCCESS;
}