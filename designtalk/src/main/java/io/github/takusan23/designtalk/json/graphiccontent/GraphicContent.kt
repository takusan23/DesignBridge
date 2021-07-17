package io.github.takusan23.designtalk.json.graphiccontent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * artwork/artboard-なんとかのファイルをパースする。中身はJSON
 * */
@Serializable
data class GraphicContentRoot(
    val children: List<GraphicContentRootChildren>,
)

/**
 * graphicContentの中のchildren[]の中にあるやつ。
 * @param id [io.github.takusan23.designtalk.json.manifest.ManifestChildren.path]と同じ値
 * */
@Serializable
data class GraphicContentRootChildren(
    val id: String? = null,
    val artboard: Artboard,
)

/**
 * [GraphicContentRootChildren]の中にアートボードのテキストの情報とかがあるので
 * */
@Serializable
data class Artboard(
    val children: List<ArtboardChildren>,
)

/**
 * "type": "text",
 * "name": "ClickManaita 1.17",
 * "meta": { },
 * "id": "ff",
 * "transform": { }
 *
 * みたいなのをパースする
 * */
@Serializable
data class ArtboardChildren(
    val type: String,
    val name: String? = null,
    val id: String,
    val transform: ArtboardChildrenTransform? = null,
    val meta: ArtboardChildMeta? = null,
    val style: ArtboardChildStyle? = null,
    val text: ArtboardChildText? = null,
    val shape: ArtboardChildrenSharp? = null,
    @SerialName("group") val group: Artboard? = null,
)

/** テキストの位置など、なぜか浮動小数点 */
@Serializable
data class ArtboardChildrenTransform(
    val tx: Float,
    val ty: Float,
)

@Serializable
abstract class ArtboardChildrenSharp {
    @SerialName("type")
    abstract val _type: String?
}

@Serializable
@SerialName("path")
data class ArtboardChildrenSharpPath(
    override val _type: String? = null,
    val path: String? = null,
) : ArtboardChildrenSharp()

@Serializable
@SerialName("line")
data class ArtboardChildrenSharpLine(
    override val _type: String? = null,
    // type: "line" のみ ---
    val x1: Float? = null,
    val y1: Float? = null,
    val x2: Float? = null,
    val y2: Float? = null,
) : ArtboardChildrenSharp()

@Serializable
@SerialName("circle")
data class ArtboardChildrenSharpCircle(
    override val _type: String? = null,
    // type: "circle" のみ ---
    val cx: Float? = null,
    val cy: Float? = null,
) : ArtboardChildrenSharp()

/** 未実装 */
@Serializable
@SerialName("ellipse")
data class ArtboardChildrenSharpEclipse(
    override val _type: String? = null,
    // type: "circle" のみ ---
    val cx: Float? = null,
    val cy: Float? = null,
    val rx: Float? = null,
    val ry: Float? = null,
) : ArtboardChildrenSharp()

@Serializable
@SerialName("rect")
data class ArtboardChildrenSharpRect(
    override val _type: String? = null,
    // type: "rect" のみ ---
    val width: Float? = null,
    val height: Float? = null,
    val r: List<Float>? = null,
) : ArtboardChildrenSharp()

/** 未実装 */
@Serializable
@SerialName("polygon")
data class ArtboardChildrenSharpPolygon(
    override val _type: String? = null,
) : ArtboardChildrenSharp()

@Serializable
data class ArtboardChildMeta(
    val ux: ArtboardChildUx,
)

@Serializable
data class ArtboardChildUx(
    val localTransform: ArtboardChildrenTransform? = null,
)

@Serializable
data class ArtboardChildStyle(
    // type: "text" のときのみ
    val font: ArtboardChildStyleFont? = null,
    val fill: ArtboardChildFill? = null,
    val stroke: ArtboardChildStroke? = null,
    val opacity: Float? = null,
)

@Serializable
data class ArtboardChildStroke(
    val type: String,
    // "type": "solid" のときのみ ---
    val color: ArtboardChildColor? = null,
    // "type": "pattern" のときのみ ---
    val pattern: ArtboardChildPattern? = null,
)

@Serializable
data class ArtboardChildFill(
    val type: String,
    // "type": "solid" のときのみ ---
    val color: ArtboardChildColor? = null,
    // "type": "pattern" のときのみ ---
    val pattern: ArtboardChildPattern? = null,
    val alpha: Float? = null,
)

@Serializable
data class ArtboardChildPattern(
    val href: String,
    val meta: ArtboardChildPatternMeta,
)

@Serializable
data class ArtboardChildPatternMeta(
    val ux: ArtboardChildPatternMetaUx,
)

@Serializable
data class ArtboardChildPatternMetaUx(
    val uid: String,
)

@Serializable
data class ArtboardChildColor(
    val mode: String,
    val value: ArtboardChildColorValue,
    val alpha: Float? = null,
)

@Serializable
data class ArtboardChildColorValue(
    val r: Int,
    val g: Int,
    val b: Int,
)

@Serializable
data class ArtboardChildStyleFont(
    val postscriptName: String,
    val family: String,
    val style: String,
    val size: Int,
)

@Serializable
data class ArtboardChildText(
    val rawText: String,
)