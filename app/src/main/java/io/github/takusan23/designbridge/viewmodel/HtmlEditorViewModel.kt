package io.github.takusan23.designbridge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File

/**
 * HTML編集画面で使うViewModel
 * @param editHtmlFilePath 編集対象のHTMLファイルのファイルパス
 * */
class HtmlEditorViewModel(application: Application, val editHtmlFilePath: String) : AndroidViewModel(application) {

    private val context = application.applicationContext

    /** Jsoup */
    var document: Document

    private val _htmlLiveData = MutableLiveData<String>()
    private val _htmlElementListLiveData = MutableLiveData<List<Element>>()
    private val _htmlSpanElementListLiveData = MutableLiveData<List<Element>>()
    private val _htmlImgOrVideoElementListLiveData = MutableLiveData<List<Element>>()
    private val _htmlInputElementListLiveData = MutableLiveData<List<Element>>()

    /** 編集中HTMLを返す */
    val htmlLiveData = _htmlLiveData as LiveData<String>

    /** HTMLの要素配列を返す */
    val htmlElementListLiveData = _htmlElementListLiveData as LiveData<List<Element>>

    /** <span>を返す */
    val htmlSpanElementListLiveData = _htmlSpanElementListLiveData as LiveData<List<Element>>

    /** <img>と<video>を返す */
    val htmlImgElementListLiveData = _htmlImgOrVideoElementListLiveData as LiveData<List<Element>>

    /** <input>を返す */
    val htmlInputElementListLiveData = _htmlInputElementListLiveData as LiveData<List<Element>>

    /** プロジェクト名 */
    val projectName = File(editHtmlFilePath).parentFile?.name!!

    init {
        // HTMLスクレイピング
        Jsoup.parse(File(editHtmlFilePath).readText()).also { doc ->
            document = doc
        }
        sendHtml()
    }

    /**
     * 要素のタグ名を変更する
     *
     * @param elementId 要素についてるID
     * @param name 変更するタグ名
     * */
    fun changeElementTagName(elementId: String, name: String) {
        val targetElement = document.getElementById(elementId)
        targetElement.tagName(name)
        // spanから変更になるかもなので消す
        targetElement.text("")
    }

    /**
     * HTMLの要素を編集する
     *
     * @param elementId 要素のID
     * @param text 指定するテキスト
     * */
    fun setElementText(elementId: String, text: String) {
        document.getElementById(elementId).text(text)
        sendHtml()
    }

    /**
     * HTMLのimg/video要素のsrcを変更する
     * @param elementId 対象のID
     * @param src 画像のURLなど
     * */
    fun setImgOrVideoElementSrc(elementId: String, src: String) {
        // 拡張子がmp4ならタグ名をvideoへ
        if (src.split(".").lastOrNull() == "mp4") {
            document.getElementById(elementId).attr("src", src)
            document.getElementById(elementId).attr("autoplay", true)
            document.getElementById(elementId).attr("controls", true)
        } else {
            document.getElementById(elementId).attr("src", src)
        }
        sendHtml()
    }

    /**
     * HTMLのspan要素のvalueを変更する
     *
     * @param elementId 要素のID
     * @param value 中身
     * */
    fun setInputElement(elementId: String, value: String) {
        document.getElementById(elementId).attr("value", value)
        sendHtml()
    }

    /**
     * 遷移先を変更する。aタグではなくjsのwindow.locationを使ってる
     *
     * @param elementId 要素のID
     * @param href リンク
     * */
    fun setClickLocation(elementId: String, href: String) {
        when {
            // 空文字列の場合は消す
            href.isEmpty() -> document.getElementById(elementId).removeAttr("onclick")
            // 相対パスの場合は ./ をつける
            !(href.startsWith("https://") || href.startsWith("http://")) -> document.getElementById(elementId).attr("onclick", "window.location='./$href'")
            // それ以外
            else -> document.getElementById(elementId).attr("onclick", "window.location='$href'")
        }
        sendHtml()
    }

    /** HTMLを保存する。プレビューの際もこの関数を呼んでからプレビューが表示される。一応サスペンド関数 */
    suspend fun saveHtml() = withContext(Dispatchers.IO) {
        File(editHtmlFilePath).writeText(document.html())
    }

    /** LiveDataへHTMLを送信する。Jsoupの編集結果を送信する */
    private fun sendHtml() {
        _htmlLiveData.value = document.html()
        val spanList = document.getElementsByTag("span")
        val imgList = document.getElementsByTag("img")
        val videoList = document.getElementsByTag("video")
        val inputList = document.getElementsByTag("input")
        _htmlSpanElementListLiveData.value = spanList
        _htmlImgOrVideoElementListLiveData.value = imgList + videoList
        _htmlInputElementListLiveData.value = inputList
    }

}