#!/bin/bash
CODEPLOY_HOME="../planetlab/codeploy"
P2PBAY_JAR_NAME="P2PBay.jar"
P2PBAY_JAR_LOCALPATH="../dist/lib/"
WWW_URL="http://web.ist.utl.pt/samuelbernardo/SEPRS/"
IST_LOGIN="ist146425"
SIGMA_PATH="./web/SEPRS/"
JAVA_SDK_PATH="../../java-sdk/"
JAVA_SDK_FILE="jre-7u71-linux-i586.gz"
JAVA_EXTRACTED_DIR="jre1.7.0_71"
JAVA_DIR="java"
CODEPLOY_PACK="codeploy.tar.gz"
CODEPLOY_EXTRACT="./codeploy/"
CODEPLOY_SOURCE="../planetlab/"
SCRIPTS_DIR="./"
BOOTSTAP_FILENAME="bootstrap.txt"
BOOTSTAP_LOCALFILE="./nodes_all.txt"
SIGMA_SERVER="sigma02"
SLICE_USER="istple_seprs5"

export MQ_NODES="./nodes_all.txt"
export MQ_SLICE='istple_seprs5'
export PATH="$CODEPLOY_HOME:$PATH"

rsync -auvz ${P2PBAY_JAR_LOCALPATH}${P2PBAY_JAR_NAME} ${JAVA_SDK_PATH}${JAVA_SDK_FILE} $IST_LOGIN@${SIGMA_SERVER}:$SIGMA_PATH
rsync -auvz $BOOTSTAP_LOCALFILE $IST_LOGIN@${SIGMA_SERVER}:${SIGMA_PATH}${BOOTSTAP_FILENAME}

# prepare remote servers
multicopy "$SCRIPTS_DIR/*" $MQ_NODES '@:.'
multiquery "mkdir $CODEPLOY_EXTRACT"
multicopy ${CODEPLOY_SOURCE}${CODEPLOY_PACK} @:${CODEPLOY_EXTRACT}
multiquery "cd $CODEPLOY_EXTRACT; tar zxvf $CODEPLOY_PACK; rm $CODEPLOY_PACK"

# sync project
multiquery "wget -q -O${JAVA_SDK_FILE} ${WWW_URL}${JAVA_SDK_FILE}; tar zxvf ${JAVA_SDK_FILE}; mv ${JAVA_EXTRACTED_DIR} ${JAVA_DIR}; rm ${JAVA_SDK_FILE}"
multiquery "wget -q -O${P2PBAY_JAR_NAME} ${WWW_URL}${P2PBAY_JAR_NAME}"
multiquery "wget -q -O${BOOTSTAP_FILENAME} ${WWW_URL}${BOOTSTAP_FILENAME}"

