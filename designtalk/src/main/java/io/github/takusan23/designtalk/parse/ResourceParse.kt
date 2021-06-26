package io.github.takusan23.designtalk.parse

import java.io.ByteArrayInputStream
import java.io.File
import java.net.URLConnection

/** xdファイル内から画像を取り出す */
class ResourceParse(private val xdFilePath: String) {

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
            val extension = ManifestParse.getResourceMimeType(xdFilePath, file.name)
            File(copyFolder, "${file.name}.$extension").apply {
                createNewFile()
                writeBytes(file.readBytes())
            }
        }
    }

}