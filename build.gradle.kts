// build.gradle.kts
plugins {
    java
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("kk3twt.abnormal.tools.MainGUI")
}

group = "kk3twt.abnormal.tools"

repositories {
    mavenCentral()
    maven {
        name = "TarsosDSP repository"
        url = uri("https://mvn.0110.be/releases")
    }
}

dependencies {
    implementation("com.alibaba:fastjson:2.0.52")

    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hamcrest:hamcrest-core:1.3")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "kk3twt.abnormal.tools.MainGUI" // 替换为你的主类全限定名
        )
    }
}

tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath)
    into("$buildDir/libs/lib")
}