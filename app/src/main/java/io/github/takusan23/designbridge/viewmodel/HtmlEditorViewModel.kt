package io.github.takusan23.designbridge.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HtmlEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    /** Jsoup */
    lateinit var document: Document

    private val _htmlLiveData = MutableLiveData<String>()
    private val _htmlElementListLiveData = MutableLiveData<List<Element>>()
    private val _htmlSpanElementListLiveData = MutableLiveData<List<Element>>()

    /** 編集中HTMLを返す */
    val htmlLiveData = _htmlLiveData as LiveData<String>

    /** HTMLの要素配列を返す */
    val htmlElementListLiveData = _htmlElementListLiveData as LiveData<List<Element>>

    /** <span>を返す */
    val htmlSpanElementListLiveData = _htmlSpanElementListLiveData as LiveData<List<Element>>

    /** Uriを受け取ってHtmlを変数に入れる */
    fun setHtmlFromUri(uri: Uri) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val htmlString = inputStream.bufferedReader().readText()
        // HTMLスクレイピング
        document = Jsoup.parse(htmlString)
        sendHtml()
    }

    /**
     * HTMLの要素を編集する
     * なんかspanのテキストを変更しようと思ったんだけどid振ってないから詰んだので
     *
     * @param parentElementId 親要素のID
     * @param text 指定するテキスト
     * */
    fun setElementText(parentElementId: String, text: String) {
        document.getElementById(parentElementId).child(0).text(text)
        sendHtml()
    }

    /** LiveDataへHTMLを送信する。Jsoupの編集結果を送信する */
    private fun sendHtml() {
        _htmlLiveData.value = document.html()
        _htmlElementListLiveData.value = document.getElementsByTag("div")
        _htmlSpanElementListLiveData.value = document.getElementsByTag("span")
    }

}