# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.3] - 2023-07-13
### Changed
- Replaced kirby-link.dd-dns.de addresses with phoenix.ipv64.de

## [1.0.2] - 2023-07-13
### Fixed
- Changed missing formatting and information about SonarQube badges token in README.md

## [1.0.1] - 2023-07-13
### Fixed
- Changed formatting in README.md

## [1.0.0] - 2023-07-13
### Added
- Increasing minor number on dev after release as default
- Information about Gotify notification in README.md

### Fixed
- Alpine image in clone step in drone.yml

### Changed
- Updated dependencies in pom.xml
- Added Sonarqube badges token placeholder in README.md (new in Sonarqube 9.9 LTS)

## [0.3.0] - 2021-11-22
### Added
- Gotify notification on all drone pipelines

### Fixed
- Updated dependencies
- drone/git instead of apline/git for clone step of Pull Request analysis to use user defined dns server instead of 8.8.8.8 and 8.8.4.4 

## [0.2.0] - 2021-11-22
### Changed
- Improved build script

## [0.1.1] - 2021-08-15

### Fixed
- Information about gitea_token in README.md added
- Changes in CHANGELOG.md renamed (e. g. Added instead Adding)

## [0.1.0] - 2021-08-03

### Added
- Drone.io pipeline for publishing artifacts to Gitea

### Changed
- slf4j-simple artifact removed and replaced with logback-classic and logback-core and logback xml setting files for structured logging function.
- CHANGELOG.md unreleased link. compares permanently master with HEAD

## [0.0.2] - 2021-08-01

### Fixed
- Set `$SONAR_HOST` instead `$SONAR_TOKEN`  for sonar.host.url in drone-script-hotfix-finish-sonarqube.sh
- Set `$SONAR_HOST` secret to SonarQube steps in .drone.yml
- Set `$DRONE_SOURCE_BRANCH` as sonar.pullrequest.branch in .drone.yml
- Adding slf4j-simple artifact to pom for non Spring Boot applications
- Updating README.md

## [0.0.1] - 2021-07-29

### Added
- Initial files

[unreleased]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/master...HEAD
[1.0.3]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/1.0.2...1.0.3
[1.0.2]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/1.0.1...1.0.2
[1.0.1]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/1.0.0...1.0.1
[1.0.0]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.3.0...1.0.0
[0.3.0]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.2.0...0.3.0
[0.2.0]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.1.1...0.2.0
[0.1.1]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.1.0...0.1.1
[0.1.0]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.0.2...0.1.0
[0.0.2]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/compare/0.0.1...0.0.2
[0.0.1]: https://git.phoenix.ipv64.de/David/Maven-Blueprint-Project.git/releases/tag/0.0.1