import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigationSafeArgs)
    alias(libs.plugins.devtoolKsp)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.firebase.crashlitycs)

}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()){
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.labs.recipe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.labs.recipe"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        buildConfigField("String", "API_KEY", "\"${keystoreProperties["apiKey"]}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ROOM
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.room.ktx)
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Life Cycle Arch
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Annotation processor
    ksp(libs.androidx.lifecycle.compiler)
    // Retrofit
//    implementation(libs.retrofit)
    // Moshi
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    //dagger-hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    //coil image loader
    implementation(libs.coil)
    // Shimmer effect
    implementation(libs.shimmer)
    // Gson
    implementation(libs.gson)
    //Datastore
    implementation(libs.androidx.datastore.preferences)
    // jsoup
    implementation(libs.jsoup)
    // Firebase
    implementation(platform(libs.firebase.bom))

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)


}