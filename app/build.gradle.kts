import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    // Google services (Firebase) plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.liquotrack.stocksip"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.liquotrack.stocksip"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:5283/api/v1/\"")
        }


        create("usb") {
            initWith(getByName("debug"))
            buildConfigField("String", "BASE_URL", "\"http://localhost:5283/api/v1/\"")
            matchingFallbacks += listOf("debug")
        }

        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://stocksip-back-end.azurewebsites.net/api/v1/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Base android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Material Icons dependency
    implementation(libs.androidx.material.icons.extended)

    // Compose Navigation dependency
    implementation(libs.androidx.navigation.compose)

    // Coil dependency for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Retrofit dependency for web services consumption
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // View Model dependency for usage of view models in the MVVM pattern
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Room dependency for usage of local database with Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.foundation)
    implementation(libs.googleid)
    implementation("com.google.android.gms:play-services-auth")
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-auth")
    ksp(libs.androidx.room.compiler)

    // Hilt dependency for injection of dependencies in the app
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Dependencies for testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase dependencies
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation(libs.firebase.analytics)

    //Google Identity Services
    implementation("androidx.credentials:credentials:1.6.0-beta01")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0-beta01")

}