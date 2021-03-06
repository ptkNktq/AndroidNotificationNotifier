# 通知配達ドロイド君

Android端末の通知をPCに送信するアプリケーション

### 使い方
[ここ](https://github.com/ptkNktq/AndroidNotificationNotifier/releases)からダウンロードして解凍してください。  
次の3つのファイルがあります。
* android.apk (Androidアプリ)
* desktop.exe (デスクトップアプリ)
* settings.json (デスクトップアプリの設定ファイル)

#### デスクトップアプリ
desktop.exeを実行するだけです。タスクトレイに常駐するので、放っておけば通知が来ます。  
ポート番号を変える必要がある場合は、settings.jsonのportの値を変更して下さい。設定を反映させるには再起動する必要があります。多重起動対策をしていないので気をつけて下さい。  
また、desktop.exeとsettings.jsonが同じフォルダ内にあれば、それらを何処に置いても(多分)問題なく動きます。

#### Androidアプリ
1. インストール  
android.apkをインストールします。

2. 通知へのアクセス  
通知へのアクセスという設定項目(開発者の端末だと設定>音と通知>通知へのアクセス)を探し、「通知配達ドロイド君」にチェックを入れます。これをしないと、通知が来たことをアプリが認識できません。

3. 各種設定  
アプリを起動して、PCのプライベートIPアドレスを入力します。  
もしデスクトップアプリでポート番号を変更したのなら、ポート番号をそれと同じ番号に変更します。テスト通知ボタンを押してPCに通知が表示されたら準備完了です。  
　<img src="https://raw.githubusercontent.com/ptkNktq/AndroidNotificationNotifier/images/20160203113940.png" width="320px">

4. 通知を送るアプリの追加  
右下のボタンからアプリ一覧を表示できます。  
　<img src="https://raw.githubusercontent.com/ptkNktq/AndroidNotificationNotifier/images/20160203113217.png" width="320px">  
この中からPCに通知を送りたいアプリを選択してください(複数選択可)。  
戻るキーを押すと選択したアプリが下に表示されます。  
　<img src="https://raw.githubusercontent.com/ptkNktq/AndroidNotificationNotifier/images/20160203114427.png" width="320px">  
後は通知が来るのを待つだけ。

### 著作権
このアプリのアイコンで使用しているAndroid robot(通称ドロイド君)の著作権はGoogleに帰属します。
