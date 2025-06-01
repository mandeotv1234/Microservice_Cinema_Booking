pipeline {
    agent any

    tools {
        maven 'Maven'
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

                    // Lưu kết quả vào biến trong currentBuild để dùng ở stage sau
                    currentBuild.displayName = "#${BUILD_NUMBER} ${detected.join(',')}"
                    // Ghi vào file tạm
                    writeFile file: 'changed_services.txt', text: detected.join(',')
                }
            }
        }

        stage('Build Services') {
            steps {
                script {
                    // Đọc lại danh sách service từ file
                    def changed = readFile('changed_services.txt').trim()
                    def services = changed.tokenize(',')

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
