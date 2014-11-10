#!/bin/bash
JDK='jdk-8u25-linux-i586'

if [ ! -e "$JDK.gz"  ]; then
    if [ ! -d "jdk1.8.0_25"  ]; then
	echo Downloading $JDK
        wget -q web.ist.utl.pt/~ist165965/jdk-8u25-linux-i586.gz
	echo Extracting $JDK
        tar xzf jdk-8u25-linux-i586.gz
	echo Removing $JDK.gz
        rm $JDK.gz
        echo Adding Java to PATH
        echo "PATH=\$HOME/jdk1.8.0_25/bin/:\$PATH" >> .bash_profile
        source .bash_profile;
    fi;
fi

rm java.sh
echo Java is installed

