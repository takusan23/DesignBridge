package io.github.takusan23.designbridge.project

import android.content.Context
import android.net.Uri
import android.os.FileObserver
import io.github.takusan23.designbridge.tool.FileTool
import io.github.takusan23.designbridge.xdhtml.XdFileToHTML
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

/**
 * プロジェクト内のファイルを操作したりするクラス
 * @param context Context
 * @param projectName プロジェクト名
 * */
class ProjectDetail(private val context: Context, val projectName: String) {

    private val projectFile = ProjectFile(context)

    private val projectFolder = projectFile.getProjectFolder(projectName)

    /** プロジェクトのフォルダの変更を通知するFlow */
    val projectFolderItemFlow = callbackFlow<List<File>> {
        // ファイル変更コールバック
        val fileObserver = object : FileObserver(projectFolder.path) {
            override fun onEvent(event: Int, path: String?) {
                trySend(getProjectItemList())
            }
        }
        // 監視開始
        fileObserver.startWatching()
        // 初期値送信
        trySend(getProjectItemList())

        // Flowがキャンセルされたら
        awaitClose { fileObserver.stopWatching() }
    }

    /**
     * プロジェクト内にアイテムを追加する
     *
     * （アプリ内ストレージにファイルをコピーする）
     *
     * @param uri StorageAccessFramework等でファイルを選択するとUriがもらえるのでそれ
     * */
    fun addFileFromUri(uri: Uri) {
        FileTool.uriFileCopy(context, uri, projectFolder.path)
    }

    /**
     * プロジェクト内のファイルを返す。ついでにsortedByDescendingでhtmlファイルが上に来るように
     *
     * 基本的には変更を検知してFlowで送信するので
     * */
    fun getProjectItemList() = projectFolder.listFiles()?.toList()?.sortedByDescending { file -> file.extension == "html" } ?: listOf()

    /**
     * プロジェクト内のファイルを削除する
     *
     * @param fileName ファイル名
     * */
    fun deleteFile(fileName: String) = File(projectFolder, fileName).deleteRecursively()

    /**
     * プロジェクト内の画像ファイルを返す
     * @return png / jpg / gif の画像ファイルたち
     * */
    fun getProjectImageList() = getProjectItemList().filter { file -> file.extension == "png" || file.extension == "jpg" || file.extension == "gif" }

    /**
     * プロジェクト内の動画ファイルを返す
     * @return mp4ファイルたち
     * */
    fun getProjectVideoList() = getProjectItemList().filter { file -> file.extension == "mp4" }

    /**
     * プロジェクト内のHTMLファイルを返す
     * @return htmlファイル
     * */
    fun getProjectHtmlList() = getProjectItemList().filter { file -> file.extension == "html" }

    /**
     * xdファイルを取り込む。多分重いのでsuspend関数
     *
     * @param uri StorageAccessFramework等でファイルを選択するとUriがもらえるのでそれ
     * */
    suspend fun importXDFile(uri: Uri) {
        // xdファイルを展開するので一時的にフォルダを作成
        val xdFileOpenFolder = File(projectFolder, "xd_open").apply { mkdir() }
        val contentResolver = context.contentResolver
        val xdFileInputStream = contentResolver.openInputStream(uri)!!
        // XdFileToHTML 参照
        XdFileToHTML.importXDFileToHTML(xdFileInputStream, projectFolder.path, xdFileOpenFolder.path)
    }

}