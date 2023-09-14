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
通知へのアクセスという設定項目(開発者の端末だと設定>音と通知>通知へのアクセス)を探し、「通知配達ドロイド君」にチェックを入れます。
これをしないと通知が来たことをアプリが認識できません。

3. 各種設定  
アプリを起動して、設定タブでPCのIPアドレスとポート番号(初期値: 8484)を入力します。  
通知テストボタンを押してPCに通知が表示されたら準備完了です。  
&nbsp;<img src="https://github.com/ptkNktq/AndroidNotificationNotifier/assets/7608826/4412d67f-1e8a-4343-a5fb-433adf326ec0" width="320px">

4. 通知を送るアプリの追加  
アプリタブでインストールされているアプリ一覧を表示できます。  
この中からPCに通知を送りたいアプリを選択してください。  
&nbsp;<img src="https://github.com/ptkNktq/AndroidNotificationNotifier/assets/7608826/deb01b19-eddd-47c1-870c-146278ed0ca1" width="320px">  

5. 完了  
通知対象タブに選択したアプリが表示されます。  
後は通知が来るのを待つだけ。  
&nbsp;<img src="https://github.com/ptkNktq/AndroidNotificationNotifier/assets/7608826/c34f58fa-43b9-4c40-9f11-64b1d9b77efd" width="320px">

### プライバシーポリシー
- アプリ内で取得したデータを第三者に送信することはありません。
  - ただし、バックアップの際はその一部(アプリのパッケージ名等)が利用端末のストレージに保存されます。
###### 権限の説明
- インターネット接続
  - 指定したアドレスに通知データを送信するために必要です。
- インストール済みアプリの取得
  - 通知対象のアプリを選択するための必要です。
- 通知へのアクセス
  - PCに通知する内容を知るために、通知へアクセスする必要があります。

### 著作権
このアプリのアイコンで使用しているAndroid robot(通称ドロイド君)の著作権はGoogleに帰属します。
