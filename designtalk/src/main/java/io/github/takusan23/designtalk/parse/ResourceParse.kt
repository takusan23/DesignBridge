package io.github.takusan23.designtalk.parse

import java.io.ByteArrayInputStream
import java.io.File
import java.net.URLConnection

/** xdファイル内から画像を取り出す */
class ResourceParse(xdFilePath: String) {

    /** resources格納パス */
    private val file = File("$xdFilePath/resources")

    /** ファイルパス配列を返す */
    val resFileList = file.listFiles()?.filter { file -> file.isFile }

    /**
     * xdファイル内の画像をコピーする
     * @param copyFolder コピー先
     * */
    fun copyResource(copyFolder: String) {
        resFileList?.forEach { file ->
            // 拡張子取る
            val extension = when (URLConnection.guessContentTypeFromStream(ByteArrayInputStream(file.readBytes()))) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                else -> ""
            }
            File(copyFolder, "${file.name}").apply {
                createNewFile()
                writeBytes(file.readBytes())
            }
        }
    }

}