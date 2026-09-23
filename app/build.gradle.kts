plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "eightBugs.osteocare"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "eightBugs.osteocare"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation("org.tensorflow:tensorflow-lite:2.17.0")
    implementation(libs.material)
    testImplementation(libs.junit)
    implementation("org.tensorflow:tensorflow-lite:2.17.0")

    implementation("com.google.mlkit:pose-detection:18.0.0-beta5")

    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}