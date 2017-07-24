
#include <stdio.h>
#include <stdlib.h>
#include <GL/glut.h>
#include <GL/glext.h>


#define anchoVentana     512  	
#define altoVentana      512  	
#define posicionVentanaX  50 	
#define posicionVentanaY  50 	

int opcion = 1;


float vertices[8][3] = { { -10.0f, -10.0f, 10.0f },
{ 10.0f, -10.0f, 10.0f },
{ 10.0f, 10.0f, 10.0f },
{ -10.0f, 10.0f, 10.0f },
{ 10.0f, -10.0f, -10.0f },
{ -10.0f, -10.0f, -10.0f },
{ -10.0f, 10.0f, -10.0f },
{ 10.0f, 10.0f, -10.0f } };

int cubo[24] = 
{ 0, 1, 2, 3,
4, 5, 6, 7,
5, 0, 3, 6,
1, 4, 7, 2,
5, 0, 3, 6,
4, 1, 0, 5 };



GLuint bufferVertices = 0;
GLuint bufferIndices = 0;


#ifdef GL_ARB_vertex_buffer_object
PFNGLBINDBUFFERARBPROC glBindBufferARB = NULL;
PFNGLGENBUFFERSARBPROC glGenBuffersARB = NULL;
PFNGLBUFFERDATAARBPROC glBufferDataARB = NULL;
PFNGLDELETEBUFFERSARBPROC glDeleteBuffersARB = NULL;
#endif

void init() {
	glBindBufferARB = (PFNGLBINDBUFFERARBPROC)wglGetProcAddress("glBindBufferARB");
	glGenBuffersARB = (PFNGLGENBUFFERSARBPROC)wglGetProcAddress("glGenBuffersARB");
	glBufferDataARB = (PFNGLBUFFERDATAARBPROC)wglGetProcAddress("glBufferDataARB");
	glDeleteBuffersARB = (PFNGLDELETEBUFFERSARBPROC)wglGetProcAddress("glDeleteBuffersARB");

	if (glBindBufferARB == NULL)
		printf("No se encuentran las extensiones GL_ARB_vertex_buffer_object\n");
}

void proyeccion(void) {

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	//glOrtho(-30.0, 30.0, -30.0, 30.0, -30.0, 30.0);
	//glOrtho(0, 200, 0, 100, 1, -1);
	//glOrtho(0.0, 1.0, 0.2, 1.0, -1.0, 1.0);
	//glOrtho(-30, 30, -30.0, 30, -30.0, 30.0);
	//glOrtho(-200, 200, -200, 200, -200, 200);
	//glOrtho(0, 640, 480, 0, 1, -1);
	glOrtho(-100, 100, -100, 100, -100, 100);


}

void eventoVentana(GLsizei ancho, GLsizei alto) {
	glViewport(0, 0, ancho, alto);
	proyeccion();
}

void crearBufferVertices() {
	glGenBuffersARB(1, &bufferVertices);
	glBindBufferARB(GL_ARRAY_BUFFER_ARB, bufferVertices);
	glBufferDataARB(GL_ARRAY_BUFFER_ARB, 24 * sizeof(float), vertices, GL_STATIC_DRAW_ARB);
}

void crearBufferIndices() {
	glGenBuffersARB(1, &bufferIndices);
	glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, bufferIndices);
	glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 24 * sizeof(int), cubo, GL_STATIC_DRAW_ARB);
}

void modoInmediato() {

	int i;
	glColor3f(0.0, 0.0, 1.0);
	glBegin(GL_QUADS);
	for (i = 0; i<24; i++)
		glVertex3fv(&vertices[cubo[i]][0]);
	glEnd();
}

void conVertices() {

	glColor3f(0.0, 1.0, 0.0);
	glBindBufferARB(GL_ARRAY_BUFFER_ARB, bufferVertices);
	glVertexPointer(3, GL_FLOAT, 0, 0);
	glEnableClientState(GL_VERTEX_ARRAY);
	glDrawElements(GL_QUADS, 24, GL_UNSIGNED_INT, &cubo);
	glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
	glDisableClientState(GL_VERTEX_ARRAY);
}


void conIndices(){

	glColor3f(1.0, 0.0, 0.0);
	glBindBufferARB(GL_ARRAY_BUFFER_ARB, bufferVertices);
	glVertexPointer(3, GL_FLOAT, 0, 0);
	glEnableClientState(GL_VERTEX_ARRAY);
	glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, bufferIndices);
	glEnableClientState(GL_ELEMENT_ARRAY_BUFFER_ARB);
	glDrawElements(GL_QUADS, 24, GL_UNSIGNED_INT, 0);
	glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
	glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_ELEMENT_ARRAY_BUFFER_ARB);
}

void Dibuja()  {

	glClearColor(.95f, .95f, .95f, 0.0f);
	glClear(GL_COLOR_BUFFER_BIT);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	gluLookAt(1, 0, 0, -3, -3, -3, 0, 1, 0);

	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	glDisable(GL_POLYGON_OFFSET_FILL);

	switch (opcion) {
	case 1:
		modoInmediato();
		break;
	case 2:
		if (bufferVertices == 0)
			crearBufferVertices();
		conVertices();
		break;
	case 3:
		if (bufferIndices == 0)
			crearBufferIndices();
		conIndices();
		break;
	default:
		break;
	}

	glutSwapBuffers();
}

void Salir() {

	glDeleteBuffersARB(1, &bufferVertices);
	glDeleteBuffersARB(1, &bufferIndices);
	exit(0);
}

void eventoTeclado(unsigned char tecla, int x, int y) {

	switch (tecla) {
	case  27:  Salir(); break;
	case '1':  opcion = 1; Dibuja(); break;
	case '2':  opcion = 2; Dibuja(); break;
	case '3':  opcion = 3; Dibuja(); break;
	}
}

void opcionesVisualizacion(void) {

	printf(" 1 - Modo inmediato (azul)\n");
	printf(" 2 - Vertices almacenados en Vertex Buffers (verde)\n");
	printf(" 3 - Vertices e indices almacenados en Vertex Buffers (rojo)\n");
}

void  asociaEventos(void) {

	glutReshapeFunc(eventoVentana);
	glutKeyboardFunc(eventoTeclado);
}


void abreVentana(int numParametros, char **listaParametros) {

	glutInit(&numParametros, listaParametros);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize(anchoVentana, altoVentana);
	glutInitWindowPosition(posicionVentanaX, posicionVentanaY);
	glutCreateWindow(listaParametros[0]);
}

int main1(int numParametros, char **listaParametros) {
	abreVentana(numParametros, listaParametros);
	init();
	asociaEventos();
	glutDisplayFunc(Dibuja);
	opcionesVisualizacion();
	glutMainLoop();
	return 1;
}

