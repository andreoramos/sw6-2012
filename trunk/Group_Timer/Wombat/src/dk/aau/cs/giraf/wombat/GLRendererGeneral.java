package dk.aau.cs.giraf.wombat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.SystemClock;

public class GLRendererGeneral implements Renderer{

	private GLCube timet;
	
	public GLRendererGeneral(){
		timet = new GLCube();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig eglconfig) {
		// Her s�tter man kameraet og alt s�dan noget. onCreate agtigt...
		gl.glDisable(GL10.GL_DITHER); //speed-up performance
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST); //Speed-up performance
		gl.glClearColor(.4f, 0f, .2f, 1f); // s�tter baggrundsfarven
		gl.glClearDepthf(1f); // s�tter dybden
	}
	
	public void onDrawFrame(GL10 gl) {
		// tegne-tools
		gl.glDisable(GL10.GL_DITHER); //speed-up performance
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); //tegner farve og dybde
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, -5, 0, 0, 0, 0, 2, 0); // hvor langt er vi far objektet og hvor kigger vi hen
		
		//Rotate the element
		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = .090f * ((int)time);
		
		gl.glRotatef(angle, 1, 0, 2);
		
		timet.draw(gl);

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// h�ndterer skift i f.eks. landscape/portrait mode
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width/height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 25); //fort�ller fra hvilken afstand trekanten er synlig
	}
}
