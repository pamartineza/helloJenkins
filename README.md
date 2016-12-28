#Hello Jenkins App

This is an Android multi-module demo app to verify the proper configuration of a Jenkins CI server and a SonarQube Server, should work straightforward if your sonarQube server is at localhost:9000

I have wrote an extensive Medium article explaining how to configure Ubuntu + Jenkins + Sonarqube for Android development, you can find it here -> https://medium.com/@pamartineza/how-to-set-up-a-continuous-integration-server-for-android-development-ubuntu-jenkins-sonarqube-43c1ed6b08d3


###How to configure JaCoCo Coverage in your apps:

In your **app** module add the **Jacoco-android-gradle-plugin** https://github.com/arturdm/jacoco-android-gradle-plugin

```groovy
buildscript {
  repositories {
    ...
    jcenter()
  }
  dependencies {
    ...
    classpath 'com.dicedmelon.gradle:jacoco-android:0.1.1'
  }
}

apply plugin: 'jacoco-android'

jacoco {
    toolVersion = jacocoVersion
}
```

In your java modules just add:

```groovy
apply plugin: "jacoco"

jacoco {
    toolVersion = jacocoVersion
}
```

In your parent build.gradle add:

```groovy
ext {
    jacocoVersion = '0.7.8'
}
```



###How to configure sonarQube in your apps:

In your parent **build.gradle** add these lines

```groovy
plugins {
    id "org.sonarqube" version "2.2.1"
}

sonarqube {
    properties {
        def jenkinsJobName = System.getenv('JOB_NAME')
        def jenkinsJobBuild = System.getenv('BUILD_NUMBER')

        def projectVersion = "nonJenkinsBuild_1.0"
        def projectName = "HelloJenkins"
        def projectKey = "hellojenkins:hellojenkins"
        def branch = "master"

        if (jenkinsJobName && jenkinsJobBuild) {
            jenkinsJobName = jenkinsJobName.replace(" ", "_")
            projectVersion = jenkinsJobName + '_' + jenkinsJobBuild
        }

        //change url if sonarqube is not at localhost:9000
        property "sonar.host.url", "http://localhost:9000"
        property "sonar.projectKey", projectKey // some shortcut name
        property "sonar.projectName", projectName
        property "sonar.projectVersion", projectVersion
        property "sonar.branch", branch
        property "sonar.sourceEncoding", "UTF-8"
        property "sonar.scm.provider", "git"
        property "sonar.java.coveragePlugin", "jacoco"
    }
}
```

**Note:** if the Analysis is triggered from Jenkins server, environmental variables Job_Name and Buid_Number will be used to tag
the reports, otherwise "nonJenkinsBuild_1.0" will be used.

In your **app** module add:

```groovy
sonarqube {
    properties {
        property "sonar.jacoco.reportPath", "build/jacoco/testDebugUnitTest.exec"
        property "sonar.junit.reportsPath", "build/test-results/debug"
    }
}
```

Nothing is required in java modules.

