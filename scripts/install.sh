#!/bin/sh
export MQ_NODES="$HOME/nodes.txt"
export MQ_SLICE="istple_seprs5"
./multiquery "wget -q web.ist.utl.pt/~ist165965/java.sh"
./multiquery "chmod +x java.sh"
./multiquery "./java.sh"