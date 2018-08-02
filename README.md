# authorization-soluction
modelo de um autorizador simplificado.

# authorization-server
  app com porta 9876 tcp em listener.
  faz parse da mensagem e distribui para o cluster.

# authorization-core (cluster-hazelcast)
  app onde mensagem é processada.
  
# authorization-data
  lib com objetos para acesso a banco de dados.

# authorization-dto
  lib com objetos de transporte.

# authorization-contract
  lib de interfaces.
  
  
  
# exemplo de como integrar com o authorization-server:
     src/test/java/br/com/hubfintech/authorization/tcp/AuthorizationTcpClient

# exemplo de script para start da plataforma
>./start.sh

#!/bin/bash
cd authorization-server
mvn clean install dockerfile:build

cd ..
cd authorization-core
mvn clean install dockerfile:build

cd ..
nohup docker run springio/authorization-core &
nohup docker run springio/authorization-server &
