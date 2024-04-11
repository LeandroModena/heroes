plugins {
    id("java-library")
    id("kotlin")
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
// Retrofit and Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}