plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.agenda"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.agenda"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

// ESTE BLOQUE ES LA SOLUCIÓN AL ERROR DE VERSIONES (Resolution Strategy)
// Obliga a todas las librerías a usar versiones compatibles con SDK 35
configurations.all {
    resolutionStrategy {
        force("androidx.activity:activity-ktx:1.9.3")
        force("androidx.activity:activity:1.9.3")
        force("androidx.fragment:fragment-ktx:1.8.5")
        force("androidx.core:core-ktx:1.13.1")
        force("androidx.core:core:1.13.1")
        force("androidx.browser:browser:1.8.0")
        force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    }
}

dependencies {
    // UI & Core base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)

    // Arquitectura (ViewModels y Fragments)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx.v1130)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Networking & Coroutines
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // AWS Amplify y soporte para Java antiguo
    implementation(libs.aws.auth.cognito)
    implementation(libs.play.services.maps3d)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.browser)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}