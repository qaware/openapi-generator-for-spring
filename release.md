# Release process

1. `mvn versions:set -DgenerateBackupPoms=false -DremoveSnapshot`
1. `git commit -am "Release version $NEW_VERSION"`
1. `mvn clean deploy -P release`
1. `git tag version-$NEW_VERSION`
1. `mvn versions:set -DgenerateBackupPoms=false -DnextSnapshot=true`
1. `git commit -am "Start development on next version"`
1. `git push && git push --tags`
1. Ping the maintainer of the `qaware-oss` to hit the publish button!
