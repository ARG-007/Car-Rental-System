plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}

application {
    mainClass = "arg.hozocabby.Main"
}

tasks.run.configure {
    standardInput = System.`in`
}

tasks.run.configure {
    mainClass = application.mainClass
}