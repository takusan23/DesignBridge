package io.github.takusan23.designbridge.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import io.github.takusan23.designbridge.project.ProjectDetail

/**
 * プロジェクトの中身を表示する画面のViewModel
 * */
class ProjectDetailViewModel(application: Application, val projectName: String) : AndroidViewModel(application) {

    private val context = application.applicationContext

    /** プロジェクトフォルダーを扱うクラス。Fileのらっぱー */
    private val projectFolder = ProjectDetail(context, projectName)

    /** プロジェクトのフォルダの変更を通知するFlow */
    val projectFolderItemFlow = projectFolder.projectFolderItemFlow

    /**
     * プロジェクト内にアイテムを追加する
     *
     * @param uri StorageAccessFramework等でファイルを選択するとUriがもらえるのでそれ
     * */
    fun addFileFromUri(uri: Uri) = projectFolder.addFileFromUri(uri)

    /**
     * プロジェクト内のファイルを削除する
     *
     * @param fileName ファイル名
     * */
    fun deleteFile(fileName: String) = projectFolder.deleteFile(fileName)

}