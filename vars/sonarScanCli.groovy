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
    def sources        = cfg.sources        ?: '.' 

    // 3. Handle Java binaries
    def binaries = cfg.binaries
    if (!binaries && fileExists('target/classes')) {
        binaries = 'target/classes'
    }

    // 4. Build arguments list
    // Use a List for better handling of spaces in arguments
    def args = [
        "-Dsonar.projectKey=${projectKey}",
        "-Dsonar.projectName=${projectName}",
        "-Dsonar.projectVersion=${projectVersion}",
        "-Dsonar.sources=${sources}"
    ]
    
    if (binaries) {
        args << "-Dsonar.java.binaries=${binaries}"
    }

    cfg.extraProps?.each { k, v ->
        args << "-D${k}=${v}"
    }

    // 5. Execution
    // CHANGE: Simplified 'tool' call. 
    // This works better if you have correctly named the tool in 'Global Tool Configuration'
    def scannerHome = tool scannerTool
    
    // Ensure the scannerHome is added to PATH or called directly
    withSonarQubeEnv(serverName) {
        // Use double quotes for the whole string to allow variable expansion
        sh "${scannerHome}/bin/sonar-scanner ${args.join(' ')}"
    }
}
