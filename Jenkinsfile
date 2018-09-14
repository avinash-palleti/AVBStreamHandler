node ('sclb01') {
	stage('Prerequisites') {
		env.AVB_DEPS='/srv/jenkins/opt/libs'
		sh("mkdir -p $AVB_DEPS")
		sh("rm -rf dlt-daemon")
		sh("git clone https://github.com/GENIVI/dlt-daemon.git")
		dir ('dlt-daemon') {
			dir ('build') {
				sh("cmake .. -DCMAKE_INSTALL_PREFIX=$AVB_DEPS -DCMAKE_INSTALL_LIBDIR=lib -DWITH_DLT_CXX11_EXT=ON")
				sh("make && make install")
			}
		}
	}
	stage('Checkout') {
		checkout scm
		sh("git submodule update --init --recursive")		
	}
	
	stage('Build') {
		echo "Building AVBSH"
		sh "rm -rf build && mkdir build"
		dir ('build') {
		echo "Path : $AVB_DEPS"
		sh "env PKG_CONFIG_PATH=$AVB_DEPS/lib/pkgconfig cmake -DIAS_IS_HOST_BUILD=1 ../"
		sh "make setcap_tool"
		sh "install setcap_tool $AVB_DEPS/bin/"
		sh "sudo setcap cap_setfcap=pe $AVB_DEPS/bin/setcap_tool"
		sh "AVB_SETCAP_TOOL=$AVB_DEPS/bin/setcap_tool make"
	}
	}
}
