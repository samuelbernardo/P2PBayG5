#!/bin/bash
export MQ_NODES="$HOME/nodes.txt"
export MQ_SLICE='istple_seprs5'
export PATH="$HOME/codeploy:$PATH"

multiquery 'wget -q http://web.ist.utl.pt/~ist165965/bootstrap.cfg'
multiquery 'wget -q http://web.ist.utl.pt/~ist165965/P2PBay.jar'
multiquery 'java -jar P2PBay.jar'