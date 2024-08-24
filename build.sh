#!/bin/bash
# Script to build image for beaglebone black.
# Author: Kareem Ibrahim.

git submodule init
git submodule sync
git submodule update

# local.conf won't exist until this step on first execution
source poky/oe-init-build-env

CONFLINE="MACHINE = \"beaglebone-yocto\""

cat conf/local.conf | grep "${CONFLINE}" > /dev/null
local_conf_info=$?

if [ $local_conf_info -ne 0 ];then
	echo "Append ${CONFLINE} in the local.conf file"
	echo ${CONFLINE} >> conf/local.conf

else
	echo "${CONFLINE} already exists in the local.conf file"
fi

bitbake-layers show-layers | grep "meta-mouse" > /dev/null
layer_info=$?

if [ $layer_info -ne 0 ];then
	echo "Adding meta-mouse layer"
	bitbake-layers add-layer ../meta-mouse
else
	echo "meta-mouse layer already exists"
fi

set -e

bitbake core-image-mouse
