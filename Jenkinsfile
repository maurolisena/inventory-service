pipeline {
    agent any

    stages {

        stage('Limpiar contenedores y volúmenes') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh '''
                        docker compose down --volumes --remove-orphans
                        docker volume rm mysql_database_data || true
                    '''
                }
            }
        }

        stage('Construir y levantar servicios') {
            steps {
                echo "Construyendo imágenes y levantando servicios en background"
                sh 'docker compose up -d --build'
            }
        }

        stage('Esperar a MySQL listo') {
            steps {
                echo "Esperando que MySQL esté saludable..."
                sh '''
                    until docker exec mysql_inventory_database mysqladmin ping -h 127.0.0.1 -u mlisena --password=A8424628 --silent; do
                        echo "Esperando MySQL..."
                        sleep 5
                    done
                '''
            }
        }

        stage('Verificar contenedores y logs de MySQL') {
            steps {
                echo "Listando contenedores activos"
                sh 'docker ps'

                echo "Mostrando últimos logs de mysql_inventory_database para verificar scripts init"
                sh 'docker logs --tail 30 mysql_inventory_database'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado'
        }
    }
}