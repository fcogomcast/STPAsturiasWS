plugins {
    id("java")
}

group = "es.tributasenasturias"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.2")
    implementation("junit:junit:4.13.2")
    implementation("junit:junit:4.13.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.2")
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnitPlatform()
}