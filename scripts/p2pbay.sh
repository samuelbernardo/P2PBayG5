#!/bin/bash
export MQ_NODES="$HOME/nodes.txt"
export MQ_SLICE='istple_seprs5'
export PATH="$HOME/codeploy:$PATH"

multiquery 'wget -q http://web.ist.utl.pt/~ist165965/java.sh; chmod +x java.sh; ./java.sh'
multiquery 'javac teste.java'
multiquery 'java teste'
