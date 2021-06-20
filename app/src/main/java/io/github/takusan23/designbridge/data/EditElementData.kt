package io.github.takusan23.designbridge.data

/**
 * 編集するHTML要素のデータが入ってるデータクラス
 *
 * @param elementId 要素のID
 * @param elementTagName 要素のタグ。span、imgなど
 * @param elementTextContent テキスト
 * */
data class EditElementData(
    val elementId: String,
    var elementTagName: String,
    var elementTextContent: String,
    var elementAttrSrc: String?,
    var elementAttrValue: String?
)