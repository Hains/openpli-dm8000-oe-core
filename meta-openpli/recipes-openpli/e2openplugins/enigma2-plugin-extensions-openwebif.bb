MODULE = "OpenWebif"
DESCRIPTION = "Control your receiver with a browser"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=26abba37d1c2fcbf96a087ceb8e1db86"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "python-cheetah-native"
RDEPENDS_${PN} = "\
	aio-grab \
	python-cheetah \
	python-compression\
	python-ipaddress\
	python-json \
	python-misc \
	python-numbers \
	python-pyopenssl \
	python-shell \
	python-unixadmin \
	"

inherit gitpkgv

require openplugins-distutils.inc

SRC_URI += "file://get-rid-of-orgdream-check.patch"

PV = "1+git${SRCPV}"
PKGV = "1+git${GITPKGV}"

python do_cleanup () {

    import os

    pluginpath = "%s%s" % (d.getVar('D', True), d.getVar('PLUGINPATH', True))
    images = "%s/public/images/" % pluginpath
    keymaps = "%s/public/static/" % pluginpath

    target_box = 'dm8000.png'
    target_remote = 'dmm1.png'
    target_keymap = 'dmm1.html'
    exception = ''

    for root, dirs, files in os.walk(images + 'boxes', topdown=False):
        for name in files:
            if target_box != name and name != 'unknown.png' and exception != name:
                os.remove(os.path.join(root, name))

    for root, dirs, files in os.walk(images + 'remotes', topdown=False):
        for name in files:
            if target_remote != name and name != 'ow_remote.png' and exception != name:
                os.remove(os.path.join(root, name))

    for root, dirs, files in os.walk(keymaps + 'remotes', topdown=False):
        for name in files:
            if target_keymap != name:
                os.remove(os.path.join(root, name))
}

addtask do_cleanup after do_populate_sysroot before do_package

# Just a quick hack to "compile" it
do_compile() {
	cheetah-compile -R --nobackup ${S}/plugin
	python2 -O -m compileall -d ${PLUGINPATH} ${S}/plugin
}

PLUGINPATH = "${libdir}/enigma2/python/Plugins/Extensions/${MODULE}"
do_install_append() {
	install -d ${D}${PLUGINPATH}
	cp -r ${S}/plugin/* ${D}${PLUGINPATH}
	chmod a+rX ${D}${PLUGINPATH}
}

FILES_${PN}-src += "${PLUGINPATH}/controllers/views/*.tmpl ${PLUGINPATH}/controllers/views/*/*.tmpl ${PLUGINPATH}/controllers/views/*/*/*.tmpl"
FILES_${PN} = "${PLUGINPATH}"

PACKAGES =+ "${PN}-vxg"
DESCRIPTION_${PN}-vxg = "Adds Google Chrome support to OpenWebif's WebTV"
FILES_${PN}-vxg = "${libdir}/enigma2/python/Plugins/Extensions/OpenWebif/public/vxg"
RDEPENDS_${PN}-vxg =+ "${PN}"
