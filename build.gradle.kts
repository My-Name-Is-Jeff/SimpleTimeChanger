/*
 * SimpleTimeChanger
 * Copyright (C) 2021 My-Name-Is-Jeff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.IReobfuscator
import net.minecraftforge.gradle.user.ReobfMappingType.SEARGE
import net.minecraftforge.gradle.user.TaskSingleReobf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("net.minecraftforge.gradle.forge") version "6f5327"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("org.spongepowered.mixin") version "d5f9873d60"
    java
}

version = "1.0.0"
group = "mynameisjeff.simpletimechanger"

minecraft {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false
    isGitVersion = false
    clientJvmArgs.addAll(
            setOf(
                    "-Delementa.dev=true",
                    "-Delementa.debug=true"
            )
    )
    clientRunArgs.addAll(
            setOf(
                    "--tweakClass gg.essential.loader.stage0.EssentialSetupTweaker",
                    "--mixin mixins.simpletimechanger.json"
            )
    )
}

repositories {
    mavenLocal()
    mavenCentral()
    setOf(
            "https://repo.spongepowered.org/repository/maven-public/",
            "https://repo.sk1er.club/repository/maven-public/",
            "https://repo.sk1er.club/repository/maven-releases/",
            "https://jitpack.io"
    ).forEach {
        maven(it)
    }
}

val shadowMe: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.7.11-SNAPSHOT")

    shadowMe("gg.essential:loader-launchwrapper:1.1.0")
    implementation("gg.essential:essential-1.8.9-forge:1282")
}

mixin {
    disableRefMapWarning = true
    defaultObfuscationEnv = searge
    add(sourceSets.main.get(), "mixins.simpletimechanger.refmap.json")
}

sourceSets {
    main {
        ext["refmap"] = "mixins.simpletimechanger.refmap.json"
        output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", project.minecraft.version)

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to project.minecraft.version))
        }
    }
    named<Jar>("jar") {
        archiveBaseName.set("simpletimechanger")
        manifest {
            attributes(
                    mapOf(
                            "FMLCorePluginContainsFMLMod" to true,
                            "ForceLoadAsMod" to true,
                            "MixinConfigs" to "mixins.simpletimechanger.json",
                            "ModSide" to "CLIENT",
                            "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                            "TweakOrder" to "0"
                    )
            )
        }
        enabled = false
    }
    named<ShadowJar>("shadowJar") {
        archiveFileName.set(jar.get().archiveFileName)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shadowMe)

        exclude(
                "**/LICENSE.md",
                "**/LICENSE.txt",
                "**/LICENSE",
                "**/NOTICE",
                "**/NOTICE.txt",
                "pack.mcmeta",
                "dummyThing",
                "**/module-info.class",
                "META-INF/proguard/**",
                "META-INF/maven/**",
                "META-INF/versions/**",
                "META-INF/com.android.tools/**",
                "fabric.mod.json"
        )
        mergeServiceFiles()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
    named<TaskSingleReobf>("reobfJar") {
        dependsOn(shadowJar)
    }
}

configure<NamedDomainObjectContainer<IReobfuscator>> {
    create("shadowJar") {
        mappingType = SEARGE
        classpath = sourceSets.main.get().compileClasspath
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8
