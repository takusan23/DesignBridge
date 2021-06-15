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

    /** 編集中HTMLを返す */
    val htmlLiveData = _htmlLiveData as LiveData<String>

    /** HTMLの要素配列を返す */
    val htmlElementListLiveData = _htmlElementListLiveData as LiveData<List<Element>>

    /** <span>を返す */
    val htmlSpanElementListLiveData = _htmlSpanElementListLiveData as LiveData<List<Element>>

    /** <img>と<video>を返す */
    val htmlImgElementListLiveData = _htmlImgOrVideoElementListLiveData as LiveData<List<Element>>

    /** プロジェクト名 */
    val projectName = File(editHtmlFilePath).parentFile?.name!!

    init {
        // HTMLスクレイピング
        Jsoup.parse(File(editHtmlFilePath).readText()).also { doc ->
            // spanにIDを振る。imgはcssで使ってるので変えるとまずい
            doc.getElementsByTag("span").forEachIndexed { index, element ->
                // 振ってあるなら別にいい
                if (element.id().isEmpty()) {
                    element.attr("id", "span_$index")
                }
            }
            document = doc
        }
        sendHtml()
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
     * HTMLのImg要素のsrcを変更する
     * @param elementId 対象のID
     * @param src 画像のURLなど
     * */
    fun setImgElementSrc(elementId: String, src: String) {
        // 拡張子がmp4ならタグ名をvideoへ
        if (src.split(".").lastOrNull() == "mp4") {
            document.getElementById(elementId).tagName("video")
            document.getElementById(elementId).attr("src", src)
            document.getElementById(elementId).attr("autoplay", true)
            document.getElementById(elementId).attr("controls", true)
        } else {
            document.getElementById(elementId).tagName("img")
            document.getElementById(elementId).attr("src", src)
            // srcsetは消す？
            document.getElementById(elementId).removeAttr("srcset")
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
        _htmlElementListLiveData.value = document.getElementsByTag("div")
        _htmlSpanElementListLiveData.value = document.getElementsByTag("span")
        _htmlImgOrVideoElementListLiveData.value = document.getElementsByTag("img") + document.getElementsByTag("video")
    }

}