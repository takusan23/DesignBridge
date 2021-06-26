package io.github.takusan23.designtalk.parse

import io.github.takusan23.designtalk.json.graphiccontent.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File

/**
 * アートボード一覧を返すなど
 *
 * @param xdFilePath .xdファイルのパス
 * */
class ArtboardListParse(xdFilePath: String) {

    /** artboard格納パス */
    private val file = File("$xdFilePath/artwork")

    /** shapeの派生。type:"rect"用のデータクラスや、type:"line"用のデータクラスがある。 */
    private val module = SerializersModule {
        polymorphic(ArtboardChildrenSharp::class) {
            subclass(ArtboardChildrenSharpPath::class)
            subclass(ArtboardChildrenSharpLine::class)
            subclass(ArtboardChildrenSharpCircle::class)
            subclass(ArtboardChildrenSharpRect::class)
        }
    }


    /** artboard-から始まるフォルダを取り出す */
    val artboardList = file
        .listFiles()!!
        .filter { file -> file.name.startsWith("artboard-") } // アートボードのみ
        .map { file -> File(file, "graphics/graphicContent.agc") }
        .map { file ->
            Json {
                ignoreUnknownKeys = true
                serializersModule = module
            }.decodeFromString<GraphicContentRoot>(file.readText())
        }

}