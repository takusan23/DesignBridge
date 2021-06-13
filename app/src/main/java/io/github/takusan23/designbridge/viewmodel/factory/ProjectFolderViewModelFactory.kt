package io.github.takusan23.designbridge.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.takusan23.designbridge.viewmodel.ProjectDetailViewModel

/**
 * プロジェクトの中身画面ViewModelを初期化するやつ
 * */
class ProjectFolderViewModelFactory(private val application: Application, private val projectName: String) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectDetailViewModel(application, projectName) as T
    }

}