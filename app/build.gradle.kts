plugins {
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
}

application {
    mainClass = "arg.hozocabby.Main"
}