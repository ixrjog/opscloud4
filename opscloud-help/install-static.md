#### 前端部署
```
# 创建前端目录
$ mkdir -p /opt/opscloud3-static
$ cd /opt/opscloud3-static
# Clone项目代码
$ git clone https://github.com/ixrjog/opscloud-web-dist
```

#### 安装Nginx
```
# CentOS7 eg:
$ yum install epel-release
$ yum install nginx
```

#### 配置Nginx
```
$ mkdir -p /data/logs/oc3
$ cd /etc/nginx/conf.d
$ vim vhost_oc3.conf
# 配置文件内容
upstream upstream.opscloud3 {
    server 127.0.0.1:8080 weight=2;
}

server {
        listen 80;
        server_name oc3.opscloud.top;

        client_max_body_size 100m;
        # 首页重定向
        rewrite ^/+$ https://oc3.opscloud.top/index.html permanent;
        location /oc3/ {
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header Host $host;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://upstream.opscloud3;
            proxy_http_version 1.1;
            proxy_buffering off;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }

        location / {
            root /opt/opscloud3-static/;
        }

        location /status {
           return 200;  
        }

        access_log  /data/logs/oc3/access.log  main;
}

```

#### 前端Aliyun-SLB配置
+ 如果在阿里云部署前端可以使用SLB代理Nginx实现集群高可用&https
+ opscloud使用了WebSocket协议,Web终端支持心跳保持会话
+ SLB配置监听
  + 负载均衡协议HTTPS
  + 后端协议HTTP
  + 启用HTTP2.0
  + 连接空闲超时时间(秒) 15
  + 连接请求超时时间(秒) 60