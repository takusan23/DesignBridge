# DesignBridge

<p align="center">
Adobe XDで作った作品をWeb Exportプラグインを利用して生成したHtmlファイルたちを編集したり全画面表示できるアプリ。
</p>

<p align="center">
画像の部分を動画に置き換えることもできます。
</p>

<p align="center">
<img src="https://imgur.com/8xIjfWF.png" width="200">
<img src="https://imgur.com/cjvjcGl.png" width="200">
<img src="https://imgur.com/ktiZh1w.png" width="200">
<img src="https://imgur.com/5WCddAh.png" width="200">
</p>

つまりHtmlを編集してプレビューできるアプリです

Android 5から起動可能。多分

## ダウンロード
うまく行けばPlayStoreにも公開されるかも

https://github.com/takusan23/DesignBridge/releases

## 仕組み
Htmlを加工して加工した結果をWebViewに表示させているだけ。  
大したもんじゃない

## Web Exportプラグインの仕様？
テキストにアルファベットか数字が入ってないとCSSがうまく当たらない。日本語だけだとidが空になってCSSが当たらない。

## UIが調子悪い
Jetpack Composeあんまり理解していないからよくわからん挙動になる。

## ファイル管理どうにかならないの
GoogleがScoped Storageを導入したのでドキュメントフォルダへアクセスするみたいなことは多分できない。

# 開発者向け？

## 使用しているAndroid Studio
安定版では起動しません。  
Android StudioのBeta版を入れてください。

## ビルド方法
- zipファイルをダウンロードするなり、git cloneするなりでこのリポジトリの中身をダウンロードします。
- 失敗するらしいのでフォルダの中にある`.idea`を消します。
- Android Studioで開きます
- しばらく待ちます
- 実行します
- おしまい

