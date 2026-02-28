import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.nassef.shoppinglistdemo"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.nassef.shoppinglistdemo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    /*kotlinOptions {
        jvmTarget = "11"
    }*/
    kotlin {          // ← required wrapper
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Compose navigation
    implementation(libs.androidx.navigation.compose)

    //navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit - needed for DI module to create instances
//    implementation(libs.retrofit)
//    implementation(libs.converter.gson)

    // Room - needed for DI module to create database instances
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    //localization lib
    implementation(libs.localizeme)
//    implementation(project(":localize"))

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))

}