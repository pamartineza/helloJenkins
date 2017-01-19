#Hello Jenkins App

This is an Android multi-module demo app to verify the proper configuration of a Jenkins CI server and a SonarQube Server, should work straightforward if your sonarQube server is at localhost:9000

I have wrote two extensive Medium articles explaining:
* How to configure Ubuntu + Jenkins + Sonarqube for Android development:
https://medium.com/@pamartineza/how-to-set-up-a-continuous-integration-server-for-android-development-ubuntu-jenkins-sonarqube-43c1ed6b08d3
* how to run Android Tests on cloud devices using a Jenkins CI server (Firebase Test Lab — Amazon Device Farm — Genymotion Cloud):
https://medium.com/@pamartineza/running-android-tests-on-cloud-devices-using-a-jenkins-ci-server-firebase-test-lab-amazon-device-b67cb4b16c40#.6055mdnuq


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

#Cloud testing

If you want to test Firebase Test Lab integration checkout "feature/fireTestLab" branch.

If you want to test Genumotion Cloud integration please checkout "feature/genymotion" branch.

Amazon Device Farm doesn't require any project configuration