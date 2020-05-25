# maven-plugins-template-common

Common collection of classes for creating Maven Plugins using templating libs 

[![Build Status](https://api.travis-ci.org/nerdynick/maven-plugins-template-common.png "Build Status")](https://travis-ci.org/nerdynick/maven-plugins-template-common)

## Dependency

**Maven**

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.nerdynick</groupId>
            <artifactId>maven-plugins-template-plugin-bom</artifactId>
            <version>${version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>com.nerdynick</groupId>
        <artifactId>maven-plugins-template-plugin-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.nerdynick</groupId>
        <artifactId>maven-plugins-template-plugin-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```