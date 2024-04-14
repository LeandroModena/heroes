plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

subprojects {
    afterEvaluate {
        val retrofitVersion = "2.9.0"
        dependencies {
            "implementation"("com.squareup.retrofit2:retrofit:$retrofitVersion")
            "implementation"("com.squareup.retrofit2:converter-gson:$retrofitVersion")
        }
    }
}
