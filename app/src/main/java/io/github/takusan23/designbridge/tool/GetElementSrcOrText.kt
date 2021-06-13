package io.github.takusan23.designbridge.tool

import org.jsoup.nodes.Element

object GetElementSrcOrText {

    /** src属性かテキストを返す */
    fun getSrcOrText(element: Element): String {
        return if (element.hasAttr("src")) element.attr("src") else element.text()
    }

}