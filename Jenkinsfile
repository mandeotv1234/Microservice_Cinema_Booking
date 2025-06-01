pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        CHANGED_SERVICES = ""
    }

    stages {
        stage('Detect Changes') {
            steps {
                script {
                    def changes = sh(script: 'git diff --name-only origin/main', returnStdout: true).trim().split("\n")
                    def services = ['auth-service', 'movie-service', 'ticket-service', 'user-service', 'config-server', 'discovery-server', 'api-gateway']
                    def detected = []

                    for (line in changes) {
                        def parts = line.tokenize('/')
                        if (parts.size() > 0 && services.contains(parts[0]) && !detected.contains(parts[0])) {
                            detected.add(parts[0])
                        }
                    }

                    if (detected.isEmpty()) {
                        echo "⚠️ Không có service nào thay đổi. Dùng mặc định: discovery-server, config-server"
                        detected = ['discovery-server', 'config-server']
                    }

                    CHANGED_SERVICES = detected.join(',')
                    echo "📦 Các service thay đổi: ${CHANGED_SERVICES}"
                }
            }
        }

        stage('Build Services') {
            steps {
                script {
                    def services = CHANGED_SERVICES.tokenize(',')
                    for (svc in services) {
                        dir(svc) {
                            if (!fileExists('pom.xml')) {
                                echo "⚠️ Bỏ qua ${svc} vì không có pom.xml"
                                continue
                            }

                            echo "🔨 Build service: ${svc}"
                            sh "mvn clean package -DskipTests"
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
            }
        }
    }
}
