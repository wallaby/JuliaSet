package onl.toth.dev.app.julia.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet


class JuliaSurfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : GLSurfaceView(context, attrs) {
    init {
        setEGLContextClientVersion(2)
    }
}