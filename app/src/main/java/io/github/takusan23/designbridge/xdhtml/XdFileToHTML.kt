package io.github.takusan23.designbridge.xdhtml

import io.github.takusan23.designtalk.html.HtmlGenerator
import io.github.takusan23.designtalk.parse.ArtboardListParse
import io.github.takusan23.designtalk.parse.ManifestParse
import io.github.takusan23.designtalk.parse.ResourceParse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * xdファイルからHTMLを生成する。Web Exportプラグイン的な
 *
 * まあ再現度が低いのでおとなしくプラグイン使って書き出したほうがいいです。
 * */
object XdFileToHTML {

    /**
     * xdファイルを展開してHTMLを生成する。多分重いのでsuspend関数
     *
     * @param xdFileInputStream .xdファイルで開いたInputStream
     * @param tempFolderPath 一時的にフォルダが欲しいので。使い終わったら削除しますので
     * @param exportFolderPath 生成先フォルダ
     */
    suspend fun importXDFileToHTML(xdFileInputStream: InputStream, exportFolderPath: String, tempFolderPath: String) {
        // xdファイルオープン
        openXDFile(xdFileInputStream, tempFolderPath)
        /**
         * DesignTalkモジュールにHTML生成を頼む（マルチモジュールなプロジェクト）
         * designtalk モジュールでhtmlを生成してますのでそっちも参照
         * とりあえずアートボードとマニフェストからアートボードの名前を持ってくる
         * */
        val artboardFolderList = ArtboardListParse(tempFolderPath)
        val artboardList = ManifestParse.getArtboardList(tempFolderPath)

        // artboard一覧
        artboardFolderList.artboardList.forEach { artboard ->
            val children = artboard.children[0]
            // HTMLファイル作成
            val htmlFile = File(exportFolderPath, "${artboardList.find { it.path == "artboard-${children.id}" }?.name}.html").apply { createNewFile() }
            // HTML描画クラス
            val htmlGenerator = HtmlGenerator(tempFolderPath)
            // テキスト、図形等をxdファイルの情報から組み立てる
            children.artboard.children
                // img要素を最前面にしたいので配列の最後に持っていく cssの pointer-events: none でもいいけどめんどい
                // .sortedBy { artboardChildren -> artboardChildren.style?.fill?.pattern?.meta?.ux?.uid != null }
                .forEach { artboardChildren ->
                    htmlGenerator.drawHtml(artboardChildren)
                }
            // html保存
            htmlFile.writeText(htmlGenerator.getHtml())
        }
        // xdファイル内の画像をコピー
        ResourceParse(tempFolderPath).copyResource(exportFolderPath)
        // 一時的に生成したフォルダ削除。Kotlinなら一発
        File(tempFolderPath).deleteRecursively()
    }

    /**
     * xdファイルを展開する
     * @param xdFileInputStream .xdファイルで開いたInputStream
     * @param tempFolderPath 一時的にフォルダが欲しいので。使い終わったら削除しますので
     */
    private suspend fun openXDFile(xdFileInputStream: InputStream, tempFolderPath: String) = withContext(Dispatchers.IO) {
        // ひとまず.xdファイルの中身を見る。zipなのでそのまま使えます
        ZipInputStream(xdFileInputStream).also { zip ->
            var zipEntry: ZipEntry?
            // Zip内のファイルをなくなるまで繰り返す
            while (zip.nextEntry.also { zipEntry = it } != null) {
                if (zipEntry != null) {
                    // コピー先ファイル作成
                    if (zipEntry!!.isDirectory) {
                        File(tempFolderPath, zipEntry!!.name).mkdirs()
                    }
                    val copyFile = File(tempFolderPath, zipEntry!!.name)
                    // 親フォルダがない場合は作成
                    copyFile.parentFile?.mkdirs()
                    copyFile.createNewFile()
                    // データを書き込む
                    copyFile.writeBytes(zip.readBytes())
                }
            }
        }
    }

}