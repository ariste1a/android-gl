package edu.union;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLU;
import android.view.KeyEvent;

public class GLTutorialEleven extends GLTutorialBase {
	float y = 1.5f;
	
	float lightAmbient[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float[] lightPos = new float[] {0,0,3,1};
	
	float matAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float matDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	
	float white[] = new float[] { 1f, 1f, 1f, 1.0f};
	float trans[] = new float[] { 1f, 1f, 1f, 0.3f};
	
	float box[] = new float[] {
			// FRONT
			-0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			// BACK
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			// LEFT
			-0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			// RIGHT
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			// TOP
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			 -0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			// BOTTOM
			-0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f, -0.5f,
		};

	float[] floorVertices = new float[] {
				-3.0f, 0.0f, 3.0f,
				 3.0f, 0.0f, 3.0f,
				-3.0f, 0.0f,-3.0f,
				 3.0f, 0.0f,-3.0f
			};

	
	FloatBuffer cubeBuff;
	FloatBuffer floorBuff;
	
	public GLTutorialEleven(Context c) {
		super(c, 20);

		GL10 gl = (GL10)glContext.getGL();
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepthf(1.0f);
		
		cubeBuff = makeFloatBuffer(box);
		floorBuff = makeFloatBuffer(floorVertices);
				
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		gl.glClearStencil(0);
		
		setFocusable(true);
		
	}
	
	float xrot = 0.0f;
	float yrot = 0.0f;
	
	protected void onDraw(Canvas canvas) {
		GL10 gl = (GL10)glContext.getGL();
		int w = getWidth();
		int h = getHeight();
		
		glContext.waitNative(canvas, this);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0,0,w,h);
		GLU.gluPerspective(gl, 45.0f, ((float)w)/h, 1f, 100f);
			
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 3, 7, 0, 0, 0, 0, 1, 0);
		

		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);
		
		gl.glEnable(GL10.GL_STENCIL_TEST);
		gl.glStencilOp(GL10.GL_REPLACE, GL10.GL_REPLACE, GL10.GL_REPLACE);
		gl.glStencilFunc(GL10.GL_ALWAYS, 1, 0xffffffff);
	
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floorBuff);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, white, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, white, 0);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glStencilFunc(GL10.GL_EQUAL, 1, 0xffffffff);
		gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);
		
		gl.glPushMatrix();
		gl.glScalef(1.0f, -1f, 1f);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		gl.glCullFace(GL10.GL_FRONT);
		drawCube(gl);
		gl.glCullFace(GL10.GL_BACK);
		gl.glPopMatrix();
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
		
		gl.glDisable(GL10.GL_STENCIL_TEST);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floorBuff);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, trans, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, trans, 0);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisable(GL10.GL_BLEND);
		
		drawCube(gl);
		
		xrot+=1.0f;
		yrot+=0.5f;
		
		glContext.waitGL();
	}
	
	protected void drawCube(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(0.0f,y,-1.0f);
		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);
	
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);

		gl.glColor4f(1.0f, 0, 0, 1.0f);
		gl.glNormal3f(0,0,1);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glNormal3f(0,0,-1);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
	
		gl.glColor4f(0, 1.0f, 0, 1.0f);
		gl.glNormal3f(-1,0,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glNormal3f(1,0,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		
		gl.glColor4f(0, 0, 1.0f, 1.0f);
		gl.glNormal3f(0,1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glNormal3f(0,-1,0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		gl.glPopMatrix();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
			y+=0.1;
			invalidate();
		}
		else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (y > 0.5)
				y-=0.1;
			invalidate();
		}	
		return super.onKeyDown(keyCode, event);
	}
}