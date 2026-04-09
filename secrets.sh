#!/usr/bin/env bash
# exit if any command produces an error
set -e

./resetdb.sh
./addsecret.sh foo bar
./addsecret.sh password P@ssw0rd!
./addsecret.sh hello world
./addsecret.sh marco polo
