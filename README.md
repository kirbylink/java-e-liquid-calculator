[![Build Status](https://drone.phoenix.ipv64.de/api/badges/<gitea_path_to_project>/status.svg?ref=refs/heads/master&token=<sonarqube_badges_token>)](https://drone.phoenix.ipv64.de/<gitea_path_to_project>) for `master`<br />
[![Build Status develop branch](https://drone.phoenix.ipv64.de/api/badges/<gitea_path_to_project>/status.svg?ref=refs/heads/develop&token=<sonarqube_badges_token>)](https://drone.phoenix.ipv64.de/<gitea_path_to_project>) for `develop`<br />
[![Bugs](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=bugs&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Code Smells](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=code_smells&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Duplizierte Quellcodezeilen (%)](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=duplicated_lines_density&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Technische Schulden](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=sqale_index&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Vulnerabilities](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=vulnerabilities&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Quellcodezeilen](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=ncloc&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)<br />
[![SQALE-Bewertung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=sqale_rating&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Reliability Rating](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=reliability_rating&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Security Rating](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=security_rating&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)<br />
[![Alarmhinweise](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=alert_status&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>)
[![Abdeckung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=<project.groupId>%3A<project.artifactId>&metric=coverage&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>) for `master`<br /> 
[![Abdeckung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?branch=develop&project=<project.groupId>%3A<project.artifactId>&metric=coverage&token=<sonarqube_badges_token>)](https://sonarqube.phoenix.ipv64.de/dashboard?id=<project.groupId>%3A<project.artifactId>&branch=develop) for `develop`<br /> 

# Blue-Print Maven Project

This file contains steps, that should be done, after creating a new Maven project from this template.

Table of contents:
- [General](#general)
- [Project source files](#project-source-files)
	- [README.md](#readme-md)
	- [CHANGELOG.md](#changelog-md)
	- [LICENSE](#license)
	- [pom.xml](#pom-xml)
	- [sonar-project.properties (optional)](#sonar-project-properties-optional)
	- [.drone-script-hotfix-finish.sh](#drone-script-hotfix-finish-sh)
	- [.drone.yml](#drone-yml)
- [Gitea](#gitea)
	- [Settings](#settings)
- [SonarQube](#sonarqube)
- [Drone.io](#drone-io)


## General
This file describes where "blue print" values placeholders are for the new project and which values they have to be replaced with.

## Project source files
The following files must be updated with project specific values.

### README.md
- Replace `<project.groupId>` with project.groupId from pom.xml in the badges.
- Replace `<project.artifactId>` with project.artifactId from pom.xml in the badges.
- Replace `<gitea_path_to_project>` with Gitea URL path to the project in the badges.
- Replace `<sonarqube_badges_token>` with the SonarQube badges token. This can be found in the project in SonarQube when it is created.

### CHANGELOG.md
- CHANGELOG.md contains the version history of the blueprint maven project. This must be changed.

### LICENSE
- Change LICENSE if another license is needed 

### pom.xml
- Replace project.artifactId with new artifactId in pom.xml

- If the project is no Spring Boot project, do the following steps:
	- Add 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-compiler-plugin: 3.8.1
	- Add 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-surefire-plugin: 2.22.2
	- Add 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-failsafe-plugin: 2.22.2
	- Set 'project.properties.main.class' to Class with main inside if the project is a runnable jar.

- If the project is a Spring Boot project, do the following steps:
	- Remove 'build.plugins.plugin' maven-assembly-plugin since Spring Boot will create running jars

### sonar-project.properties (optional)
- enable `sonar.projectKey` and set new key. Default is &lt;project.groupId&gt;:&lt;project.artifactId&gt; from pom.xml
- enable `sonar.projectName` and set new name. Default is &lt;project.name&gt; from pom.xml

### .drone-script-hotfix-finish.sh
- Replace `<SSH_GITEA_URL>` with the ssh url of project.

### .drone.yml
- Replace `<SSH_GITEA_URL>` with the ssh url of project.

## Gitea
The following steps must be done for the new project.

### Settings
- Deploy-Key (Deploy-Schlüsse)
	- Add new Deploy-Key to project
		- Titel = `Drone.io`
		- Content = `ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPfqLs0W2nViUQqS5+L48OWtGgk3wLAc8w0rQogo9ozv drone@phoenix.ipv64.de`
		- Mark `Enable Write Access`
- Branches
	- Set develop as standard branch
	- Branch protection
		- Select `develop`
		- Enable branch protection
		- Whitelist Restricted Push
		- Enable `Whitelist deploy keys with write access to push`
		- Mark `Enable Status Check`
			- If available mark `continuous-integration/drone/pr`
			- If not available a status check must be done and then it is available
		- Repeat branch protection for `master`

## SonarQube
If `sonar.projectKey` changed run the following command in the project folder:

```
mvn sonar:sonar \
  -Dsonar.projectKey=<sonar.projectKey> \
  -Dsonar.host.url=<sonar.host.url> \
  -Dsonar.login=<SONAR_TOKEN>
```

If `sonar.projectKey` is **NOT** changed run the following command in the project folder:
```
mvn sonar:sonar \
  -Dsonar.host.url=<sonar.host.url> \
  -Dsonar.login=<SONAR_TOKEN>
```

Same init analysis must be done for develop branch.
If `sonar.projectKey` changed run the following command in the project folder:

```
mvn sonar:sonar \
  -Dsonar.projectKey=<sonar.projectKey> \
  -Dsonar.host.url=<sonar.host.url> \
  -Dsonar.branch.name=develop \
  -Dsonar.login=<SONAR_TOKEN>
```

If `sonar.projectKey` is **NOT** changed run the following command in the project folder:
```
mvn sonar:sonar \
  -Dsonar.host.url=<sonar.host.url> \
  -Dsonar.branch.name=develop \
  -Dsonar.login=<SONAR_TOKEN>
```


Replace `<sonar.projectKey>`, `<sonar.host.url>` and `<SONAR_TOKEN>` with the known values.

## Drone.io
### Single Repository
The following steps must be done for the new project.

- Synchronize projects to find new gitea project
- Enabling the project
- Add new secrets with `Allow Pull Requests`
	- SONAR_TOKEN: The token that is needed to connect to SonarQube
	- SONAR_HOST: The url to the SonarQube server
	- plugin_gotifytoken: The token that is needed to connect to Gotify
	- plugin_gotifyendpoint: The url to the Gotify server
- Add new secret without `Allow Pull Requests`
	- gitea_token: The token that is needed to publish new artifacts to gitea

### Organization
When several Repositories in one organization using the template then the token can be set with drone organziation secrets.
- Administrator for drone must be enabled
- If not yet done install drone cli from [here](https://docs.drone.io/cli/install/)
- Export drone server and token:
```
export DRONE_SERVER=<url>
export DRONE_TOKEN=<token>
```
- Adding `SONAR_TOKEN, SONAR_HOST and gitea_token` with the following commands:
```
drone orgsecret add --allow-pull-request <organization> SONAR_HOST <SONAR_HOST>
drone orgsecret add --allow-pull-request <organization> SONAR_TOKEN <SONAR_TOKEN>
drone orgsecret add --allow-pull-request <organization> plugin_gotifyendpoint <plugin_gotifyendpoint>
drone orgsecret add --allow-pull-request <organization> plugin_gotifytoken <plugin_gotifytoken>
drone orgsecret add <organization> gitea_token <gitea_token>
```


