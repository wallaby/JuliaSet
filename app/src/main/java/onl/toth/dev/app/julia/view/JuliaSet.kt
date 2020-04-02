package onl.toth.dev.app.julia.view

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class JuliaSet(vertexShaderCode: String, fragmentShaderCode: String) {


    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val mProgram: Int
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0

    // number of coordinates per vertex.shader in this array
    val COORDS_PER_VERTEX = 3

    var squareCoords = floatArrayOf(
        -1.0f, 1.0f, 0.0f,  // top left
        -1.0f, -1.0f, 0.0f,  // bottom left
        1.0f, -1.0f, 0.0f,  // bottom right
        1.0f, 1.0f, 0.0f
    ) // top right


    private val drawOrder =
        shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices


    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex.shader


    init {

        // initialize vertex.shader byte buffer for shape coordinates
        vertexBuffer =
            // (# of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(squareCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(squareCoords)
                    position(0)
                }
            }

        // initialize byte buffer for the draw list
        drawListBuffer =
            // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }

        // prepare shaders and OpenGL program
        val vertexShader: Int = loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )
        val fragmentShader: Int = loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )
        mProgram = GLES20.glCreateProgram() // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader) // add the vertex.shader shader to program
        GLES20.glAttachShader(mProgram, fragmentShader) // add the fragment shader to program
        GLES20.glLinkProgram(mProgram) // create OpenGL program executables
        GLES20.glFrontFace(GLES20.GL_CCW)
    }


    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    fun draw(mvpMatrix: FloatArray?) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex.shader shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer
        )

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        // Disable vertex.shader array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}