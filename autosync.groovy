/*!
    @page Jenkinsfile
    Pipeline to build project.

    @section Pipeline dependencies:
    The following are required to run the pipeline:
    @section Required Jenkins varables:

    The following are required to be set in the jenkins job:
    @li http_proxy
    @li https_proxy
    @li ftp_proxy
    @li all_proxy
    @li no_proxy
    @li GIT_PROXY_COMMAND

**/
def commit_msg = "CI submodule autosync\n"
try {
    node {
        stage('Checkout') {
            checkout scm
            sh("git submodule update --init --recursive")       
        }

		stage('Sync') {
			branch = "ci_dev"
			repos = ["deps/gptp", "deps/igb_avb", "deps/audio/common"]
			for (repo in repos) {
				echo "Repo name $repo"
				dir (repo) {
					hash1 = sh(script: "git rev-parse --verify HEAD", returnStdout: true).trim()
				    sh("git fetch --all --prune")
					sh("git pull origin master")
					sh("git submodule update --recursive")
					hash2 = sh(script: "git rev-parse --verify HEAD", returnStdout: true).trim()
					if (hash1 == hash2) {
						echo "No update in submodule repo"
					} else {
						log = sh(script: "git log --oneline $hash1..$hash2", returnStdout: true).trim()
						commit_msg += "$repo\n"
						commit_msg += log
						echo "$commit_msg"
					}
				}
			}
		}
        stage('Build') {
        }
	}
}
catch (Exception e) {
    echo 'in expection'
    throw e
}
finally {
    echo 'in finally'
}

