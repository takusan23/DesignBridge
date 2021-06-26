package io.github.takusan23.designtalk.parse

import io.github.takusan23.designtalk.json.manifest.Manifest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

/** xdファイルの情報などなど */
object ManifestParse {

    /**
     * artboard内のリソース、なぜか拡張子を消し飛ばしているので拡張子を取得する。
     * @return pngかjpg。それ以外は空文字列
     * */
    fun getResourceMimeType(xdOpenFolderPath: String, uid: String): String {
        val manifest = Json { ignoreUnknownKeys = true }.decodeFromString<Manifest>(File(xdOpenFolderPath, "manifest").readText())
        // contentTypeとmimeTypeって違うの？
        val mimeType = manifest.children
            .find { manifestChildren -> manifestChildren.name == "resources" }!!
            .components!!
            .find { manifestChildrenComponent -> manifestChildrenComponent.path == uid }!!
            .mimeType
        return when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            else -> ""
        }
    }

    /** アートボードの配列を返す */
    fun getArtboardList(xdOpenFolderPath: String) =
        Json { ignoreUnknownKeys = true }
            .decodeFromString<Manifest>(File(xdOpenFolderPath, "manifest").readText())
            .children
            .find { manifestChildren -> manifestChildren.name == "artwork" }!!.children!!
            .filter { manifestChildren -> manifestChildren.uxDesignBonds != null }
}