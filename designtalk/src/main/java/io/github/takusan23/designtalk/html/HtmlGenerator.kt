package io.github.takusan23.designtalk.html

import io.github.takusan23.designtalk.json.graphiccontent.*
import io.github.takusan23.designtalk.parse.ManifestParse
import org.jsoup.nodes.Document
import kotlin.math.min

/**
 * HTML生成クラス
 *
 * @param xdOpenFolderPath xdファイルを展開したフォルダのパス
 * */
class HtmlGenerator(private val xdOpenFolderPath: String) {

    private val bodyCSS = generateCSS(mapOf())

    /** HTML。この中に記述していく */
    private var document = Document.createShell("/").apply {
        head().appendElement("meta")
            .attr("name", "viewport")
            .attr("content", "width=device-width,initial-scale=1")
        body().attr("style", bodyCSS)
    }

    /**
     * ArtboardChildrenをもとにHTMLを書いていく
     *
     * @param artboardChildren 文字とかSVGとか
     * @param elementId 要素のID。指定した場合指定した要素の子として追加します
     * */
    fun drawHtml(artboardChildren: ArtboardChildren, elementId: String? = null) {
        // 要素作成
        val parentElement = if (elementId != null) {
            document.getElementById(elementId)
        } else {
            document.body()
                .appendElement("div")
                .attr("id", artboardChildren.name)
                .attr("xd_name", artboardChildren.name)
                .attr(
                    "style", when (artboardChildren.type) {
                        "text" -> generateSpanCSS(artboardChildren)
                        else -> generateDivCSS(artboardChildren)
                    }
                )
        }


        parentElement.apply {
            // 中身
            when (artboardChildren.type) {
                // 文字
                "text" -> {
                    // aタグでspanを包む
                    if (artboardChildren.text != null) {
                        appendElement("span")
                            .attr("id", artboardChildren.id)
                            .text(artboardChildren.text.rawText)
                    }
                }
                // グループ
                "group" -> {
                    // 子要素を書く
                    artboardChildren.group!!.children
                        .filter { artboardChildren -> artboardChildren.id != null }
                        .forEach { children ->
                            // Boundaryはいらん
                            if (children.name != "Boundary") {
                                val parentId = parentElement
                                    .appendElement("div")
                                    .attr("style", generateGroupDivCSS(children))
                                    .attr("xd_name", children.name)
                                    .attr("dev_xd_type", children::class.java.name)
                                    .attr("id", children.id)
                                    .id()
                                drawHtml(children, parentId)
                            }
                        }
                }
                // 図形
                "shape" -> {
                    // 再帰的に呼ぶ
                    when (artboardChildren.shape) {
                        is ArtboardChildrenSharpPath -> {
                            // svg
                            appendElement("svg")
                                .attr("style", generateSvgCSS(artboardChildren))
                                // svgタグ内にpathタグを入れる
                                .appendElement("path")
                                .attr("d", artboardChildren.shape.path)
                        }
                        is ArtboardChildrenSharpLine -> {
                            // 線
                            val x1 = artboardChildren.shape.x1!!.toString()
                            val y1 = artboardChildren.shape.x2!!.toString()
                            val x2 = artboardChildren.shape.y1!!.toString()
                            val y2 = artboardChildren.shape.y2!!.toString()
                            appendElement("svg")
                                .attr("style", generateSvgCSS(artboardChildren))
                                // svgタグ内にpathタグを入れる
                                .appendElement("path")
                                .attr("d", "M $x1 $x2 L $y1 $y2")
                        }
                        is ArtboardChildrenSharpCircle -> {
                            // 円
                            val rx = artboardChildren.shape.cx!!
                            val ry = artboardChildren.shape.cy!!
                            appendElement("svg")
                                .attr("style", generateSvgCSS(artboardChildren))
                                .appendElement("ellipse")
                                .attr("rx", rx.toString())
                                .attr("ry", ry.toString())
                                .attr("cx", rx.toString())
                                .attr("cy", ry.toString())
                        }
                        is ArtboardChildrenSharpRect -> {
                            // 四角形。その他画像とかもこれ
                            val width = artboardChildren.shape.width!!.toString()
                            val height = artboardChildren.shape.height!!.toString()
                            // 画像じゃなくて四角形の描画かも
                            if (artboardChildren.style?.fill?.pattern?.meta?.ux?.uid != null) {
                                // 画像
                                val src = artboardChildren.style.fill.pattern.meta.ux.uid
                                val imgExtension = ManifestParse.getResourceMimeType(xdOpenFolderPath, src)
                                // aタグでimgを包む
                                appendElement("img")
                                    .attr("onclick", "")
                                    .attr(
                                        "style", generateCSS(
                                            mapOf(
                                                "width" to width,
                                                "height" to height,
                                                // 以下2つは上に要素が重なったときでもonclickを拾うために
                                                "position" to "absolute",
                                                "z-index" to "1"
                                            )
                                        )
                                    )
                                    .attr("id", artboardChildren.id)
                                    .attr("src", "${artboardChildren.style.fill.pattern.meta.ux.uid}.$imgExtension")
                            } else {
                                // 角丸処理
                                var rx = 0f
                                if (artboardChildren.shape.r != null) {
                                    // width、height以上の角丸の値を許さない
                                    val min = min(width.toFloat() / 2, height.toFloat() / 2)
                                    val round = artboardChildren.shape.r?.get(0) ?: 0f
                                    rx = min(round, min)
                                }
                                // 四角形
                                appendElement("svg")
                                    .attr("style", generateSvgCSS(artboardChildren))
                                    .appendElement("rect")
                                    .attr("width", width)
                                    .attr("height", height)
                                    .attr("rx", rx.toString())
                                    .attr("ry", rx.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    /** 生成したHTMLを文字列で返す */
    fun getHtml() = document.html()

    /** MapからCSS生成 */
    private fun generateCSS(map: Map<String, String>): String {
        return map.toList().map { pair -> "${pair.first}: ${pair.second}" }.joinToString(separator = ";")
    }

    /** spanタグのCSS生成 */
    private fun generateSpanCSS(artboardChildren: ArtboardChildren): String {
        val left = artboardChildren.meta?.ux?.localTransform?.tx
        val fontSize = artboardChildren.style?.font?.size ?: 0
        val top = (artboardChildren.meta?.ux?.localTransform?.ty!!) - fontSize
        val color = generateFillRGBA(artboardChildren)
        val styleMap = mutableMapOf(
            "left" to left.toString(),
            "top" to top.toString(),
            "position" to "absolute",
            "font-size" to "$fontSize",
            "color" to color,
        )
        return generateCSS(styleMap)
    }

    /** svgタグ用のCSS生成。位置などは親要素がやるので色の指定とか */
    private fun generateSvgCSS(artboardChildren: ArtboardChildren): String {
        val styleMap = mutableMapOf<String, String>()
        // 図形の色
        styleMap["fill"] = if (artboardChildren.style?.fill?.type == "solid") generateFillRGBA(artboardChildren) else "transparent"
        // 線の色
        styleMap["stroke"] = if (artboardChildren.style?.stroke?.type == "solid") generateStrokeRGBA(artboardChildren) else "transparent"
        // 透明度
        styleMap["opacity"] = artboardChildren.style?.opacity?.toString() ?: "1"
        // rectの描画は多分width/heightの指定がいる
        if (artboardChildren.shape is ArtboardChildrenSharpRect) {
            styleMap["height"] = artboardChildren.shape.height?.toString() ?: "initial"
            styleMap["width"] = artboardChildren.shape.width?.toString() ?: "initial"
        }
        return generateCSS(styleMap)
    }

    /** fill: rgba(255,255,255,0)を作る */
    private fun generateFillRGBA(artboardChildren: ArtboardChildren): String {
        // 色取得
        val colorR = artboardChildren.style?.fill?.color?.value?.r ?: 0
        val colorG = artboardChildren.style?.fill?.color?.value?.g ?: 0
        val colorB = artboardChildren.style?.fill?.color?.value?.b ?: 0
        val opacity = artboardChildren.style?.fill?.color?.alpha ?: 1f
        return "rgba($colorR,$colorG,$colorB,$opacity)"
    }

    /** stroke: rgba(255,255,255,0)を作る */
    private fun generateStrokeRGBA(artboardChildren: ArtboardChildren): String {
        // 色取得
        val colorR = artboardChildren.style?.stroke?.color?.value?.r ?: 0
        val colorG = artboardChildren.style?.stroke?.color?.value?.g ?: 0
        val colorB = artboardChildren.style?.stroke?.color?.value?.b ?: 0
        val opacity = artboardChildren.style?.stroke?.color?.alpha ?: 1f
        return "rgba($colorR,$colorG,$colorB,$opacity)"
    }

    /** divタグ用のCSS生成 */
    private fun generateDivCSS(artboardChildren: ArtboardChildren): String {
        val rgba = generateFillRGBA(artboardChildren)
        val styleMap = mutableMapOf(
            "left" to (artboardChildren.meta?.ux?.localTransform?.tx ?: artboardChildren.transform?.tx).toString(),
            "top" to (artboardChildren.meta?.ux?.localTransform?.ty ?: artboardChildren.transform?.ty).toString(),
            "position" to "absolute",
            "color" to rgba,
        )
        return generateCSS(styleMap)
    }

    /** type:"group"用のDivのCSSせいせい */
    private fun generateGroupDivCSS(artboardChildren: ArtboardChildren): String {
        val rgba = generateFillRGBA(artboardChildren)
        val styleMap = mutableMapOf(
            "left" to (artboardChildren.meta?.ux?.localTransform?.tx ?: artboardChildren.transform?.tx).toString(),
            "top" to (artboardChildren.meta?.ux?.localTransform?.ty ?: artboardChildren.transform?.ty).toString(),
            "position" to "absolute",
            "color" to rgba,
        )
        return generateCSS(styleMap)
    }

}