DEFAULT_PREFERENCE = "1"

include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
					file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
					"

SRCREV_FORMAT = "base"

SRC_URI = " git://anongit.freedesktop.org/gstreamer/gst-plugins-bad;branch=master;name=base \
			git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common \
			"

SRC_URI += "file://001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
			file://002-fix-maybe-uninitialized-warnings-when-compiling-with-Os.patch \
			file://003-configure-allow-to-disable-libssh2.patch \
			file://004-rtmp-fix-seeking-and-potential-segfault.patch \
			file://005-rtmp-hls-tsdemux-fix.patch \
			file://006-fix-compile.patch \
			"

S = "${WORKDIR}/git"

inherit gitpkgv

GST_VERSION_FULL = "1.17.0.1"
PV = "${GST_VERSION_FULL}+git${SRCPV}"
PKGV = "${GST_VERSION_FULL}+git${GITPKGV}"

EXTRA_OECONF += " \
	--disable-openjpeg \
	"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
