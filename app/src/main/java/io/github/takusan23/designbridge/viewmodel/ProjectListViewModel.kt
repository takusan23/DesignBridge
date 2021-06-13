package io.github.takusan23.designbridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.github.takusan23.designbridge.project.ProjectFile
import java.io.File

/**
 * プロジェクト一覧ViewModel
 * */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val projectFile = ProjectFile(context)

    /** プロジェクト一覧をFlowで受け取る */
    val projectListFlow = projectFile.projectParentFolderFlow

    /**
     * 新規プロジェクトを作成
     * @param projectName プロジェクト名
     * */
    fun createNewProject(projectName: String) = projectFile.createNewProject(projectName)

    /**
     * プロジェクトを削除する
     * @param projectName プロジェクト名
     * */
    fun deleteProject(projectName: String) = projectFile.deleteProject(projectName)

    /**
     * プロジェクト名のフォルダを取得する
     * @param projectName プロジェクト名
     * */
    fun getProjectFolder(projectName: String) = projectFile.getProjectFolder(projectName)

}