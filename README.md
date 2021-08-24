## elk日志处理环境搭建
 - springboot 2.5.3
 - elasticsearch 7.12.1
 - logstash 7.12.1
 
### run
#### win10
 - elasticsearch
进入elasticsearch/bin  
双击elasticsearch.bat  
 - logstash  
进入logstash/bin  
cmd执行  
```shell script
logstash.bat -f ../config/logstash-elk-demo.conf
```
 - kibana  
进入kibana/bin  
双击kibana.bat
   
#### ubuntu 21.04
 - elasticsearch  
```shell
./bin/elasticsearch
```
 - logstash  
```shell
./bin/logstash -f ./config/logstash-elk-demo.conf
```
指定运行的配置文件，可以写多个，运行时指定  
 - kibana
```shell
./bin/kibana
```

### ui
 - kibana ui  
 http://localhost:5601  
 - elasticsearch  
 http://localhost:9200  
    - indices  
    http://localhost:9200/_cat/indices
    
    
### config
```yaml
# elasticsearch/config/elasticsearch.yml
cluster.name: spring-first-application
node.name: node-1
cluster.initial_master_nodes: ["node-1"]
```
---
```yaml
# kibana/config/kibana.yml
i18n.locale: "zh-CN"
```
---
```conf
# new logstash/config/logstash-elk-demo.conf

# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {

  #springboot通过logstash-logback-encoder tcp发送日志
  tcp {
  	mode => "server"
    type => "logstash-logback-encoder-tcp-demo"
  	host => "localhost"
  	port => 4560
  	codec => json_lines
  }

  #读取指定的日志文件
  file {
    type => "spring-boot-log-file-demo"
    path => "/tmp/zhang/log/elk.log"
    codec => multiline {
      pattern => "^%{YEAR}-%{MONTHNUM}-%{MONTHDAY} %{TIME}.*"
      negate => "true"
      what => "previous"
    }
  }

}

output {

  if [type] == "logstash-logback-encoder-tcp-demo" {
    elasticsearch {
      hosts => ["http://localhost:9200"]
      index => "logstash-logback-encoder-tcp-demo-%{+YYYY.MM.dd}"
      #index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
      #user => "elastic"
      #password => "changeme"
    }
  }

  if [type] == "spring-boot-log-file-demo" {
    elasticsearch {
      hosts => ["http://localhost:9200"]
      index => "spring-boot-log-file-demo-%{+YYYY.MM.dd}"
      #index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
      #user => "elastic"
      #password => "changeme"
    }
  }

  #收到的信息进行打印
  stdout{
    codec => rubydebug
  }
  
}
```
    
### reference
1. https://github.com/logstash/logstash-logback-encoder#tcp-appenders  
2. https://chrome.google.com/webstore/detail/elasticsearch-head/ffmkiejjmecolpfloofpjologoblkegm  
