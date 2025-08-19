// Jenkinsfile (Multibranch) — 1civil.it : Spring Boot + Angular + Flutter + Sonar + Helm
pipeline {
  agent any

  options {
    skipDefaultCheckout(false)
    disableConcurrentBuilds()
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '20'))
  }

  environment {
    APP_SLUG = '1civil-it'
    REGISTRY = 'registry.example.com'                
    REGISTRY_CREDENTIALS = 'docker-registry'        
    HELM_RELEASE = 'onecivilit'
    DOCKER_REPO_API = "${REGISTRY}/${APP_SLUG}-api"
    DOCKER_REPO_WEB = "${REGISTRY}/${APP_SLUG}-web"

    MAVEN_CACHE = "${WORKSPACE}/.cache/m2"
    NPM_CACHE   = "${WORKSPACE}/.cache/npm"
    PUB_CACHE   = "${WORKSPACE}/.cache/flutter_pub"

    // Healthchecks (adapter si besoin)
    SMOKE_API_URL = 'https://api.dev.1civil.it/actuator/health'
    SMOKE_WEB_URL = 'https://dev.1civil.it'
  }

  stages {
    stage('Init meta') {
      steps {
        script {
          env.SHORT_SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          // Détection multibranch
          def isPR   = env.CHANGE_ID?.trim()
          def branch = env.BRANCH_NAME ?: 'local'
          def isTag  = env.TAG_NAME?.trim()

          // Mapping env déploiement
          def depEnv = (branch == 'main' || branch == 'master') ? 'prod' :
                       (branch == 'staging')                   ? 'staging' : 'dev'
          env.DEPLOY_ENV = depEnv

          // Règles de déploiement :
          // - PR : jamais
          // - develop/staging/main(+tags) : oui
          env.DEPLOY_ENABLED = (!isPR && (branch in ['develop','staging','main','master'] || isTag)) ? 'true' : 'false'

          // Build mobile seulement sur main/tags (modifiable)
          env.BUILD_MOBILE = ((branch in ['main','master']) || isTag) ? 'true' : 'false'

          // Config Angular par env
          env.ANGULAR_CONFIG = (depEnv == 'prod') ? 'production' : (depEnv == 'staging' ? 'staging' : 'development')

          sh 'mkdir -p .cache/m2 .cache/npm .cache/flutter_pub artifacts'
          echo "Branch=${branch} PR=${isPR ? 'yes' : 'no'} TAG=${isTag ? isTag : 'no'} DEPLOY_ENV=${env.DEPLOY_ENV} DEPLOY_ENABLED=${env.DEPLOY_ENABLED} BUILD_MOBILE=${env.BUILD_MOBILE}"
        }
      }
    }

    // ========= BACKEND (Spring Boot) =========
    stage('Backend • tests + package + JaCoCo') {
      steps {
        sh """
          docker run --rm \
            -v "${WORKSPACE}/backend:/app" \
            -v "${MAVEN_CACHE}:/root/.m2" \
            -w /app maven:3.9-eclipse-temurin-21 sh -lc '
              mvn -B -ntp clean verify jacoco:report
              cp target/*.jar /app/../artifacts/ || true
            '
        """
      }
    }

    stage('SonarQube • Backend') {
      steps {
        withSonarQubeEnv('sonarqube') {
          script {
            def sonarExtra = ''
            if (env.SONAR_ID) {
              sonarExtra = "-Dsonar.pullrequest.key=${env.SONAR_ID} -Dsonar.pullrequest.branch=${env.SONAR_BRANCH} -Dsonar.pullrequest.base=${env.SONAR_TARGET}"
            } else {
             
              sonarExtra = "-Dsonar.branch.name=${env.BRANCH_NAME}"
            }
            sh """
              docker run --rm \
                -e SONAR_HOST_URL -e SONAR_AUTH_TOKEN \
                -v "${WORKSPACE}/backend:/app" \
                -v "${MAVEN_CACHE}:/root/.m2" \
                -w /app maven:3.9-eclipse-temurin-21 sh -lc '
                  mvn -B -ntp sonar:sonar \
                    -Dsonar.projectKey=1civil-it-api \
                    -Dsonar.projectName="1civil.it API" \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.token=$SONAR_AUTH_TOKEN \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                    ${sonarExtra}
                '
            """
          }
        }
      }
    }

    stage('Quality Gate • Backend') {
      steps { timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true } }
    }

    // ========= FRONTEND (Angular) =========
    stage('Dashboard • Angular build + tests') {
      steps {
        sh """
          docker run --rm \
            -v "${WORKSPACE}/dashboard:/app" \
            -v "${NPM_CACHE}:/root/.npm" \
            -w /app node:20 sh -lc '
              if [ -f package-lock.json ]; then npm ci; else npm install; fi
              npm run lint || true
              npm test -- --watch=false --code-coverage || true
              npx -y @angular/cli@^18 build --configuration=${ANGULAR_CONFIG} || npx -y @angular/cli@^18 build --configuration=production
            '
          mkdir -p artifacts/dashboard && cp -r dashboard/dist/* artifacts/dashboard/ || true
        """
      }
    }

    stage('SonarQube • Dashboard') {
      steps {
        withSonarQubeEnv('sonarqube') {
          script {
            def sonarExtra = env.SONAR_ID ?
              "-Dsonar.pullrequest.key=${env.SONAR_ID} -Dsonar.pullrequest.branch=${env.SONAR_BRANCH} -Dsonar.pullrequest.base=${env.SONAR_TARGET}" :
              "-Dsonar.branch.name=${env.BRANCH_NAME}" // Dev/Ent edition
            sh """
              docker run --rm \
                -e SONAR_HOST_URL -e SONAR_AUTH_TOKEN \
                -v "${WORKSPACE}/dashboard:/usr/src" \
                sonarsource/sonar-scanner-cli:latest \
                  -Dsonar.projectKey=1civil-it-web \
                  -Dsonar.projectName='1civil.it Web' \
                  -Dsonar.host.url=$SONAR_HOST_URL \
                  -Dsonar.token=$SONAR_AUTH_TOKEN \
                  -Dsonar.sources=src \
                  -Dsonar.exclusions=**/*.spec.ts,**/node_modules/**,**/*.e2e-spec.ts \
                  -Dsonar.tests=src \
                  -Dsonar.test.inclusions=**/*.spec.ts,**/*.e2e-spec.ts \
                  -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info \
                  -Dsonar.sourceEncoding=UTF-8 \
                  ${sonarExtra}
            """
          }
        }
      }
    }

    stage('Quality Gate • Dashboard') {
      steps { timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true } }
    }

    // ========= MOBILE (Flutter APK, seulement main/tags par défaut) =========
    stage('Mobile • Flutter (APK)') {
      when { expression { return env.BUILD_MOBILE == 'true' } }
      steps {
        sh """
          docker run --rm \
            -v "${WORKSPACE}/mobile:/app" \
            -v "${PUB_CACHE}:/root/.pub-cache" \
            -w /app ghcr.io/cirruslabs/flutter:stable sh -lc '
              flutter pub get
              flutter test --coverage || true
              flutter build apk --release
            '
          mkdir -p artifacts/mobile && cp mobile/build/app/outputs/flutter-apk/app-release.apk artifacts/mobile/ || true
        """
      }
    }

    // ========= Docker (API + WEB) =========
    stage('Docker • build & push') {
      when { expression { return env.DEPLOY_ENABLED == 'true' } }
      steps {
        withCredentials([usernamePassword(credentialsId: "${env.REGISTRY_CREDENTIALS}", usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
          sh """
            echo "\$REG_PASS" | docker login ${env.REGISTRY} -u "\$REG_USER" --password-stdin

            DOCKER_BUILDKIT=1 docker build -f backend/Dockerfile   -t ${env.DOCKER_REPO_API}:${env.SHORT_SHA} backend
            DOCKER_BUILDKIT=1 docker build -f dashboard/Dockerfile -t ${env.DOCKER_REPO_WEB}:${env.SHORT_SHA} dashboard

            # Tag par environnement (dev/staging/prod)
            docker tag ${env.DOCKER_REPO_API}:${env.SHORT_SHA} ${env.DOCKER_REPO_API}:${env.DEPLOY_ENV}
            docker tag ${env.DOCKER_REPO_WEB}:${env.SHORT_SHA} ${env.DOCKER_REPO_WEB}:${env.DEPLOY_ENV}

            docker push ${env.DOCKER_REPO_API}:${env.SHORT_SHA}; docker push ${env.DOCKER_REPO_API}:${env.DEPLOY_ENV}
            docker push ${env.DOCKER_REPO_WEB}:${env.SHORT_SHA}; docker push ${env.DOCKER_REPO_WEB}:${env.DEPLOY_ENV}
          """
        }
      }
    }

    // ========= Déploiement Helm =========
    stage('Deploy • Helm (canary)') {
      when { expression { return env.DEPLOY_ENABLED == 'true' } }
      steps {
        withCredentials([file(credentialsId: 'kubeconfig-1civil', variable: 'KUBECONFIG')]) {
          sh """
            helm upgrade --install ${env.HELM_RELEASE} deploy/chart \
              --namespace ${env.APP_SLUG} --create-namespace \
              --set env=${env.DEPLOY_ENV} \
              --set api.image.repository=${env.DOCKER_REPO_API} --set api.image.tag=${env.SHORT_SHA} \
              --set web.image.repository=${env.DOCKER_REPO_WEB} --set web.image.tag=${env.SHORT_SHA} \
              --set canary.enabled=true --set canary.weight=10
          """
        }
      }
    }

    stage('Smoke tests') {
      when { expression { return env.DEPLOY_ENABLED == 'true' } }
      steps {
        sh """
          curl -fsSL "${env.SMOKE_API_URL}" | head -n 5 > /dev/null
          curl -fsSL "${env.SMOKE_WEB_URL}" | head -n 5 > /dev/null
        """
      }
    }

    stage('Promotion canary ➜ 100% / Approbation PROD') {
      when { expression { return env.DEPLOY_ENABLED == 'true' } }
      steps {
        script {
          if (env.DEPLOY_ENV == 'prod') {
            input message: "Promouvoir à 100% en PROD ?", ok: "Promote"
          }
        }
        withCredentials([file(credentialsId: 'kubeconfig-1civil', variable: 'KUBECONFIG')]) {
          sh """
            helm upgrade --install ${env.HELM_RELEASE} deploy/chart \
              --namespace ${env.APP_SLUG} \
              --set env=${env.DEPLOY_ENV} \
              --set api.image.repository=${env.DOCKER_REPO_API} --set api.image.tag=${env.SHORT_SHA} \
              --set web.image.repository=${env.DOCKER_REPO_WEB} --set web.image.tag=${env.SHORT_SHA} \
              --set canary.enabled=false
          """
        }
      }
    }
  }

  post {
    success {
      junit testResults: 'backend/target/surefire-reports/*.xml', allowEmptyResults: true
      archiveArtifacts artifacts: 'artifacts/**/*', allowEmptyArchive: true
      echo "✅ ${env.BRANCH_NAME ?: 'local'} : build OK — images ${env.DOCKER_REPO_API}:${env.SHORT_SHA} / ${env.DOCKER_REPO_WEB}:${env.SHORT_SHA}"
    }
    unsuccessful {
      echo "❌ Échec : tentative de rollback Helm (si release existe)"
      script {
        try {
          withCredentials([file(credentialsId: 'kubeconfig-1civil', variable: 'KUBECONFIG')]) {
            sh """
              set -e
              REV=\$(helm history ${env.HELM_RELEASE} -n ${env.APP_SLUG} --output json | jq -r '.[-2].revision' || echo '')
              [ -n "\$REV" ] && helm rollback ${env.HELM_RELEASE} "\$REV" -n ${env.APP_SLUG} || echo "Pas de révision précédente."
            """
          }
        } catch (err) { echo "Rollback non effectué: ${err}" }
      }
    }
    always {
      cleanWs(deleteDirs: true, notFailBuild: true)
    }
  }
}
