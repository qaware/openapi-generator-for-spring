#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<HELP_USAGE
Usage:
  $0 NEW_VERSION

  Tags the master branch with given NEW_VERSION and pushes to upstream.
  Ensures the current HEAD is master and has no untracked changes.
HELP_USAGE
  exit 1
}

NEW_VERSION=${1:-""}
if [[ -z "$NEW_VERSION" ]]; then
  echo "Error: First argument NEW_VERSION must be non-empty"
  usage
fi

ERROR_OCCURRED=""

if [[ "$(git rev-parse --abbrev-ref HEAD 2>/dev/null)" != "master" ]]; then
  echo "Error: The current branch is not master"
  ERROR_OCCURRED="1"
fi

if [[ -n "$(git status --porcelain)" ]]; then
  echo "Error: There are untracked changes. Please commit or stash them before releasing."
  ERROR_OCCURRED="1"
fi

TAG_NAME="version-$NEW_VERSION"
if [[ "$(git tag -l "$TAG_NAME")" == "$TAG_NAME" ]]; then
  echo "Error: Tag $TAG_NAME already exists locally"
  ERROR_OCCURRED="1"
fi

echo "Running mvn verify...(be patient)"
if ! mvn -q verify >/dev/null; then
  echo "Error: mvn verify has failed"
  ERROR_OCCURRED="1"
fi

REMOTE_TRACKING_REF=$(git for-each-ref --format='%(upstream:short)' "$(git symbolic-ref -q HEAD)" | tr -d "$IFS")
if [[ -z "${REMOTE_TRACKING_REF/[ ]*\n/}" ]]; then
  echo "Error: No remote tracking ref found"
  ERROR_OCCURRED="1"
fi
REMOTE_NAME=${REMOTE_TRACKING_REF%/*}

git fetch -q "$REMOTE_NAME" master
if [[ "$(git rev-list -1 FETCH_HEAD)" != "$(git rev-list -1 master)" ]]; then
  echo "Error: Upstream is not in sync with local master. Please pull and/or push before releasing."
  ERROR_OCCURRED="1"
fi

if git fetch -q "$REMOTE_NAME" $TAG_NAME 2>/dev/null; then
  echo "Error: Tag $TAG_NAME already exists remotely"
  ERROR_OCCURRED="1"
fi

if [[ -n "$ERROR_OCCURRED" ]]; then
  exit 1
fi

echo "Releasing version $NEW_VERSION..."
mvn -q versions:set -DgenerateBackupPoms=false "-DnewVersion=$NEW_VERSION"
git commit -am "Release version $NEW_VERSION"
git tag "$TAG_NAME"
git push "$REMOTE_NAME" "refs/heads/master" "refs/tags/$TAG_NAME"

echo "Updating versions in README and demo pom.xml..."

gsed -z -i -f - \
  README.md \
  demo/openapi-generator-for-spring-demo-webflux/pom.xml \
  demo/openapi-generator-for-spring-demo-webmvc/pom.xml \
  <<SED_SCRIPT
s~\(\
[ ]*<dependency>\n\
[ ]*<groupId>de.qaware.tools.openapi-generator-for-spring</groupId>\n\
[ ]*<artifactId>openapi-generator-for-spring-starter</artifactId>\n\
[ ]*<version>\).*\(</version>\)\
~\1$NEW_VERSION\2~
SED_SCRIPT
git commit -am "Use released version in README and demos"

mvn -q versions:set -DgenerateBackupPoms=false -DnextSnapshot=true >/dev/null
git commit -am "Start development on next version"
git push "$REMOTE_NAME" "refs/heads/master"
