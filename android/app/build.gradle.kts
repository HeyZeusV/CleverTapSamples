import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.google.services)
}

android {
	namespace = "com.heyzeusv.clevertapassessment"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.heyzeusv.clevertapassessment"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		val keysFile = project.rootProject.file("keys.properties")
		val properties = Properties()
		properties.load(keysFile.inputStream())

		val clevertapAccountId = properties.getProperty("CLEVERTAP_ACCOUNT_ID") ?: ""
		val clevertapToken = properties.getProperty("CLEVERTAP_TOKEN") ?: ""

		manifestPlaceholders["CLEVERTAP_ACCOUNT_ID"] = clevertapAccountId
		manifestPlaceholders["CLEVERTAP_TOKEN"] = clevertapToken
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		debug {
			applicationIdSuffix = ".dev"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		buildConfig = true
		compose = true
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
	implementation(libs.androidx.navigation.compose)
	implementation(libs.kotlinx.serialization.json)

	// CleverTap
	implementation(libs.clevertap.android.sdk)
	implementation(libs.clevertap.push.templates)

	// CleverTap App Inbox
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.recyclerview)
	implementation(libs.androidx.viewpager)
	implementation(libs.material)

	// CleverTap In-App Videos
	implementation(libs.androidx.media3)
	implementation(libs.androidx.media3.hls)
	implementation(libs.androidx.media3.ui)

	// Firebase
	implementation(libs.firebase.messaging)

	// Glide
	implementation(libs.glide)

	// Koin
	implementation(libs.koin.compose)

	// ViewModel
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.androidx.lifecycle.viewmodel.ktx)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}