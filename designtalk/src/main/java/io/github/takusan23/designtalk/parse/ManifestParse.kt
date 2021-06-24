package io.github.takusan23.designtalk.parse

import io.github.takusan23.designtalk.json.manifest.Manifest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * manifestをパースする
 *
 * @param xdFilePath .xdファイルのパス
 * */
class ManifestParse(xdFilePath: String) {

    private val manifest = Json { ignoreUnknownKeys = true }.decodeFromString<Manifest>(File(xdFilePath, "manifest").readText())

    /** アートボードの名前配列を返す */
    val artboardNameList = manifest.children
        .find { manifestChildren -> manifestChildren.name == "artwork" }!!.children!!
        .filter { manifestChildren -> manifestChildren.uxDesignBonds != null }

}