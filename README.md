# KoregaE

KoregaEは、Kotlin、Android Jetpack、およびKtorを使用してOAuth認証を行い、認証したユーザのはてなブックマークのRSSフィードを表示するAndroidアプリケーションです。

## 機能

- OAuth認証フロー
- RSSフィードの取得と表示
- ComposeベースのUI
- Koinによる依存性注入

## プロジェクト構成

```
app/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── koregae/
│   │                   ├── MainActivity.kt
│   │                   ├── data/
│   │                   │   ├── local/
│   │                   │   │   └── UserInfoManager.kt
│   |                   │   └── remote/
│   │                   │       ├── RssRepository.kt
│   │                   │       └── model/
│   │                   │           └── RssItem.kt
│   │                   ├── ui/
│   │                   │   ├── view/
│   │                   │   │   ├── OAuthScreen.kt
│   │                   │   │   └── RssScreen.kt
│   │                   │   └── viewModel/
│   │                   │       ├── OAuthViewModel.kt
│   │                   │       └── RssViewModel.kt
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── koregae/
│                       └── ui/
│                           └── viewModel/
│                               ├── OAuthViewModelTest.kt
│                               └── FakeOAuthService.kt
│                               └── FakeOAuthConfig.kt
│   └── sharedTest/
│       └── java/
│           └── com/
│               └── example/
│                   └── koregae/
│                       └── ui/
│                           └── viewModel/
│                               └── FakeRssViewModel.kt
```

## はじめに

### 前提条件

- Android Studio
- Kotlin
- Gradle

### インストール

1. リポジトリをクローンします：
    ```sh
    git clone https://github.com/yourusername/koregae.git
    ```
2. Android Studioでプロジェクトを開きます。
3. Gradleファイルとプロジェクトを同期します。
4. 　consumerKeyとconsumerSecretの登録
以下のページを参照し、consumerKeyとconsumerSecretを取得します。
https://developer.hatena.ne.jp/ja/documents/auth/apis/oauth/consumer

プロジェクトのルートディレクトリにlocal.propertiesファイルを作成し、以下の内容を追加します。
```
consumerKey=YOUR_CONSUMER_KEY
consumerSecret=YOUR_CONSUMER_SECRET
```

## テストの実行

ユニットテストを実行するには、以下のコマンドを使用します：

```sh
./gradlew testDebugUnitTest
```

## 使用方法

### OAuth認証

`OAuthViewModel`はOAuth認証フローを処理します。`OAuthService`と`OAuthConfig`を使用してOAuthプロセスを管理します。

### RSSフィードの取得

`RssViewModel`は`RssRepository`を使用してRSSフィードを取得します。RSSフィードを解析し、UIの状態を更新します。

## ライセンス

このプロジェクトはMITライセンスの下でライセンスされています。詳細は`LICENSE`ファイルを参照してください。
