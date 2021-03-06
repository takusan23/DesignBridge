package io.github.takusan23.designbridge.tool

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

/** ファイル関係 */
object FileTool {

    /**
     * Uriからファイル名を取得する。失敗時は「不明」になります
     *
     * @param context Context
     * @param uri StorageAccessFrameworkとかで取れるあれ
     * @return ファイル名
     * */
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        // Uriを使ってMediaStoreへ問い合わせる
        val result = contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null,
            null
        ) ?: return "不明"
        result.moveToFirst()
        var fileName = "不明"
        repeat(result.columnCount) {
            fileName = result.getString(0)
            // 次行く
            result.moveToNext()
        }
        result.close()
        return fileName
    }

    /**
     * ByteをMBに変換する。少数二桁まで
     * @param byte Byte
     * @return MB
     * */
    fun byteToMB(byte: Long): Float {
        return String.format("%.2f", byte / 1024f / 1024f).toFloat()
    }

    /**
     * 指定したUriを指定パスにコピーする
     *
     * @param context
     * @param copyFolderPath コピー先
     * @param uri Uri
     * */
    fun uriFileCopy(context: Context, uri: Uri, copyFolderPath: String) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        // ファイル名
        val fileName = getFileNameFromUri(context, uri)
        // 書き込む
        val file = File(copyFolderPath, fileName).apply { createNewFile() }
        val outputStream = file.outputStream()
        // コピー
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
    }

}