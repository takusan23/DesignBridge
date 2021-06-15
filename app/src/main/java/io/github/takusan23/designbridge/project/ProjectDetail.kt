package io.github.takusan23.designbridge.project

import android.content.Context
import android.net.Uri
import android.os.FileObserver
import io.github.takusan23.designbridge.tool.FileTool
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
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        // Uriからファイル名を出す
        val fileName = FileTool.getFileNameFromUri(context, uri)
        // 保存先
        val copyFile = File(projectFolder, fileName)
        copyFile.createNewFile()
        val outputStream = copyFile.outputStream()
        // 書き込む
        inputStream?.copyTo(outputStream)
        // リソース開放
        inputStream?.close()
        outputStream.close()
    }

    /**
     * プロジェクト内のファイルを返す
     *
     * 基本的には変更を検知してFlowで送信するので
     * */
    fun getProjectItemList() = projectFolder.listFiles()?.toList() ?: listOf()

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

}