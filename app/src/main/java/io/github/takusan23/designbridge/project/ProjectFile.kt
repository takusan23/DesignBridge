package io.github.takusan23.designbridge.project

import android.content.Context
import android.os.FileObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

/**
 * プロジェクトを管理するクラス
 *
 * ViewModelからこのクラスの関数を呼んだりする
 * */
class ProjectFile(private val context: Context) {

    /** このフォルダの中にプロジェクトフォルダを作成していく。 */
    private val projectParentFolder = File(context.getExternalFilesDir(null), "project").apply {
        // 無いときは作成
        if (!exists()) {
            mkdir()
        }
    }

    /** プロジェクト一覧の変更を通知するFlow */
    val projectParentFolderFlow = callbackFlow<List<File>> {
        // ファイル変更コールバック
        val fileObserver = object : FileObserver(projectParentFolder.path) {
            override fun onEvent(event: Int, path: String?) {
                trySend(getProjectList())
            }
        }
        // 監視開始
        fileObserver.startWatching()
        // 初期値送信
        trySend(getProjectList())

        // Flowがキャンセルされたら
        awaitClose { fileObserver.stopWatching() }
    }

    /**
     * 新規プロジェクトを作成する関数
     *
     * @param projectName プロジェクト名
     * */
    fun createNewProject(projectName: String): File {
        val projectFolder = File(projectParentFolder, projectName)
        projectFolder.mkdir()
        return projectFolder
    }

    /**
     * プロジェクト一覧を返す
     *
     * 基本的にはflowでファイルの変更をキャッチできるはずなのでこの関数を使うことは多分無い
     * */
    fun getProjectList() = projectParentFolder.listFiles()?.toList() ?: listOf()

    /**
     * プロジェクト名のフォルダを取得する
     *
     * @param projectName プロジェクト名
     * */
    fun getProjectFolder(projectName: String) = File(projectParentFolder, projectName)

    /**
     * プロジェクトを削除する
     *
     * Kotlinなら中にファイルが入っていても削除ができる。便利
     *
     * @param projectName 削除するプロジェクト名
     * */
    fun deleteProject(projectName: String) = File(projectParentFolder, projectName).deleteRecursively()

}