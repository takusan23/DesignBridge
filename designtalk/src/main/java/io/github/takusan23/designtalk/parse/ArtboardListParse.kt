package io.github.takusan23.designtalk.parse

import io.github.takusan23.designtalk.json.graphiccontent.GraphicContentRoot
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * アートボード一覧を返すなど
 *
 * @param xdFilePath .xdファイルのパス
 * */
class ArtboardListParse(xdFilePath: String) {

    /** artboard格納パス */
    private val file = File("$xdFilePath/artwork")

    /** artboard-から始まるフォルダを取り出す */
    val artboardList = file
        .listFiles()!!
        .filter { file -> file.name.startsWith("artboard-") } // アートボードのみ
        .map { file -> File(file, "graphics/graphicContent.agc") }
        .map { file -> Json { ignoreUnknownKeys = true }.decodeFromString<GraphicContentRoot>(file.readText()) }

}