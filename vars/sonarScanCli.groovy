def call(Map cfg = [:]) {
  // --- Required input ---
  def projectKey = cfg.projectKey
  if (!projectKey) { error "sonarScan: 'projectKey' is required" }

  // --- Simple inputs with sane defaults ---
  def serverName     = cfg.server        ?: 'sonar'          // Jenkins > System > SonarQube servers (Name)
  def scannerTool    = cfg.scanner       ?: 'sonar-scanner'  // Jenkins > Tools > SonarQube Scanner (Name)
  def projectName    = cfg.projectName   ?: projectKey
  def projectVersion = cfg.projectVersion ?: (env.BUILD_NUMBER ?: '1')
  def sources        = cfg.sources       ?: 'src'

  // --- Java binaries: only if explicitly provided OR typical Maven folder exists ---
  def binaries = cfg.binaries
  if (!binaries && fileExists('target/classes')) {
    binaries = 'target/classes'
  }

  // --- Build CLI flags ---
  List<String> args = [
    "-Dsonar.projectKey=${projectKey}",
    "-Dsonar.projectName=${projectName}",
    "-Dsonar.projectVersion=${projectVersion}",
    "-Dsonar.sources=${sources}",
  ]
  if (binaries) {
    args += "-Dsonar.java.binaries=${binaries}"
  }
  (cfg.extraProps ?: [:]).each { k, v ->
    args += "-D${k}=${v}"
  }

  // --- Resolve scanner path and run inside SonarQube env (URL/token from Jenkins config) ---
  def scannerHome = tool name: scannerTool, type: 'hudson.plugins.sonar.SonarRunnerInstallation'
  withSonarQubeEnv(serverName) {
    sh "${scannerHome}/bin/sonar-scanner ${args.join(' ')}"
  }
}
