copy integration-test-1.0.0.jar 到Dockerfile相同路径
docker build -t jxp/test:v2 .
docker run -d --name integration -p 8081:8081  -p 8082:8082 jxp/test:v2