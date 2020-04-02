package onl.toth.dev.app.julia

import android.content.Context
import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import onl.toth.dev.app.julia.util.FileUtil
import onl.toth.dev.app.julia.view.JuliaRenderer
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private val LOGGER = LoggerFactory.getLogger(MainActivity::class.java)

    private lateinit var juliaSurfaceView: GLSurfaceView
    private lateinit var juliaRenderer: JuliaRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        juliaRenderer = JuliaRenderer()
        juliaSurfaceView = findViewById(R.id.juliaSurfaceView)
        initView()
    }

    private fun initView() {
        val vertexShaderCode = readRawTextFile(this, R.raw.vertex)
        val fragmentShaderCode = readRawTextFile(this, R.raw.fragment)
        juliaRenderer = JuliaRenderer(vertexShaderCode!!, fragmentShaderCode!!)
//        mDetector = ScaleGestureDetector(getContext(), ScaleListener())
        juliaSurfaceView.setRenderer(juliaRenderer)
        juliaSurfaceView.renderMode = RENDERMODE_WHEN_DIRTY
    }

    /**
     * Read a raw text file and return its content
     */
    fun readRawTextFile(context: Context, resId: Int): String? {
        val inputStream: InputStream
        val result: String
        try {
            inputStream = context.resources.openRawResource(resId)
            result = FileUtil.getUTF8FileData(inputStream)!!
            inputStream.close()
        } catch (e: IOException) {
            LOGGER.error("Reading raw file error", e)
            return null
        }
        return result
    }
}