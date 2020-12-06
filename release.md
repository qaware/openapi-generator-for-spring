# Release process

1. `mvn clean package`
1. `export NEW_VERSION="Next version number"`
1. `mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$NEW_VERSION`
1. `git commit -am "Release version $NEW_VERSION"`
1. `mvn clean deploy -P release`
1. `git tag version-$NEW_VERSION`
1. `git push && git push --tags` Otherwise SonarQube does not pick up this version
1. `mvn versions:set -DgenerateBackupPoms=false -DnextSnapshot=true`
1. `git commit -am "Start development on next version"`
1. `git push`
1. Ping the maintainer of the `qaware-oss` to hit the publish button!
1. Adjust the README and the demo projects to use the released version
