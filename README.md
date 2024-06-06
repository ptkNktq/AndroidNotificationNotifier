# 通知配達ドロイド君

Android端末の通知をPCに送信するアプリケーション

<a href='https://play.google.com/store/apps/details?id=me.nya_n.notificationnotifier&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Google Play で手に入れよう' src='https://play.google.com/intl/en_us/badges/static/images/badges/ja_badge_web_generic.png' width="200"/></a>

### 使い方

#### デスクトップアプリ
1. インストール  
[ここ](https://github.com/ptkNktq/AndroidNotificationNotifier/releases)からダウンロードして解凍してください。  
`desktop.exe`を実行するだけです。  
タスクトレイに常駐するので、放っておけば通知が来ます。

1. 設定変更  
ポート番号を変える必要がある場合は、`settings.json`の`port`の値を変更して下さい。  
設定を反映させるには再起動する必要があります。  
⚠️ 多重起動対策をしていないので気をつけて下さい。  
また、`desktop.exe`と`settings.json`が同じフォルダ内にあれば、それらを何処に置いても(多分)問題なく動きます。

#### Androidアプリ
1. インストール  
Google Playからインストールしてください。

1. 権限の許可  
アプリを起動して、画面の表示に従って2つの権限を許可してください。  
それらを許可しなければアプリを使うことはできません。

1. 各種設定  
アプリを起動して、設定タブでPCのIPアドレスとポート番号(初期値: 8484)を入力します。  
通知テストボタンを押してPCに通知が表示されたら準備完了です。  
&nbsp;<img src="https://github.com/ptkNktq/AndroidNotificationNotifier/assets/7608826/4412d67f-1e8a-4343-a5fb-433adf326ec0" width="320px">

1. 通知を送るアプリの追加  
アプリタブでインストールされているアプリ一覧を表示できます。  
この中からPCに通知を送りたいアプリを選択してください。  
&nbsp;<img src="https://github.com/ptkNktq/AndroidNotificationNotifier/assets/7608826/deb01b19-eddd-47c1-870c-146278ed0ca1" width="320px">  

1. 完了  
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
