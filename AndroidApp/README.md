# Androidアプリ

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

### 注意事項

- AGPの更新をするときはAndroid Studioとの互換性を確認すること
    - https://developer.android.com/build/releases/gradle-plugin?hl=ja#android_gradle_plugin_and_android_studio_compatibility