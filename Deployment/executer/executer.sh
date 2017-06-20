#!/bin/bash
cd ../../
rm -rf release
rm -rf installer
rm -rf apollo.tar

mkdir -vp release/lib
mkdir -vp release/config

cp -rf ApolloAgent/build/libs/* release/

cp -rf ApolloAgent/build/output/lib/* release/lib
cp -rf ApolloAgent/build/output/config/* release/config
cp -rf Deployment/Apollo/Apollo.sh release/

cd release

tar cf ../apollo.tar ./*

cd ..

mkdir installer
cp -rf Deployment/build installer
cp apollo.tar installer/build/payload
cd installer/build
./packager.sh
cd ../../