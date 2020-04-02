package onl.toth.dev.app.julia.view

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import org.slf4j.LoggerFactory
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


private val LOGGER = LoggerFactory.getLogger(JuliaRenderer::class.java)

class JuliaRenderer(private val vertexShaderCode: String, private val fragmentShaderCode: String)
    : GLSurfaceView.Renderer {

    private var mHeight = 0
    private var mWidth = 0

    //Store all values as doubles, and truncate for use as floats.
    private var mRatio = 0.0
    private var mY = 1.0
    private var mX = 1.0
    private var mZoom = 0.5

    private lateinit var fractal: JuliaSet


    override fun onSurfaceCreated(
        unused: GL10?,
        config: EGLConfig?
    ) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        fractal = JuliaSet(vertexShaderCode, fragmentShaderCode)
    }

    override fun onDrawFrame(unused: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        val mMVPMatrix = floatArrayOf(
            3.0f/mWidth, 0.0f, 0.0f, 0.0f,
            0.0f, 3.0f/mWidth, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            -2.0f, -1.5f*mHeight/mWidth, 0.0f, 1.0f
        )
        fractal.draw(mMVPMatrix)
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        mWidth = width
        mHeight = height

        //Set viewport to fullscreen
        GLES20.glViewport(0, 0, width, height)
        mRatio = width.toDouble() / height
    }
}

/**
 * Utility method from Android Tutorials for compiling a OpenGL shader.
 *
 *
 * **Note:** When developing shaders, use the checkGlError()
 * method to debug shader coding errors.
 *
 * @param type - Vertex or fragment shader type.
 * @param shaderCode - String containing the shader code.
 * @return - Returns an id for the shader.
 */
fun loadShader(type: Int, shaderCode: String?): Int {
    val shader = GLES20.glCreateShader(type)

    // add the source code to the shader and compile it
    GLES20.glShaderSource(shader, shaderCode)
    GLES20.glCompileShader(shader)
    checkGlError("glCompileShader")
    return shader
}

/**
 * Utility method from Android Tutorials for debugging OpenGL calls. Provide the name of the call
 * just after making it:
 *
 * <pre>
 * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
 * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
 *
 * If the operation is not successful, the check throws an error.
 *
 * @param glOperation - Name of the OpenGL call to check.
 */
fun checkGlError(glOperation: String) {
    var error: Int
    while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
        LOGGER.error("$glOperation: glError $error")
        throw RuntimeException("$glOperation: glError $error")
    }
}
