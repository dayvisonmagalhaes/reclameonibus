<?php

include("./connection.php");
include("./functions.php");

$params = $_REQUEST;
$error = true;
$message = "Unknow error";
$response = array();

$params = splitData($params);

switch($params['action']){
	case "insert":
		//do signup
		$query = "INSERT INTO  `matheusn_reclameonibus`.`reclamacoes` (
					`Data` ,
					`linha_onibus` ,
					`num_ordem`,
					`hora_ocorrido` ,
					`data_ocorrido` ,
					`local_ocorrido`,
					`tipo_rec`
					)
				  VALUES (";
       	$query.="NOW(), '".$params['linha_onibus']."' , '".$params['num_ordem']."' , '".$params['hora_ocorrido']."' , '".$params['data_ocorrido']."' , '".$params['local_ocorrido']."' , '".$params['tipo_rec']."')";

		//echo $query;

		$result = mysql_query ($query);

		if(!$result){
			switch (mysql_errno($conn)) {
				
				default:
					$message = "Infelizmente não foi possível realizar o cadastro. Erro ".mysql_errno($conn);
					break;
			}
			$response = null;
		}
		else{
			$error = false;
			$message = "Reclamação criada com sucesso!";
			$response = mysql_insert_id();
		}

		break;
		
	case "list":
		$query = "SELECT v.*, e.nome AS regiao 
				  FROM  Vaga v , Estado e
				  WHERE ativo = '1'
				     AND estado_id = '".$params['estado']."'
				     AND estado_id = e.id
				     AND tipo LIKE '".$params['tipo']."'
				  ORDER BY v.insertedAt DESC;";

		//echo $query;

		$result = mysql_query ($query);

		if(!$result){
			if(mysql_errno($conn)) {
				$message = "Não foi possível encontrar nenhuma reclamação. Erro ".mysql_errno($conn);
			}
			$response = null;
		}
		else{
			$error = false;
			if(mysql_num_rows($result)==0){
				$message = "Nenhuma reclamação foi encontrada.";
				$response = null;
			}
			else{
				$message = "Reclamacões encontradas com sucesso!";
				$error = false;

				while ($row = mysql_fetch_assoc($result)) {
					$row['logo']= "http://topoportunidades.com.br/adm/vagas/logos/".$row['logo'];
					array_push($response, $row);
				}
			}
		}

		break;
	case "getNews":
		$query = "SELECT * FROM `Reclamacao` 
				WHERE HOUR(TIMEDIFF(NOW(), insertedAt)) <=24
				AND ativo = '1'";

		$result = mysql_query ($query);

		if(!$result){
			if(mysql_errno($conn)) {
				$message = "Não foi possível encontrar nenhuma reclamação nova. Erro ".mysql_errno($conn);
			}
			$response = null;
		}
		else{
			$error = false;
			if(mysql_num_rows($result)==0){
				$message = "Nenhuma nova reclamação foi encontrada.";
				$response = null;
			}
			else{
				$message = "Novas reclamações foram encontradas!";
				while ($row = mysql_fetch_assoc($result)) {
					array_push($response, $row);
				}
			}
		}
		break;
	default:
		$message = "Nenhuma ação foi definida";
		break;

}

echo json_encode(
	array(
		"error"=>$error,
		"message"=>$message,
		"response"=>$response
	)
);

?>