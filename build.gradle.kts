plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false// Asegúrate de que sea la misma versión
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
}

tasks.dokkaGfm {
    outputDirectory.set(layout.buildDirectory.dir("documentation/markdown"))
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}
