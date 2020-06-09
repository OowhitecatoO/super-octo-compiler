import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "io.github.oowhitecatoo"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "io.github.oowhitecatoo.superoctocompiler.MainKt"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    shadowJar {
        archiveClassifier.set("")
    }
}
