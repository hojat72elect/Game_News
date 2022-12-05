import com.paulrybitskyi.gamedge.extensions.property
import com.paulrybitskyi.gamedge.extensions.stringField

plugins {
    androidLibrary()
    gamedgeAndroid()
    kotlinKapt()
    ksp()
    kotlinxSerialization()
    daggerHiltAndroid()
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.paulrybitskyi.gamedge.common.testing.GamedgeTestRunner"

        stringField("GAMESPOT_API_KEY", property("GAMESPOT_API_KEY", ""))
    }
}

dependencies {
    api(project(deps.local.commonApi))
    implementation(project(deps.local.core))

    implementation(deps.kotlin.coroutines)
    implementation(deps.kotlin.serialization)

    implementation(deps.square.retrofit)
    implementation(deps.square.retrofitKotlinxSerializationConverter)

    implementation(deps.misc.kotlinResult)

    implementation(deps.google.daggerHiltAndroid)
    kapt(deps.google.daggerHiltAndroidCompiler)

    implementation(deps.misc.hiltBinder)
    ksp(deps.misc.hiltBinderCompiler)

    testImplementation(project(deps.local.commonTesting))
    testImplementation(deps.testing.jUnit)
    testImplementation(deps.testing.truth)
    testImplementation(deps.testing.mockk)
    testImplementation(deps.testing.coroutines)

    androidTestImplementation(project(deps.local.commonTesting))
    androidTestImplementation(deps.testing.testRunner)
    androidTestImplementation(deps.testing.jUnitExt)
    androidTestImplementation(deps.testing.truth)
    androidTestImplementation(deps.testing.mockWebServer)

    androidTestImplementation(deps.testing.daggerHilt)
    kaptAndroidTest(deps.google.daggerHiltAndroidCompiler)
}
