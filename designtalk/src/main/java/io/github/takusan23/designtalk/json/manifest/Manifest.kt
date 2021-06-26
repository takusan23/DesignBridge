package io.github.takusan23.designtalk.json.manifest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * .xdファイル内にある、manifestファイルを読み出す。
 *
 * 最低限名前とアートボードだけ取り出せばええやろ
 * */
@Serializable
data class Manifest(
    val name: String,
    val id: String,
    @SerialName("children") val children: List<ManifestChildren>,
)

/**
 * manifestのchildren[]
 *
 * アートボード配列だと思う
 *
 * 再帰的に使う
 * */
@Serializable
data class ManifestChildren(
    val id: String,
    val name: String,
    val path: String,
    // artboardのみある
    @SerialName("uxdesign#bounds") val uxDesignBonds: uxDesignBonds? = null,
    @SerialName("children") val children: List<ManifestChildren>? = null,
    val components: List<ManifestChildrenComponent>? = null,
)

@Serializable
data class ManifestChildrenComponent(
    val id: String,
    val name: String,
    val path: String,
    @SerialName("type") val mimeType: String,
)

/**
 * アートボードの大きさなど。
 * */
@Serializable
data class uxDesignBonds(
    val width: Int,
    val height: Int,
)