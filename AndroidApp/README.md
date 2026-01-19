# Androidアプリ

### 開発環境

- Android Studio Otter 3 Feature Drop | 2025.2.3
- Windows 11
    - 一部 wsl2(Ubuntu 22.04.5 LTS)

### テスト

###### ユニットテスト

- GitHub Actionsで自動実行

###### スクリーンショットによるテスト

- Previewのスクリーンショット自動生成
    - `./gradlew :ui:updateDebugScreenshotTest`
- Previewのテスト
    - `./gradlew :ui:validateDebugScreenshotTest`
- 結果
    - https://github.com/ptkNktq/AndroidNotificationNotifier/tree/develop/AndroidApp/ui/build/reports/screenshotTest/preview/debug/.html

###### 注意事項

- ⚠ Windows環境だと、`CreateProcess error=206, ファイル名または拡張子が長すぎます。`というエラーが出る可能性がある
  ⚠
    - 諦めてwsl2等使おう

### 注意事項

- AGPの更新をするときはAndroid Studioとの互換性を確認すること
    - https://developer.android.com/build/releases/gradle-plugin?hl=ja#android_gradle_plugin_and_android_studio_compatibility