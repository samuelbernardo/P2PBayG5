#!/bin/bash
CODEPLOY_HOME="../planetlab/codeploy"
P2PBAY_JAR_NAME="P2PBay.jar"
P2PBAY_JAR_LOCALPATH="../dist/lib/"
WWW_URL="http://web.ist.utl.pt/samuelbernardo/SEPRS/"
IST_LOGIN="ist146425"
SIGMA_PATH="./web/SEPRS/"
JAVA_SDK_PATH="../../java-sdk/"
JAVA_SDK_FILE="jre-7u71-linux-x64.gz"
JAVA_EXTRACTED_DIR="jre1.7.0_71"
JAVA_DIR="java"
CODEPLOY_PACK="codeploy.tar.gz"
CODEPLOY_EXTRACT="./codeploy/"
CODEPLOY_SOURCE="../planetlab/"
SCRIPTS_DIR="./"
BOOTSTAP_FILENAME="bootstrap.txt"
BOOTSTAP_LOCALFILE="./nodes.txt"
SIGMA_SERVER="sigma02"
SLICE_USER="istple_seprs5"

export MQ_NODES="./nodes.txt"
export MQ_SLICE='istple_seprs5'
export PATH="./${JAVA_DIR}/bin:${CODEPLOY_HOME}:${PATH}"
export JAVA_HOME=./${JAVA_DIR}
export LD_LIBRARY_PATH="./${JAVA_DIR}/lib/amd64:/lib:./lib:$LD_LIBRARY_PATH"

multiquery 'java -jar $P2PBAY_JAR_NAME server'
java -jar $P2PBAY_JAR_NAME

