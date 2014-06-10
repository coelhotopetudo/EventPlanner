EventPlanner
========

QR code para eventos.

- A partir do cadastro de inscrições são geradas etiquetas com e-mail e nome do inscrito, mantidos no QR, que serão coladas no crachá.
- O celular carrega as informações de salas e participantes, via webservice, em uma tabela local no celular.
- Na entrada da palesta a pessoa apresenta o o QRcode que é lido pela aplicação confirmando ou não se a pessoa está inscrita e em caso positivo cadastra presença do mesmo.
- No final é feito acesso ao webservice que descarrega as informações de presença do celular no sistema.

Módulos:
- EventPlanner aplicação Android para coletar (lendo o QR code) e confirmar os inscritos. Possui um banco de dados local (no app)
- ftsl aplicação PHP que gera QR code e que pode fornecer os dados (dados.xml) base para o coletor

Acesse a wiki:
https://github.com/coelhotopetudo/EventPlanner/wiki
