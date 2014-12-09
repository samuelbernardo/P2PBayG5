#!/bin/bash
CODEPLOY_HOME="../planetlab/codeploy"
P2PBAY_JAR_NAME="P2PBay.jar"
P2PBAY_JAR_LOCALPATH="../dist/lib/"
WWW_URL="http://web.ist.utl.pt/samuelbernardo/SEPRS/"
IST_LOGIN="ist146425"
SIGMA_PATH="./web/SEPRS/"
JAVA_SDK="../../java-sdk/jre-7u71-linux-x64.gz"
CODEPLOY_PACK="../planetlab/codeploy.tar.gz"
CODEPLOY_EXTRACT="$HOME/codeploy"
SCRIPTS_DIR="./"
BOOTSTAP_FILENAME="bootstrap.txt"
BOOTSTAP_LOCALFILE="./nodes.txt"

export MQ_NODES="./nodes.txt"
export MQ_SLICE='istple_seprs5'
export PATH="$CODEPLOY_HOME:$PATH"

multiquery 'rm -r $HOME/*'

