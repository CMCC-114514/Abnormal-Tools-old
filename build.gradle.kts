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

    testImplementation("org.hamcrest:hamcrest-core:1.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
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

    archiveFileName.set("abnormal-tools.jar")
}

tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath)
    into("$buildDir/libs/lib")
}

// -------------------- 新增任务 --------------------

// 1. 分析模块依赖（生成 jlink 所需模块列表）
tasks.register("analyzeModules") {
    group = "distribution"
    description = "分析项目所需模块，用于 jlink 裁剪 JRE"

    val outputFile = file("$buildDir/modules.txt")
    inputs.files(tasks.jar.get().archiveFile, configurations.runtimeClasspath)
    outputs.file(outputFile)

    doLast {
        // 收集所有需要分析的 JAR（主 JAR + 依赖）
        val allJars = listOf(tasks.jar.get().archiveFile.get().asFile) +
                configurations.runtimeClasspath.get().filter { it.isFile && it.extension == "jar" }

        // 运行 jdeps 提取模块名
        val moduleSet = mutableSetOf<String>()
        allJars.forEach { jarFile ->
            // 使用 jdeps 列出模块依赖（忽略 JDK 内部模块）
            val process = ProcessBuilder(
                "jdeps",
                "--print-module-deps",
                "--ignore-missing-deps",
                jarFile.absolutePath
            ).start()
            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            if (exitCode == 0 && output.isNotBlank()) {
                moduleSet.addAll(output.trim().split(","))
            } else {
                // 如果 jdeps 失败（如非模块化 JAR），至少包含 java.base
                moduleSet.add("java.base")
            }
        }

        // 添加应用程序可能需要的常见模块（根据您的实际需求调整）
        val additionalModules = listOf("java.desktop", "java.sql", "java.logging")
        moduleSet.addAll(additionalModules)

        // 写入文件
        outputFile.writeText(moduleSet.joinToString(","))
        println("Required modules: ${moduleSet.joinToString(", ")}")
    }
}

// 2. 创建自定义 JRE（依赖 analyzeModules）
tasks.register("createCustomJre") {
    group = "distribution"
    description = "使用 jlink 创建自定义 JRE"
    dependsOn("analyzeModules")

    val modulesFile = file("$buildDir/modules.txt")
    val jreDir = file("$buildDir/custom-jre")
    outputs.dir(jreDir)

    doLast {
        if (!modulesFile.exists()) {
            throw GradleException("modules.txt not found, run analyzeModules first.")
        }
        val modules = modulesFile.readText().trim()
        if (modules.isEmpty()) {
            throw GradleException("No modules found, cannot create JRE.")
        }

        // 删除旧的 JRE 目录
        jreDir.deleteRecursively()

        // 执行 jlink
        val command = listOf(
            "jlink",
            "--module-path", System.getProperty("java.home") + "/jmods",
            "--add-modules", modules,
            "--output", jreDir.absolutePath,
            "--strip-debug",
            "--compress", "2",
            "--no-header-files",
            "--no-man-pages"
        )
        println("Running: ${command.joinToString(" ")}")
        val process = ProcessBuilder(command).inheritIO().start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw GradleException("jlink failed with exit code $exitCode")
        }
        println("Custom JRE created at $jreDir")
    }
}

// 3. 生成安装程序（依赖 createCustomJre 和 jar）
tasks.register("createInstaller") {
    group = "distribution"
    description = "使用 jpackage 生成 Windows 安装程序"
    dependsOn("createCustomJre", "jar", "copyDependencies") // 确保依赖已复制

    val appName = project.name
    val appVersion = project.version.toString().takeIf { it != "unspecified" } ?: "1.0"
    val installerOutput = file("$buildDir/installer")

    doLast {
        val stagingDir = file("$buildDir/jpackage-staging")
        stagingDir.deleteRecursively()
        stagingDir.mkdirs()

        // 复制主 JAR 到根目录
        copy {
            from(tasks.jar.get().archiveFile.get().asFile)
            into(stagingDir)
            rename { "app.jar" }
        }

        // 复制所有依赖到根目录（重要！）
        copy {
            from("$buildDir/libs/lib")  // 这里假设 copyDependencies 将依赖放在此目录
            into(stagingDir)
        }

        // 执行 jpackage，不再指定 --java-options 类路径
        val command = mutableListOf(
            "jpackage",
            "--type", "exe",
            "--name", appName,
            "--app-version", appVersion,
            "--input", stagingDir.absolutePath,
            "--main-jar", "app.jar",
            "--main-class", "kk3twt.abnormal.tools.MainGUI",
            "--runtime-image", file("$buildDir/custom-jre").absolutePath,
            "--dest", installerOutput.absolutePath,
            //"--win-console",          // 调试时可保留，正式发布可去掉
            "--win-dir-chooser",
            "--win-menu",
            "--win-shortcut"
        )
        // 添加图标
        val icon = file("E:\\Code\\java\\Abnormal-tools-release\\ICON.ico")
        if (icon.exists()) command.addAll(listOf("--icon", icon.absolutePath))

        println("Running: ${command.joinToString(" ")}")
        val process = ProcessBuilder(command).inheritIO().start()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw GradleException("jpackage failed with exit code $exitCode")
        }
        println("Installer created at ${installerOutput.absolutePath}")
    }
}