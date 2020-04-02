package onl.toth.dev.app.julia.util

import android.annotation.SuppressLint
import android.content.res.Resources.NotFoundException
import android.util.Base64
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

object FileUtil {
    private val LOGGER = LoggerFactory.getLogger(FileUtil::class.java)
    private const val UTF_8 = "UTF-8"

    @JvmStatic
    fun getBase64FileData(inputStream: InputStream): String? {
        val data = getFileData(inputStream)
        return if (data.isNotEmpty()) {
            Base64.encodeToString(data, Base64.NO_WRAP)
        } else null
    }

    @JvmStatic
    fun getUTF8FileData(inputStream: InputStream): String? {
        return getFileData(inputStream, Charset.forName(UTF_8))
    }

    private fun getFileData(inputStream: InputStream, charsetName: Charset): String? {
        val data = getFileData(inputStream)
        if (data.isNotEmpty()) {
            try {
                return String(data, charsetName)
            } catch (e: UnsupportedEncodingException) {
                LOGGER.error("encoding not supported for file", e)
            }
        }
        return null
    }

    @SuppressLint("ResourceType")
    private fun getFileData(inputStream: InputStream): ByteArray {
        try {
            inputStream.use { stream ->
                val size = stream.available()
                val buffer = ByteArray(size)
                stream.read(buffer) //NOSONAR
                return buffer
            }
        } catch (e: IOException) {
            LOGGER.error("error loading data from font", e)
            return ByteArray(0)
        } catch (e: NotFoundException) {
            LOGGER.error("error loading data from font", e)
            return ByteArray(0)
        }
    }
}