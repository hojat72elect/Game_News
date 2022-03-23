/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    androidLibrary()
    gamedgeAndroid()
    kotlinKapt()
    ksp()
    daggerHiltAndroid()
}

android {
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versions.compose
    }
}

dependencies {
    implementation(project(deps.local.domain))
    implementation(project(deps.local.core))
    implementation(project(deps.local.commonsUi))
    implementation(project(deps.local.imageLoading))

    implementation(deps.androidX.recyclerView)
    implementation(deps.androidX.constraintLayout)
    api(deps.androidX.swipeRefreshLayout)
    implementation(deps.androidX.coreKtx)

    implementation(deps.compose.ui)
    implementation(deps.compose.tooling)
    implementation(deps.compose.foundation)
    implementation(deps.compose.activity)
    implementation(deps.compose.material)
    implementation(deps.compose.runtime)
    implementation(deps.compose.constraintLayout)
    implementation(deps.compose.coil)
    implementation(deps.compose.accompanist.swipeRefresh)

    implementation(deps.google.materialComponents)

    implementation(deps.commons.core)
    implementation(deps.commons.ktx)
    implementation(deps.commons.widgets)
    implementation(deps.commons.recyclerView)
    implementation(deps.commons.material)
    implementation(deps.commons.deviceInfo)

    implementation(deps.misc.expandableTextView)

    implementation(deps.google.daggerHilt)
    kapt(deps.google.daggerHiltCompiler)

    implementation(deps.misc.hiltBinder)
    ksp(deps.misc.hiltBinderCompiler)

    testImplementation(deps.testing.jUnit)
    androidTestImplementation(deps.testing.jUnitExt)
}
