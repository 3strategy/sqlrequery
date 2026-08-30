plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.sqlrequery"
    compileSdk {
        version = release(37)
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.sqlrequery"
        minSdk = 31
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("io.requery:requery:1.6.0")
    implementation("io.requery:requery-android:1.6.0")
    annotationProcessor("io.requery:requery-processor:1.6.0")
}
