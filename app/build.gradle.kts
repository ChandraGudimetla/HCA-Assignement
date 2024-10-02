plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.hca"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hca"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")

    // Jetpack Compose
    implementation("androidx.compose.material3:material3:1.3.0")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    kapt("androidx.room:room-compiler:2.6.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")

    // Jetpack Compose Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Junit
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    //Espresso
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.4.0")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.4.0")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.4.0")

    //Mockito
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.mockito:mockito-inline:4.0.0")

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    kapt ("com.google.dagger:hilt-compiler:2.48")

    testImplementation ("androidx.arch.core:core-testing:2.2.0")

}