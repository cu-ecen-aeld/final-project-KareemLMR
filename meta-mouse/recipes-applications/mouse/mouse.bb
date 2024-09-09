# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"
SRC_URI = "git://git@github.com/KareemLMR/Wireless-Air-Gyroscopic-Mouse.git;protocol=ssh;branch=main"
PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "a09ecd53c31744e31c498438ccb227a5d7978a09"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/mouse"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
INITDIR="${sysconfdir}/init.d"
FILES:${PN} += "\
                   ${bindir}/driver \
                   ${INITDIR}/S99mouse \
               "

INSANE_SKIP:${PN} += "ldflags"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

export EXTRA_CFLAGS = "${CFLAGS}"
export EXTRA_LDFLAGS = "${LDFLAGS}"

EXTRA_OEMAKE = "CC='${CC}' LD='${CCLD}' V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y HOSTCC='${BUILD_CC}' HOSTCPP='${BUILD_CPP}'"

do_compile () {
	 oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
        install -d ${D}${bindir}
	install -m 0755 ${S}/driver ${D}${bindir}/

        install -d ${D}${INITDIR}
        install -m 0755 ${S}/mouse ${D}${INITDIR}/S99mouse
	
	install -d ${D}${sysconfdir}/rc5.d
	ln -sf ../init.d/S99mouse ${D}${sysconfdir}/rc5.d/S99mouse
}
