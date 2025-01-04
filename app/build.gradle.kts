import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.koregae"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.koregae"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val properties = Properties().apply {
            load(FileInputStream(rootProject.file("local.properties")))
        }
        val consumerKey = properties["consumerKey"] as String
        val consumerSecret = properties["consumerSecret"] as String

        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "CONSUMER_KEY", "\"${consumerKey}\"")
            buildConfigField("String", "CONSUMER_SECRET", "\"${consumerSecret}\"")
        }

        debug {
            buildConfigField("String", "CONSUMER_KEY", "\"${consumerKey}\"")
            buildConfigField("String", "CONSUMER_SECRET", "\"${consumerSecret}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

dependencies {
    // AndroidX関連
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.browser)

    implementation(libs.scribejava.apis)

    // Koin関連の依存関係
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.androidx.compose)

    // Ktor関連の依存関係
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)

    // コルーチン & シリアライズ
    implementation(libs.coroutines.core)
    implementation(libs.serialization.json)

    // Lifecycle & ViewModel
    implementation(libs.lifecycle.viewmodel.compose)

    // Jetpack DataStore
    implementation(libs.androidx.datastore.preferences)

    // JUnit5テスト関連
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    // AndroidX Test - Core library
    testImplementation(libs.androidx.core.testing)

    // Kotlin Coroutines Test library
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Instrumentation Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
