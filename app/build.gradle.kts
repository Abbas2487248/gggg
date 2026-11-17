plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.hayday.smartmanager"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.hayday.smartmanager"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}
