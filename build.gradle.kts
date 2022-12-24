plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.testng:testng:7.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
