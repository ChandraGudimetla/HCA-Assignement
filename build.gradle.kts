// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.android.library") version "8.1.4" apply false
}