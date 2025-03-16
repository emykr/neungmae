import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

plugins {
    kotlin("jvm") version "2.1.20-Beta1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.sobixn.matchup"

// 버전 정보를 저장할 JSON 파일 경로 설정
val versionFile = File(rootProject.projectDir, "version.json")
// Gson 인스턴스 생성
val gson = Gson()

// 버전 정보를 읽어오는 함수
fun loadVersion(): Map<String, Int> {
    return try {
        if (versionFile.exists()) {
            val versionType = object : TypeToken<Map<String, Int>>() {}.type
            gson.fromJson(versionFile.readText(), versionType)
        } else {
            mapOf("major" to 1, "minor" to 0, "patch" to 0, "build" to 0)
        }
    } catch (e: Exception) {
        e.printStackTrace()  // 오류 로그 출력
        mapOf("major" to 1, "minor" to 0, "patch" to 0, "build" to 0)
    }
}

// 버전 정보를 저장하는 함수
fun saveVersion(version: Map<String, Int>) {
    versionFile.writeText(gson.toJson(version))
}

// 현재 버전 정보 로드
val currentVersion = loadVersion()
// 패치 버전 증가
val newVersion = currentVersion.toMutableMap().apply {
    this["patch"] = this["patch"]!! + 2
}
// 새로운 버전 정보를 파일에 저장
saveVersion(newVersion)

// 버전 정보를 문자열로 설정
version = "${newVersion["major"]}.${newVersion["minor"]}.${newVersion["patch"]}.${newVersion["build"]}"

repositories {
    mavenCentral()
    maven ("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc-repo" }
    maven("https://oss.sonatype.org/content/groups/public/") { name = "sonatype" }

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.code.gson:gson:2.8.9")
    compileOnly ("com.comphenix.protocol:ProtocolLib:5.1.0")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
