// build.gradle.kts
plugins {
    java
    application
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
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("com.googlecode.soundlibs:mp3spi:1.9.5.4")
    implementation("com.alibaba:fastjson:2.0.52")

    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hamcrest:hamcrest-core:1.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
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
    // 将依赖打包进 JAR
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}