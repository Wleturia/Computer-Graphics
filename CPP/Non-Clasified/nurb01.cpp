#include <GL/glut.h>

GLUnurbsObj *Nurb1;
GLboolean solido=GL_FALSE;
GLfloat girax = 0, giray=90, zoom=40;

GLfloat ptosctrl[4][3][3]=
{
    {{-8,-8,-8},{-2.8,0,0},{-8,-8,-8}},
    {{1,9,8},{-1,-2,0},{1,9,-8}},
    {{-1,9,8},{1,-2,0},{-1,9,-8}},
    {{8,-8,8},{2.8,0,0},{8,-8,-8}},
}

void mover(void){
    glTranslated(0,0,zoom);
    glRotatef(girax,1.0,0.0,0.0);
    glRotatef(giray,0.0,1.0,0.0);    
}

void teclado(unsigned char tecla, int x, int y){
    switch(tecla){
        case 27: if(Nurb1) gluDeleteNurbsRenderer(Nurb1);
        exit(0);
        case 'i': zoom+=2;
        break;
        case 'o': zoom-=2;
        break;
        case 's': solido=!solido;
        default: break;
    }
    glutPostRedisplay();
}

void flechas(int tecla, int x, int y){
    switch(tecla){
        case GLUT_KEY_LEFT: giray-=15; break;
        case GLUT_KEY_RIGHT: giray+=15; break;
        case GLUT_KEY_UP: girax-=15; break;
        case GLUT_KEY_DOWN: girax+=15;
        default:break;
    }
}

void dibujo(void){
    int u,v;
    GLfloat nodosu[8] = {0.0,1.0,1.0,1.0,2.0,2.0,2.0,3.0};
    GLfloat nodosv[6] = {0.0,1.0,1.0,2.0,2.0,3.0};

    glClear(GL_COLOR_BUFFER_BIT);

    glPushMatrix();
        mover();
        glPointSize(9.0);
        glColor3f(0.0,0.7,0.0);
        glBegin(GL_POINTS);
            for(u=0;u<4;u++)
                for(v=0;v<3;v++)
                    glVertex3fv(ptosctrl[u][v]);
        glEnd();
        glColor3f(0.0,0.0,1.0);

        if(solido == GL_FALSE)
            gluNurbsProperty(Nurb1,GLU_DISPLAY_MODE, GLU_OUTLINE_POLYGON);
        else
            gluNurbsProperty(Nurb1,GLU_DISPLAY_MODE, GLU_FILL);

        gluBeginSurface(Nurb1);
            gluNurbsSurface(Nurb1,8,nodosu,6,nodosv,3*3,3,&ptosctrl[0][0][0],4,3,GL_MAP2_VERTEX_3);
        gluEndSurface(Nurb1);
    glPopMatrix();

    glutSwapBuffers();
}

void ajusta(int ancho, int alto){
    glClearColor(1.0,1.0,1.0,0.0);
    glViewport(0,0,ancho,alto);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(45,(float)ancho/alto,10,150);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    Nurb1=gluNewNurbsRenderer();
    gluNurbsProperty(Nurb1,GLU_SAMPLING_TOLERANCE,20.0);
}