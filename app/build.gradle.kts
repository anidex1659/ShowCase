import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.10"
    id("com.google.devtools.ksp")
}


val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val tmdbApiKey =
    localProperties.getProperty(
        "TMDB_API_KEY"
    ) ?: ""

android {
    namespace = "com.example.showcase"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.example.showcase"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"


        buildConfigField(
            "String",
            "TMDB_API_KEY",
            "\"$tmdbApiKey\""
        )

        buildConfigField(
            "String",
            "TEST_VALUE",
            "\"HELLO_WORLD\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //navigiction
    val nav_version = "2.8.4"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    /*serialization*/
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


    //icons
    implementation("androidx.compose.material:material-icons-extended")


    //media3 Dependesis
    implementation("androidx.media3:media3-exoplayer:1.8.0")
    implementation("androidx.media3:media3-ui:1.8.0")
    implementation("androidx.media3:media3-session:1.8.0")

    implementation("androidx.documentfile:documentfile:1.0.1")

    //database
    implementation("androidx.room:room-runtime:2.8.0")
    implementation("androidx.room:room-ktx:2.8.0")
    ksp("androidx.room:room-compiler:2.8.0")

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx)

    //https calls
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    //img loader
    implementation("io.coil-kt:coil:2.7.0")
    implementation("io.coil-kt:coil-compose:2.7.0")



}