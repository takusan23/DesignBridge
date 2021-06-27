package io.github.takusan23.designbridge.tool

import org.jsoup.nodes.Element

object GetElementValue {

    /** src属性かテキストかvalue属性を返す */
    fun getSrcOrTextOrValue(element: Element): String {
        return when {
            element.hasAttr("src") -> element.attr("src")
            element.hasAttr("value") -> element.attr("value")
            else -> element.text()
        }
    }

    /** onclickに指定したURLを取得する */
    fun getOnClickLocationURL(element: Element): String {
        return element.attr("onclick").replace("window.location=", "").replace("'", "")
    }

}