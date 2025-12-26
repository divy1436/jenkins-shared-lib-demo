def call(Map cfg = [:]) {
    // 1. Validate required input
    def projectKey = cfg.projectKey
    if (!projectKey) { 
        error "sonarScan: 'projectKey' is required. Example: sonarScan(projectKey: 'my-repo')" 
    }

    // 2. Setup defaults
    def serverName     = cfg.server         ?: 'sonar'
    def scannerTool    = cfg.scanner        ?: 'sonar-scanner'
    def projectName    = cfg.projectName    ?: projectKey
    def projectVersion = cfg.projectVersion ?: (env.BUILD_NUMBER ?: '1')
    def sources        = cfg.sources        ?: '.'  // Changed 'src' to '.' to be safer for root scans

    // 3. Handle Java binaries
    def binaries = cfg.binaries
    if (!binaries && fileExists('target/classes')) {
        binaries = 'target/classes'
    }

    // 4. Build arguments list
    def args = [
        "-Dsonar.projectKey=${projectKey}",
        "-Dsonar.projectName=${projectName}",
        "-Dsonar.projectVersion=${projectVersion}",
        "-Dsonar.sources=${sources}"
    ]
    
    if (binaries) {
        args << "-Dsonar.java.binaries=${binaries}"
    }

    // Add any extra properties passed in
    cfg.extraProps?.each { k, v ->
        args << "-D${k}=${v}"
    }

    // 5. Execution
    // This finds the path to the sonar-scanner binary installed in Jenkins Tools
    def scannerHome = tool name: scannerTool, type: 'hudson.plugins.sonar.SonarRunnerInstallation'
    
    // This injects the SONAR_HOST_URL and SONAR_AUTH_TOKEN from Jenkins System Config
    withSonarQubeEnv(serverName) {
        sh "${scannerHome}/bin/sonar-scanner ${args.join(' ')}"
    }
}
