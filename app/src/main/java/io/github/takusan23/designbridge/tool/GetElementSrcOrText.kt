package io.github.takusan23.designbridge.tool

import org.jsoup.nodes.Element

object GetElementSrcOrText {

    /** src属性かテキストかvalue属性を返す */
    fun getSrcOrTextOrValue(element: Element): String {
        return when {
            element.hasAttr("src") -> element.attr("src")
            element.hasAttr("value") -> element.attr("value")
            else -> element.text()
        }
    }

}