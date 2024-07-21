FROM nginx:latest

# 필요한 디렉토리 생성
RUN mkdir -p /etc/nginx/ssl

# 설정 파일과 인증서 복사
COPY nginx.conf /etc/nginx/nginx.conf
COPY dhparam.pem /etc/nginx/ssl/dhparam.pem

# 컨테이너가 시작할 때 Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
