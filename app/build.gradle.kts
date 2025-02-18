plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace="com.example.rainbowcalendar"
    compileSdk=34
    androidResources {
        generateLocaleConfig=true
    }
    defaultConfig {
        applicationId="com.example.rainbowcalendar"
        minSdk=26
        targetSdk=34
        versionCode=1
        versionName="1.0"


        testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary=true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled=true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    bundle {
        language {
            enableSplit=false
        }
    }
    compileOptions {
        sourceCompatibility=JavaVersion.VERSION_11
        targetCompatibility=JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget="11"

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    buildFeatures {
        viewBinding=true
        compose=true
    }
    packaging {
        resources {
            excludes+="/META-INF/{AL2.0,LGPL2.1}"
            excludes+="/META-INF/{AL2.0,LGPL2.1}"
            merges+="META-INF/LICENSE.md"
            merges+="META-INF/LICENSE-notice.md"

        }
    }
    lint.disable+="UsingMaterialAndMaterial3Libraries"
}
tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.navigation:navigation-compose:2.8.6")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.material:material-icons-extended:1.7.8")


    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.paging:paging-compose:3.3.5")


    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("sh.calvin.reorderable:reorderable:2.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material:1.7.6")
    implementation("androidx.compose.foundation:foundation:1.7.7")
    implementation("androidx.compose.animation:animation:1.5.4")
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")
    implementation("com.github.vsnappy1:ComposeDatePicker:2.2.0")


    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.6")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")


    testImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
    testImplementation("org.mockito:mockito-android:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.mockito:mockito-android:5.10.0")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("androidx.test.espresso:espresso-core:3.6.1")

    androidTestImplementation("org.mockito:mockito-android:3.8.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    androidTestImplementation("com.google.truth:truth:1.1.5")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.7")
    androidTestImplementation("org.mockito:mockito-android:5.10.0")
    androidTestImplementation("org.mockito:mockito-core:5.10.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
//    testImplementation("io.mockk:mockk:1.13.2")



}